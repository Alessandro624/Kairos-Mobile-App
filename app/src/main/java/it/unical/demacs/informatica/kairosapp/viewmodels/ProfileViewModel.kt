package it.unical.demacs.informatica.kairosapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.swagger.client.apis.UsersApi // Assicurati sia UsersApi e non UserApi
import io.swagger.client.models.UserDTO
import it.unical.demacs.informatica.kairosapp.R
import it.unical.demacs.informatica.kairosapp.validation.UserValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import io.swagger.client.infrastructure.ClientException
import io.swagger.client.infrastructure.ServerException
import io.swagger.client.models.UserUpdateDTO // Questo è il DTO per l'aggiornamento
import it.unical.demacs.informatica.kairosapp.security.AuthManager

data class UserProfileUiState(
    val id: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val email: String = "",
    val phoneNumber: String? = null,
    val profileImageUrl: String? = null,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val phoneNumberError: String? = null,

    val originalFirstName: String = "",
    val originalLastName: String = "",
    val originalPhoneNumber: String? = null,
    val originalUsername: String = "",
    val originalEmail: String = "",
    val originalProfileImageUrl: String? = null
)

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val _application = application
    private val _uiState = MutableStateFlow(UserProfileUiState())
    private val _userApi: UsersApi = UsersApi()
    private val _authManager: AuthManager = AuthManager.getInstance(application)

    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val accessToken = _authManager.getAccessToken()
                if (accessToken == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = _application.getString(R.string.error_user_not_logged_in)
                        )
                    }
                    return@launch
                }

                val user: UserDTO = withContext(Dispatchers.IO) {
                    _userApi.getCurrentUser(accessToken)
                }

                _uiState.update {
                    it.copy(
                        id = user.id,
                        firstName = user.firstName,
                        lastName = user.lastName,
                        username = user.username,
                        email = user.email,
                        phoneNumber = user.phoneNumber,
                        profileImageUrl = user.profileImage?.photoUrl,
                        isLoading = false,
                        originalFirstName = user.firstName,
                        originalLastName = user.lastName,
                        originalPhoneNumber = user.phoneNumber,
                        originalUsername = user.username,
                        originalEmail = user.email,
                        originalProfileImageUrl = user.profileImage?.photoUrl,
                    )
                }
            } catch (e: Exception) {
                Log.e("UserProfileViewModel", "Failed to fetch user profile: ${e.message}", e)
                val errorMessage = when (e) {
                    is ClientException -> {
                        _application.getString(R.string.error_user_not_logged_in)
                    }

                    is ServerException -> _application.getString(R.string.error_fetch_profile_generic_client)
                    else -> _application.getString(R.string.error_fetch_profile_api_failure)
                }
                _uiState.update { it.copy(isLoading = false, errorMessage = errorMessage) }
            }
        }
    }

    fun toggleEditMode() {
        _uiState.update {
            if (it.isEditing) {
                it.copy(
                    isEditing = false,
                    firstName = it.originalFirstName,
                    lastName = it.originalLastName,
                    phoneNumber = it.originalPhoneNumber,
                    firstNameError = null,
                    lastNameError = null,
                    phoneNumberError = null,
                    errorMessage = null,
                    saveSuccess = false
                )
            } else {
                it.copy(isEditing = true, errorMessage = null, saveSuccess = false)
            }
        }
    }

    fun updateFirstName(input: String) {
        _uiState.update { it.copy(firstName = input, firstNameError = null, errorMessage = null) }
    }

    fun updateLastName(input: String) {
        _uiState.update { it.copy(lastName = input, lastNameError = null, errorMessage = null) }
    }

    fun updatePhoneNumber(input: String) {
        _uiState.update {
            it.copy(
                phoneNumber = input,
                phoneNumberError = null,
                errorMessage = null
            )
        }
    }

    fun saveUserProfile() {
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                saveSuccess = false,
                firstNameError = null,
                lastNameError = null,
                phoneNumberError = null
            )
        }

        val current = _uiState.value
        val userId = current.id

        if (userId == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = _application.getString(R.string.error_user_id_missing)
                )
            }
            return
        }

        val errors = UserValidator.validateUserProfileUpdate(
            current.firstName,
            current.lastName,
            current.phoneNumber,
            _application
        )

        var hasError = false
        _uiState.update { currentState ->
            currentState.copy(
                firstNameError = errors["firstName"].also { if (it != null) hasError = true },
                lastNameError = errors["lastName"].also { if (it != null) hasError = true },
                phoneNumberError = errors["phoneNumber"].also { if (it != null) hasError = true }
            )
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = _application.getString(R.string.error_please_correct_fields)
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                val updatedUser = UserUpdateDTO(
                    firstName = current.firstName,
                    lastName = current.lastName,
                    phoneNumber = current.phoneNumber
                )

                val accessToken = _authManager.getAccessToken()
                if (accessToken == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = _application.getString(R.string.error_user_not_logged_in)
                        )
                    }
                    return@launch
                }

                withContext(Dispatchers.IO) {
                    _userApi.updateUser(updatedUser, userId, accessToken)
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEditing = false,
                        saveSuccess = true,
                        errorMessage = null,
                        originalFirstName = current.firstName,
                        originalLastName = current.lastName,
                        originalPhoneNumber = current.phoneNumber
                    )
                }
                Log.d("UserProfileViewModel", "User profile updated successfully for ID: $userId")

            } catch (e: Exception) {
                Log.e("UserProfileViewModel", "Failed to save user profile: ${e.message}", e)
                val errorMessage = when (e) {
                    is ClientException -> {
                        _application.getString(R.string.error_user_not_logged_in)
                    }

                    is ServerException -> _application.getString(R.string.error_update_profile_generic_client)
                    else -> _application.getString(R.string.error_update_profile_api_failure)
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMessage,
                        saveSuccess = false
                    )
                }
            }
        }
    }
}
