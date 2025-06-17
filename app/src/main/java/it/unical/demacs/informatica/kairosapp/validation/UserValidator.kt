package it.unical.demacs.informatica.kairosapp.validation

import android.content.Context
import android.util.Patterns
import it.unical.demacs.informatica.kairosapp.R
import java.util.regex.Pattern

object UserValidator {
    private val USERNAME_ALLOWED_CHARS_REGEX = "^[a-zA-Z0-9._-]+$".toRegex()
    private val PASSWORD_SPECIAL_CHARS_REGEX = Pattern.compile(".*[@#$%^&+=!*()_\\-].*")

    fun validateFirstName(firstName: String, context: Context): String? {
        return when {
            firstName.isBlank() -> context.getString(R.string.error_empty_first_name)
            firstName.length < 2 || firstName.length > 50 -> context.getString(R.string.error_invalid_first_name)
            else -> null
        }
    }

    fun validateLastName(lastName: String, context: Context): String? {
        return when {
            lastName.isBlank() -> context.getString(R.string.error_empty_last_name)
            lastName.length < 2 || lastName.length > 50 -> context.getString(R.string.error_invalid_last_name)
            else -> null
        }
    }

    fun validateUsername(username: String, context: Context): String? {
        val minLength = 4
        val maxLength = 30
        return when {
            username.isBlank() -> context.getString(R.string.error_empty_username)
            username.length < minLength || username.length > maxLength -> "${context.getString(R.string.username_length_error)} $minLength - $maxLength"
            !username.matches(USERNAME_ALLOWED_CHARS_REGEX) -> context.getString(R.string.username_allowed_chars_error)
            else -> null
        }
    }

    fun validateEmail(email: String, context: Context): String? {
        return when {
            email.isBlank() -> context.getString(R.string.error_empty_email)
            !Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> context.getString(R.string.error_invalid_email)

            else -> null
        }
    }

    fun validatePassword(password: String, context: Context): String? {
        val minLength = 8
        val maxLength = 15

        return when {
            password.isBlank() -> context.getString(R.string.error_empty_password)
            password.length < minLength -> "${context.getString(R.string.error_password_min_length)} $minLength"
            password.length > maxLength -> "${context.getString(R.string.error_password_max_length)} $maxLength"
            !password.any { it.isUpperCase() } -> context.getString(R.string.error_password_uppercase)
            !password.any { it.isLowerCase() } -> context.getString(R.string.error_password_lowercase)
            !password.any { it.isDigit() } -> context.getString(R.string.error_password_digit)
            !PASSWORD_SPECIAL_CHARS_REGEX.matcher(password)
                .matches() -> context.getString(R.string.error_password_special_char)

            else -> null
        }
    }

    fun validateRegistrationForm(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String,
        context: Context
    ): Map<String, String?> {
        val errors = mutableMapOf<String, String?>()

        errors["firstName"] = validateFirstName(firstName, context)
        errors["lastName"] = validateLastName(lastName, context)
        errors["username"] = validateUsername(username, context)
        errors["email"] = validateEmail(email, context)
        errors["password"] = validatePassword(password, context)

        return errors
    }
}
