package it.unical.demacs.informatica.kairosapp.security

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import it.unical.demacs.informatica.kairosapp.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthManager private constructor(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    private val _isLoggedIn = MutableStateFlow(checkIsLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isAdmin = MutableStateFlow(checkIsAdmin())
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    companion object {
        @Volatile
        private var instance: AuthManager? = null

        fun getInstance(context: Context): AuthManager =
            instance ?: synchronized(this) {
                instance ?: AuthManager(context.applicationContext).also { instance = it }
            }

        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val TOKEN_EXPIRATION_TIME_KEY = "token_expiration_time_millis"
        private const val ROLE_KEY = "role"
        private const val REFRESH_BUFFER_MILLIS = 5 * 60 * 1000L // refresh 5 mains before exp
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        val expirationTimeMillis = TokenUtils.getTokenExpirationTimeMillis(accessToken)

        val roles: List<TokenUtils.JwtRole> = TokenUtils.getTokenRoles(accessToken)

        val userRole = roles.firstOrNull()?.authority

        sharedPreferences.edit {
            putString(ACCESS_TOKEN_KEY, accessToken)
            putString(REFRESH_TOKEN_KEY, refreshToken)
            putLong(TOKEN_EXPIRATION_TIME_KEY, expirationTimeMillis)
            putString(ROLE_KEY, userRole)
        }

        Log.d("AuthManager", "Tokens and Role saved.")
        Log.d("AuthManager", "Access Token expires: ${java.util.Date(expirationTimeMillis)}")
        Log.d("AuthManager", "User Role: ${userRole ?: "N/A"}")

        _isLoggedIn.update { checkIsLoggedIn() }
        _isAdmin.update { checkIsAdmin() }
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
    }

    fun getTokenExpirationTimeMillis(): Long {
        return sharedPreferences.getLong(TOKEN_EXPIRATION_TIME_KEY, 0L)
    }

    fun getRole(): String? {
        return sharedPreferences.getString(ROLE_KEY, null)
    }

    fun getNextRefreshDelayMillis(): Long {
        val expirationTime = getTokenExpirationTimeMillis()
        if (expirationTime == 0L) return 0L

        val timeUntilExpiration = expirationTime - System.currentTimeMillis()
        val refreshDelay = timeUntilExpiration - REFRESH_BUFFER_MILLIS

        val minDelayMillis = 10 * 1000L
        return if (refreshDelay > minDelayMillis) refreshDelay else minDelayMillis
    }

    fun clearTokens() {
        sharedPreferences.edit {
            remove(ACCESS_TOKEN_KEY)
            remove(REFRESH_TOKEN_KEY)
            remove(TOKEN_EXPIRATION_TIME_KEY)
            remove(ROLE_KEY)
        }
        Log.d("AuthManager", "Token cleared.")

        _isLoggedIn.update { checkIsLoggedIn() }
        _isAdmin.update { checkIsAdmin() }
    }

    private fun checkIsLoggedIn(): Boolean {
        val accessToken = getAccessToken()
        val expirationTime = getTokenExpirationTimeMillis()
        val isTokenValid = accessToken != null && expirationTime > System.currentTimeMillis()
        Log.d(
            "AuthManager",
            "checkIsLoggedIn: $isTokenValid (Token present: ${accessToken != null}, Expired: ${expirationTime <= System.currentTimeMillis()})"
        )
        return isTokenValid
    }

    private fun checkIsAdmin(): Boolean {
        val userRole = getRole()
        val isAdminUser = userRole == UserRole.ADMIN.toString()
        Log.d("AuthManager", "checkIsAdmin: $isAdminUser (User role: ${userRole ?: "N/A"})")
        return isAdminUser
    }

    fun isLoggedIn(): Boolean {
        return _isLoggedIn.value
    }

    fun isAdmin(): Boolean {
        return _isAdmin.value
    }
}
