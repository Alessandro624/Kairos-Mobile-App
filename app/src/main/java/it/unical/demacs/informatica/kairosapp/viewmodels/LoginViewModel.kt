package it.unical.demacs.informatica.kairosapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.swagger.client.apis.AuthenticationApi
import io.swagger.client.models.AuthRequest
import io.swagger.client.models.AuthResponse
import it.unical.demacs.informatica.kairosapp.R
import it.unical.demacs.informatica.kairosapp.security.AuthManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class LoginUiState(
    val usernameOrEmail: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false,
    val isPasswordVisible: Boolean = false
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val _application = application
    private val _uiState = MutableStateFlow(LoginUiState())
    private val _authApi = AuthenticationApi()
    private val _authManager = AuthManager(application)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateUsernameOrEmail(usernameOrEmail: String) {
        _uiState.update { it.copy(usernameOrEmail = usernameOrEmail) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, loginSuccess = false) }

            val usernameOrEmail = _uiState.value.usernameOrEmail
            val password = _uiState.value.password

            if (usernameOrEmail.isBlank() || password.isBlank()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = _application.getString(R.string.error_empty_fields)
                    )
                }
                return@launch
            }

            try {
                val loginRequest =
                    AuthRequest(usernameOrEmail = usernameOrEmail, password = password)

                val loginResponse: AuthResponse =
                    withContext(Dispatchers.IO) { _authApi.login(loginRequest) }

                _authManager.saveTokens(
                    accessToken = loginResponse.token.toString(),
                    refreshToken = loginResponse.refreshToken.toString()
                )

                _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
                Log.d("LoginViewModel", "Login successful for user: $usernameOrEmail")
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login failed: ${e.message}", e)
                val errorMessage = when (e) {
                    is io.swagger.client.infrastructure.ServerException -> {
                        _application.getString(R.string.error_invalid_credentials)
                    }

                    else -> _application.getString(R.string.error_login_generic)
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
            }
        }
    }

    fun resetLoginState() {
        _uiState.update { it.copy(loginSuccess = false, errorMessage = null) }
    }
}
