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

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<UserDTO?> {
        return userService.getUser(id)
    }

    @PostMapping("/multiple")
    fun getUsers(@RequestBody users: List<String>): ResponseEntity<List<UserDTO>> {
        return userService.getUsers(users)
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
        return userService.getProfilePicture(id)
        //TODO is returning the image of the currently authenticated user not with the given id
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