package it.unical.demacs.informatica.kairosapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.swagger.client.apis.AuthenticationApi
import io.swagger.client.models.UserCreateDTO
import it.unical.demacs.informatica.kairosapp.R
import it.unical.demacs.informatica.kairosapp.validation.UserValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class RegistrationStep {
    PERSONAL_INFO,
    ACCOUNT_INFO
}

data class RegistrationUiState(
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val registrationSuccess: Boolean = false,
    val errorMessage: String? = null,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val currentStep: RegistrationStep = RegistrationStep.PERSONAL_INFO
)

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {
    private val _application = application
    private val _uiState = MutableStateFlow(RegistrationUiState())
    private val _authApi: AuthenticationApi = AuthenticationApi()

    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    fun updateFirstName(input: String) {
        _uiState.update { it.copy(firstName = input, firstNameError = null, errorMessage = null) }
    }

    fun updateLastName(input: String) {
        _uiState.update { it.copy(lastName = input, lastNameError = null, errorMessage = null) }
    }

    fun updateUsername(input: String) {
        _uiState.update { it.copy(username = input, usernameError = null, errorMessage = null) }
    }

    fun updateEmail(input: String) {
        _uiState.update { it.copy(email = input, emailError = null, errorMessage = null) }
    }

    fun updatePassword(input: String) {
        _uiState.update { it.copy(password = input, passwordError = null, errorMessage = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun navigateToNextStep() {
        val current = _uiState.value
        when (current.currentStep) {
            RegistrationStep.PERSONAL_INFO -> {
                val firstNameError =
                    UserValidator.validateFirstName(current.firstName, _application)
                val lastNameError = UserValidator.validateLastName(current.lastName, _application)

                _uiState.update {
                    it.copy(
                        firstNameError = firstNameError,
                        lastNameError = lastNameError,
                        errorMessage = null
                    )
                }

                if (firstNameError == null && lastNameError == null) {
                    _uiState.update { it.copy(currentStep = RegistrationStep.ACCOUNT_INFO) }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = _application.getString(R.string.error_please_fill_all_fields_correctly)
                        )
                    }
                }
            }

            RegistrationStep.ACCOUNT_INFO -> {
                register()
            }
        }
    }

    fun navigateToPreviousStep() {
        _uiState.update {
            when (it.currentStep) {
                RegistrationStep.PERSONAL_INFO -> it
                RegistrationStep.ACCOUNT_INFO -> it.copy(
                    currentStep = RegistrationStep.PERSONAL_INFO,
                    usernameError = null,
                    passwordError = null,
                    errorMessage = null
                )
            }
        }
    }

    fun register() {
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                registrationSuccess = false,
                firstNameError = null,
                lastNameError = null,
                usernameError = null,
                emailError = null,
                passwordError = null
            )
        }

        val current = _uiState.value
        val errors = UserValidator.validateRegistrationForm(
            current.firstName,
            current.lastName,
            current.username,
            current.email,
            current.password,
            _application
        )

        var hasError = false
        _uiState.update { currentState ->
            currentState.copy(
                firstNameError = errors["firstName"].also { if (it != null) hasError = true },
                lastNameError = errors["lastName"].also { if (it != null) hasError = true },
                usernameError = errors["username"].also { if (it != null) hasError = true },
                emailError = errors["email"].also { if (it != null) hasError = true },
                passwordError = errors["password"].also { if (it != null) hasError = true }
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
                val registrationRequest = UserCreateDTO(
                    firstName = current.firstName,
                    lastName = current.lastName,
                    username = current.username,
                    email = current.email,
                    password = current.password
                )

                withContext(Dispatchers.IO) {
                    _authApi.register(registrationRequest)
                }

                _uiState.update { it.copy(isLoading = false, registrationSuccess = true) }
                Log.d(
                    "RegistrationViewModel",
                    "Registration successful for user: ${current.username}"
                )

            } catch (e: Exception) {
                Log.e("RegistrationViewModel", "Registration failed: ${e.message}", e)
                val errorMessage = when (e) {
                    is io.swagger.client.infrastructure.ServerException -> {
                        _application.getString(R.string.error_registration)
                    }

                    else -> _application.getString(R.string.error_registration_api_failure)
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

    fun resetRegistrationState() {
        _uiState.update {
            it.copy(
                registrationSuccess = false,
                errorMessage = null,
                currentStep = RegistrationStep.PERSONAL_INFO
            )
        }
    }
}
