package com.simongoller.baleTrackerAPI.model.user

import com.simongoller.baleTrackerAPI.utils.TimeUtils
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

data class UserRegisterDTO(
    val email: String,
    val username: String,
    val password: String
) {
    private val timeUtils: TimeUtils = TimeUtils()
    private val encoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    fun toUser(): User {
        return User(email, username, encoder.encode(password), timeUtils.getCurrentDateTimeInFormat())
    }
}
