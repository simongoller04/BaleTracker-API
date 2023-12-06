package com.simongoller.baleTrackerAPI.service

import com.simongoller.baleTrackerAPI.constants.StringConstants
import com.simongoller.baleTrackerAPI.jwt.JwtUtils
import com.simongoller.baleTrackerAPI.model.user.User
import com.simongoller.baleTrackerAPI.model.user.UserLoginDTO
import com.simongoller.baleTrackerAPI.model.user.UserRegisterDTO
import com.simongoller.baleTrackerAPI.repositroy.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class AuthService(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val encoder: PasswordEncoder,
    @Autowired private val authenticationManager: AuthenticationManager,
    @Autowired private val jwtUtils: JwtUtils
) {
    fun register(userRegisterDTO: UserRegisterDTO): ResponseEntity<String> {
        if (userRepository.existsByUsername(userRegisterDTO.username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringConstants.USERNAME_TAKEN)
        } else if (userRepository.existsByEmail(userRegisterDTO.email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringConstants.EMAIL_TAKEN)
        }

        val user = User(null,
            userRegisterDTO.email,
            userRegisterDTO.username,
            encoder.encode(userRegisterDTO.password))

        userRepository.save(user)
        return ResponseEntity.status(HttpStatus.OK).body("${StringConstants.USER_CREATED} ${user.username}")
    }

    fun login(userLoginDTO: UserLoginDTO): ResponseEntity<String> {
        return try {
            val authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(userLoginDTO.username, userLoginDTO.password)
            )

            SecurityContextHolder.getContext().authentication = authentication
            val jwt = jwtUtils.generateToken(authentication)

            ResponseEntity.status(HttpStatus.OK).body(jwt)
        } catch (ex: AuthenticationException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringConstants.INVALID_CREDENTIALS)
        }
    }
}