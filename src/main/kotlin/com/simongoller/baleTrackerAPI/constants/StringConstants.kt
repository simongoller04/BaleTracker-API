package com.simongoller.baleTrackerAPI.constants

enum class RegistrationState {
    USERNAME_TAKEN,
    EMAIL_TAKEN,
    USER_CREATED
}

enum class LoginError {
    INVALID_CREDENTIALS
}
class StringConstants {
    companion object {
        const val USERNAME_TAKEN = "Username is already taken!"
        const val EMAIL_TAKEN = "Email is already in use!"
        const val USER_CREATED = "User created:"
        const val INVALID_CREDENTIALS = "Invalid username or password"
    }
}