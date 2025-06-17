package it.unical.demacs.informatica.kairosapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.swagger.client.apis.UsersApi
import io.swagger.client.infrastructure.ClientException
import io.swagger.client.infrastructure.ServerException
import io.swagger.client.models.UserDTO
import it.unical.demacs.informatica.kairosapp.R
import it.unical.demacs.informatica.kairosapp.security.AuthManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import io.swagger.client.models.UserPage
import it.unical.demacs.informatica.kairosapp.model.UserRole

data class AdminUiState(
    val users: List<UserDTO> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val deleteSuccessMessage: String? = null,
    val roleUpdateSuccessMessage: String? = null,
    val userToDelete: UserDTO? = null,
    val userToEditRole: UserDTO? = null,
    val showDeleteDialog: Boolean = false,
    val showRoleChangeDialog: Boolean = false,

    val currentPage: Int = 0,
    val pageSize: Int = 10,
    val totalElements: Long = 0,
    val totalPages: Int = 0,
    val sortBy: String = "username",
    val sortDirection: String = "ASC"
)

class AdminViewModel(application: Application) : AndroidViewModel(application) {
    private val _application = application
    private val _uiState = MutableStateFlow(AdminUiState())
    private val _userApi: UsersApi = UsersApi()
    private val _authManager: AuthManager = AuthManager.getInstance(application)
    val currentUsername: StateFlow<String?> = _authManager.currentUsername

    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        fetchUsers()
    }

    fun fetchUsers() {
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

                val currentPage = _uiState.value.currentPage
                val pageSize = _uiState.value.pageSize
                val sortBy = _uiState.value.sortBy
                val sortDirection = _uiState.value.sortDirection

                val pageResponse: UserPage = withContext(Dispatchers.IO) {
                    _userApi.getAllUsers(
                        page = currentPage,
                        size = pageSize,
                        sortBy = sortBy,
                        direction = sortDirection,
                        authorization = accessToken
                    )
                }

                val users = pageResponse.content ?: emptyList()

                val totalElements = when (val elements = pageResponse.totalElements) {
                    is Double -> elements.toLong()
                    is Long -> elements
                    is Int -> elements.toLong()
                    else -> 0L
                }

                val totalPages = when (val pages = pageResponse.totalPages) {
                    is Double -> pages.toInt()
                    is Int -> pages
                    else -> 0
                }

                _uiState.update {
                    it.copy(
                        users = users,
                        isLoading = false,
                        errorMessage = null,
                        totalElements = totalElements,
                        totalPages = totalPages
                    )
                }
            } catch (e: Exception) {
                Log.e("AdminViewModel", "Failed to fetch users: ${e.message}", e)
                val errorMessage = when (e) {
                    is ClientException -> _application.getString(R.string.error_admin_unauthorized)
                    is ServerException -> _application.getString(R.string.error_fetch_users_generic_server)
                    else -> _application.getString(R.string.error_fetch_users_api_failure)
                }
                _uiState.update { it.copy(isLoading = false, errorMessage = errorMessage) }
            }
        }
    }

    fun goToNextPage() {
        if (_uiState.value.currentPage < _uiState.value.totalPages - 1) {
            _uiState.update { it.copy(currentPage = it.currentPage + 1) }
            fetchUsers()
        }
    }

    fun goToPreviousPage() {
        if (_uiState.value.currentPage > 0) {
            _uiState.update { it.copy(currentPage = it.currentPage - 1) }
            fetchUsers()
        }
    }

    fun setPageSize(newSize: Int) {
        if (newSize > 0 && newSize != _uiState.value.pageSize) {
            _uiState.update { it.copy(pageSize = newSize, currentPage = 0) }
            fetchUsers()
        }
    }

    fun setSortBy(newSortBy: String) {
        if (newSortBy != _uiState.value.sortBy) {
            _uiState.update { it.copy(sortBy = newSortBy, currentPage = 0) }
            fetchUsers()
        }
    }

    fun toggleSortDirection() {
        _uiState.update {
            it.copy(
                sortDirection = if (it.sortDirection == "ASC") "DESC" else "ASC",
                currentPage = 0
            )
        }
        fetchUsers()
    }

    fun confirmDeleteUser(user: UserDTO) {
        if (user.username == _authManager.currentUsername.value) {
            _uiState.update { it.copy(errorMessage = _application.getString(R.string.error_cannot_delete_self)) }
            return
        }
        _uiState.update { it.copy(userToDelete = user, showDeleteDialog = true) }
    }

    fun dismissDeleteDialog() {
        _uiState.update {
            it.copy(
                userToDelete = null,
                showDeleteDialog = false,
                deleteSuccessMessage = null
            )
        }
    }

    fun deleteUser() {
        val user = _uiState.value.userToDelete
        if (user == null) {
            _uiState.update { it.copy(errorMessage = _application.getString(R.string.error_no_user_to_delete)) }
            return
        }
        if (user.username == _authManager.currentUsername.value) {
            _uiState.update {
                it.copy(
                    errorMessage = _application.getString(R.string.error_cannot_delete_self),
                    isLoading = false
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                deleteSuccessMessage = null,
                showDeleteDialog = false
            )
        }
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

                withContext(Dispatchers.IO) {
                    _userApi.deleteUser(user.id, accessToken)
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        deleteSuccessMessage = _application.getString(R.string.user_deleted_success),
                        users = it.users.filter { u -> u.id != user.id },
                        userToDelete = null
                    )
                }
            } catch (e: Exception) {
                Log.e("AdminViewModel", "Failed to delete user: ${e.message}", e)
                val errorMessage = when (e) {
                    is ClientException -> _application.getString(R.string.error_admin_unauthorized)
                    is ServerException -> _application.getString(R.string.error_delete_user_generic_server)
                    else -> _application.getString(R.string.error_delete_user_api_failure)
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMessage,
                        userToDelete = null
                    )
                }
            }
        }
    }

    fun prepareForRoleChange(user: UserDTO) {
        if (user.username == _authManager.currentUsername.value) {
            _uiState.update { it.copy(errorMessage = _application.getString(R.string.error_cannot_change_own_role)) }
            return
        }
        _uiState.update { it.copy(userToEditRole = user, showRoleChangeDialog = true) }
    }

    fun dismissRoleChangeDialog() {
        _uiState.update {
            it.copy(
                userToEditRole = null,
                showRoleChangeDialog = false,
                roleUpdateSuccessMessage = null
            )
        }
    }

    fun updateUserRole(userId: String, newRole: String) {
        val user = _uiState.value.userToEditRole
        if (user == null || user.id != userId) {
            _uiState.update { it.copy(errorMessage = _application.getString(R.string.error_no_user_to_update)) }
            return
        }

        if (user.username == _authManager.currentUsername.value) {
            _uiState.update {
                it.copy(
                    errorMessage = _application.getString(R.string.error_cannot_change_own_role),
                    isLoading = false
                )
            }
            return
        }

        if (user.role.equals(newRole.removePrefix("ROLE_"), ignoreCase = true)) {
            _uiState.update {
                it.copy(
                    errorMessage = _application.getString(R.string.error_role_already_set),
                    isLoading = false,
                    showRoleChangeDialog = false
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                roleUpdateSuccessMessage = null,
                showRoleChangeDialog = false
            )
        }
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

                withContext(Dispatchers.IO) {
                    val authHeader = accessToken
                    when (newRole) {
                        UserRole.ADMIN -> _userApi.makeUserAdmin(userId, authHeader)
                        UserRole.ORGANIZER -> _userApi.makeUserOrganizer(userId, authHeader)
                        else -> {
                            throw IllegalArgumentException("Attempted to set an unknown role: $newRole")
                        }
                    }
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        roleUpdateSuccessMessage = _application.getString(R.string.user_role_updated_success),
                        users = it.users.map { u ->
                            if (u.id == userId) u.copy(role = newRole.removePrefix("ROLE_")) else u
                        },
                        userToEditRole = null
                    )
                }
            } catch (e: Exception) {
                Log.e("AdminViewModel", "Failed to update user role: ${e.message}", e)
                val errorMessage = when (e) {
                    is ClientException -> {
                        _application.getString(R.string.error_admin_unauthorized)
                    }

                    is ServerException -> _application.getString(R.string.error_update_role_generic_server)
                    is IllegalArgumentException -> e.message
                    else -> _application.getString(R.string.error_update_role_api_failure)
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMessage,
                        userToEditRole = null
                    )
                }
            }
        }
    }
}
