package it.unical.demacs.informatica.kairosapp.security

import android.util.Base64
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

object TokenUtils {
    data class JwtRole(val authority: String)

    fun decodeToken(token: String): JSONObject {
        val tokenParts = token.split(".")
        if (tokenParts.size < 2) {
            throw IllegalArgumentException("Invalid JWT format: missing payload part.")
        }
        val decodedPayload = String(Base64.decode(tokenParts[1], Base64.URL_SAFE))
        val jsonPayload = JSONObject(decodedPayload)
        return jsonPayload
    }

    fun getTokenExpirationTimeMillis(token: String): Long {
        return try {
            val decodedToken = decodeToken(token)
            val exp = decodedToken.getLong("exp")
            exp * 1000L
        } catch (_: Exception) {
            0L
        }
    }

    fun getTokenRoles(token: String): List<JwtRole> {
        val roleList = mutableListOf<JwtRole>()
        try {
            val decodedToken = decodeToken(token)
            val rolesJsonArray: JSONArray? = decodedToken.optJSONArray("roles")

            rolesJsonArray?.let {
                for (i in 0 until it.length()) {
                    val roleObject: JSONObject = it.getJSONObject(i)
                    val authority = roleObject.optString("authority")
                    if (authority.isNotEmpty()) {
                        roleList.add(JwtRole(authority))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("TokenUtils", "Error parsing token roles: ${e.message}", e)
        }
        return roleList
    }
}
