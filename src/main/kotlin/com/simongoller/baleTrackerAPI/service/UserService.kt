package com.simongoller.baleTrackerAPI.service

import com.simongoller.baleTrackerAPI.jwt.JwtUtils
import com.simongoller.baleTrackerAPI.model.response.UserDeletionResponse
import com.simongoller.baleTrackerAPI.model.user.User
import com.simongoller.baleTrackerAPI.model.user.UserDTO
import com.simongoller.baleTrackerAPI.repositroy.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpInputMessage
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service


@Service
class UserService(
    @Autowired val userRepository: UserRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)

    fun getUser(): ResponseEntity<UserDTO> {
        val user = getCurrentUser()
        val userDTO = user?.let {
            user.id?.let { it1 -> UserDTO(it1, user.email, it.username) } }
        return ResponseEntity.ok(userDTO)
    }

    fun deleteUser(): ResponseEntity<UserDeletionResponse> {
        getCurrentUser()?.let {
            it.id?.let { it1 -> userRepository.deleteById(it1)
                return ResponseEntity.ok(UserDeletionResponse.DELETED)
            }
        }
        return ResponseEntity.badRequest().body(UserDeletionResponse.NOT_DELETED)
    }

    private fun getCurrentUser(): User? {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        return userRepository.findByUsername(auth.name)
    }
}