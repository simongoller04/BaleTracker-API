package com.simongoller.baleTrackerAPI.controller

import com.simongoller.baleTrackerAPI.constants.RegistrationState
import com.simongoller.baleTrackerAPI.model.token.TokenDTO
import com.simongoller.baleTrackerAPI.model.user.UserLoginDTO
import com.simongoller.baleTrackerAPI.model.user.UserRegisterDTO
import com.simongoller.baleTrackerAPI.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(@RequestBody userRegisterDTO: UserRegisterDTO): ResponseEntity<RegistrationState> {
       return authService.register(userRegisterDTO)
    }

    @RequestMapping("/login")
    fun login(@RequestBody userLoginDTO: UserLoginDTO): ResponseEntity<TokenDTO> {
        return authService.login(userLoginDTO)
    }

    @RequestMapping("/refreshToken")
    fun refreshToken(@RequestBody refreshToken: String): ResponseEntity<TokenDTO> {
        return authService.refresh(refreshToken)
    }
}