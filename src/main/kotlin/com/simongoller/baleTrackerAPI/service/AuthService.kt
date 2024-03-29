package com.simongoller.baleTrackerAPI.service

import com.simongoller.baleTrackerAPI.jwt.JwtUtils
import com.simongoller.baleTrackerAPI.model.response.ErrorResponse
import com.simongoller.baleTrackerAPI.model.token.RefreshToken
import com.simongoller.baleTrackerAPI.model.token.RefreshTokenDTO
import com.simongoller.baleTrackerAPI.model.token.TokenDTO
import com.simongoller.baleTrackerAPI.model.user.UserLoginDTO
import com.simongoller.baleTrackerAPI.model.user.UserRegisterDTO
import com.simongoller.baleTrackerAPI.repositroy.RefreshTokenRepository
import com.simongoller.baleTrackerAPI.repositroy.UserRepository
import com.simongoller.baleTrackerAPI.utils.TimeUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
import java.util.Date


@Service
class AuthService(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val refreshTokenRepository: RefreshTokenRepository,
    @Autowired private val encoder: PasswordEncoder,
    @Autowired private val authenticationManager: AuthenticationManager,
    @Autowired private val jwtUtils: JwtUtils,
    private val timeUtils: TimeUtils = TimeUtils()
) {
    private val logger: Logger = LoggerFactory.getLogger(AuthService::class.java)

    fun register(userRegisterDTO: UserRegisterDTO): ResponseEntity<ErrorResponse> {
        if (userRepository.existsByUsername(userRegisterDTO.username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.USERNAME_TAKEN)
        } else if (userRepository.existsByEmail(userRegisterDTO.email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.EMAIL_TAKEN)
        }

        userRepository.save(userRegisterDTO.toUser())
        return ResponseEntity.status(HttpStatus.OK).body(ErrorResponse.USER_CREATED)
    }

    fun login(userLoginDTO: UserLoginDTO): ResponseEntity<*> {
        try {
            if (!userRepository.existsByUsername(userLoginDTO.username)) {
                return ResponseEntity.badRequest().body(ErrorResponse.INVALID_USERNAME)
            }
            val authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(userLoginDTO.username, userLoginDTO.password)
            )

            SecurityContextHolder.getContext().authentication = authentication

            val accessToken = jwtUtils.generateAccessToken(authentication)
            val refreshToken = jwtUtils.generateRefreshToken(authentication)

            val user = userRepository.findByUsername(authentication.name)
            if (user != null) {
                user.lastLoginTime = timeUtils.getCurrentDateTimeInFormat()
                userRepository.save(user)
                val refreshToken = RefreshToken(null, user, refreshToken, jwtUtils.extractRefreshExpiration(refreshToken))
                refreshTokenRepository.save(refreshToken)
            }
            return ResponseEntity.status(HttpStatus.OK).body(TokenDTO(accessToken, refreshToken))
        } catch (ex: AuthenticationException) {
            return ResponseEntity.badRequest().body(ErrorResponse.INVALID_PASSWORD)
        }
    }

    fun refresh(refreshToken: RefreshTokenDTO): ResponseEntity<TokenDTO?> {
        refreshTokenRepository.findByToken(refreshToken.refreshToken)?.let { token ->
            if (token.expiryDate > Date()) {
                val newAccessToken = jwtUtils.generateAccessToken(token.user.username)
                return ResponseEntity.ok(TokenDTO(newAccessToken, token.token))
            }
        }
        return ResponseEntity.badRequest().body(null)
    }
}