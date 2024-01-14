package com.simongoller.baleTrackerAPI.controller

import com.simongoller.baleTrackerAPI.model.response.UserDeletionResponse
import com.simongoller.baleTrackerAPI.model.user.User
import com.simongoller.baleTrackerAPI.model.user.UserDTO
import com.simongoller.baleTrackerAPI.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

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

    @DeleteMapping("/delete")
    fun deleteUser(): ResponseEntity<UserDeletionResponse> {
        return userService.deleteUser()
    }

    @PostMapping("/media/pic")
    fun updateProfilePicture(@RequestParam("image") image: MultipartFile): ResponseEntity<*> {
        return userService.updateProfilePicture(image)
    }

    @GetMapping("/media/{id}/pic")
    fun getProfilePicture(@PathVariable id: String): ResponseEntity<ByteArray?> {
        return userService.getProfilePicture()
    }

    @DeleteMapping("/media/pic")
    fun deleteProfilePicture(): ResponseEntity<*> {
        return userService.deleteProfilePicture()
    }

    // just for testing to get all users, remove afterwards
    @GetMapping("/all")
    fun getAllUsers(): ResponseEntity<List<User>> {
        return userService.getAllUsers()
    }

    // just for testing to delete all users, remove afterwards
    @DeleteMapping("/all")
    fun deleteAll(): ResponseEntity<*> {
        return userService.deleteAllUsers()
    }
}