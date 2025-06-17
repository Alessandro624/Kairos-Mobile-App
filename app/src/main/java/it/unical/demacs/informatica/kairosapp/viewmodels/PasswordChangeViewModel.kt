package it.unical.demacs.informatica.kairosapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.swagger.client.apis.UsersApi
import it.unical.demacs.informatica.kairosapp.R
import it.unical.demacs.informatica.kairosapp.security.AuthManager
import it.unical.demacs.informatica.kairosapp.validation.UserValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import io.swagger.client.infrastructure.ClientException
import io.swagger.client.models.UserPasswordUpdateDTO

data class PasswordChangeUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val isCurrentPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isConfirmNewPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val passwordChangeSuccess: Boolean = false,
    val errorMessage: String? = null,
    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmNewPasswordError: String? = null
)

class PasswordChangeViewModel(application: Application) : AndroidViewModel(application) {
    private val _application = application
    private val _uiState = MutableStateFlow(PasswordChangeUiState())
    private val _userApi: UsersApi = UsersApi()
    private val _authManager: AuthManager = AuthManager.getInstance(application)

    val uiState: StateFlow<PasswordChangeUiState> = _uiState.asStateFlow()

    fun updateCurrentPassword(input: String) {
        _uiState.update {
            it.copy(
                currentPassword = input,
                currentPasswordError = null,
                errorMessage = null
            )
        }
    }

    fun updateNewPassword(input: String) {
        _uiState.update {
            it.copy(
                newPassword = input,
                newPasswordError = null,
                errorMessage = null
            )
        }
    }

    fun updateConfirmNewPassword(input: String) {
        _uiState.update {
            it.copy(
                confirmNewPassword = input,
                confirmNewPasswordError = null,
                errorMessage = null
            )
        }
    }

    fun toggleCurrentPasswordVisibility() {
        _uiState.update { it.copy(isCurrentPasswordVisible = !it.isCurrentPasswordVisible) }
    }

    fun toggleNewPasswordVisibility() {
        _uiState.update { it.copy(isNewPasswordVisible = !it.isNewPasswordVisible) }
    }

    fun toggleConfirmNewPasswordVisibility() {
        _uiState.update { it.copy(isConfirmNewPasswordVisible = !it.isConfirmNewPasswordVisible) }
    }

    fun changePassword() {
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                passwordChangeSuccess = false,
                currentPasswordError = null,
                newPasswordError = null,
                confirmNewPasswordError = null
            )
        }

        val current = _uiState.value
        val accessToken = _authManager.getAccessToken()

        if (accessToken == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = _application.getString(R.string.error_user_not_logged_in)
                )
            }
            return
        }

        val errors = UserValidator.validatePasswordChange(
            current.currentPassword,
            current.newPassword,
            current.confirmNewPassword,
            _application
        )

        var hasError = false
        _uiState.update { currentState ->
            currentState.copy(
                currentPasswordError = errors["currentPassword"].also {
                    if (it != null) hasError = true
                },
                newPasswordError = errors["newPassword"].also { if (it != null) hasError = true },
                confirmNewPasswordError = errors["confirmNewPassword"].also {
                    if (it != null) hasError = true
                }
            )
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = _application.getString(R.string.error_please_fill_all_fields_correctly)
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                val passwordChangeRequest = UserPasswordUpdateDTO(
                    oldPassword = current.currentPassword,
                    newPassword = current.newPassword
                )

                withContext(Dispatchers.IO) {
                    _userApi.changePassword(passwordChangeRequest, accessToken)
                }

                _uiState.update { it.copy(isLoading = false, passwordChangeSuccess = true) }
                Log.d(
                    "PasswordChangeViewModel",
                    "Password changed successfully for logged user"
                )
            } catch (e: Exception) {
                Log.e("PasswordChangeViewModel", "Password change failed: ${e.message}", e)
                val errorMessage = when (e) {
                    is ClientException -> {
                        _application.getString(R.string.error_password_change_failure)
                    }

                    else -> _application.getString(R.string.password_change_failure)
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMessage,
                        passwordChangeSuccess = false
                    )
                }
            }
        }
    }

    fun resetState() {
        _uiState.update {
            PasswordChangeUiState()
        }
    }
}
