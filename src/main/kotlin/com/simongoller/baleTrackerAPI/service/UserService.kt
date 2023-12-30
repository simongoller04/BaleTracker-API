package com.simongoller.baleTrackerAPI.service

import com.simongoller.baleTrackerAPI.model.user.UserDTO
import com.simongoller.baleTrackerAPI.repositroy.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service


@Service
class UserService(
    @Autowired val userRepository: UserRepository
) {
    fun getUser(): ResponseEntity<UserDTO> {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        val user = userRepository.findByUsername(auth.name)
        val userDTO = user?.let {
            user.id?.let { it1 -> UserDTO(it1, user.email, it.username) } }
        return ResponseEntity.ok(userDTO)
    }
}