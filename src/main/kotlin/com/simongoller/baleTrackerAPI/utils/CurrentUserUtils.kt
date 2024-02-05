package com.simongoller.baleTrackerAPI.utils

import com.simongoller.baleTrackerAPI.model.user.User
import com.simongoller.baleTrackerAPI.repositroy.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class CurrentUserUtils(
    @Autowired private val userRepository: UserRepository,
) {
    fun getUser(): User? {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        return userRepository.findByUsername(auth.name)
    }

    fun getUserId(): String? {
        return getUser()?.id
    }
}