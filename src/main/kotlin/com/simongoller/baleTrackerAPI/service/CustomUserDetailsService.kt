package com.simongoller.baleTrackerAPI.service

import com.simongoller.baleTrackerAPI.model.user.UserDetailsImpl
import com.simongoller.baleTrackerAPI.repositroy.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CustomUserDetailsService(
    @Autowired private val userRepository: UserRepository
): UserDetailsService {
    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User Not Found with username: $username")
        return UserDetailsImpl(user)
    }
}