package it.unical.demacs.informatica.kairosapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.swagger.client.apis.AuthenticationApi
import io.swagger.client.models.PasswordResetRequest
import it.unical.demacs.informatica.kairosapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ForgotPasswordState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val usernameOrEmail: String = "",
)

class ForgotPasswordViewModel(application: Application) : AndroidViewModel(application) {
    private val _application = application
    private val _authApi: AuthenticationApi = AuthenticationApi()
    private val _uiState = MutableStateFlow(ForgotPasswordState())
    val uiState: StateFlow<ForgotPasswordState> = _uiState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(usernameOrEmail = newEmail) }
        clearMessages()
    }

    fun sendPasswordResetRequest() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    successMessage = null
                )
            }

            val usernameOrEmail = _uiState.value.usernameOrEmail
            if (usernameOrEmail.isBlank()) {
                _uiState.update {
                    it.copy(
                        errorMessage = _application.getString(R.string.error_empty_fields),
                        isLoading = false
                    )
                }
                return@launch
            }

            try {
                withContext(Dispatchers.IO) {
                    _authApi.forgotPassword(
                        PasswordResetRequest(
                            usernameOrEmail = usernameOrEmail
                        )
                    )
                }

                _uiState.update {
                    it.copy(
                        successMessage = _application.getString(R.string.message_password_reset_success),
                        isLoading = false,
                        usernameOrEmail = ""
                    )
                }
            } catch (e: Exception) {
                Log.e(
                    "ForgotPasswordViewModel",
                    "Error sending password reset request: ${e.message}",
                    e
                )
                _uiState.update {
                    it.copy(
                        errorMessage = _application.getString(R.string.error_password_reset_generic),
                        isLoading = false
                    )

                }
            }

        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
