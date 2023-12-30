package com.simongoller.baleTrackerAPI.controller

import com.simongoller.baleTrackerAPI.model.user.UserDTO
import com.simongoller.baleTrackerAPI.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/me")
    fun getUser(): ResponseEntity<UserDTO> {
        return userService.getUser()
    }
}