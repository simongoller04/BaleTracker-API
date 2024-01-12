package com.simongoller.baleTrackerAPI.service

import com.simongoller.baleTrackerAPI.model.response.UserDeletionResponse
import com.simongoller.baleTrackerAPI.model.user.User
import com.simongoller.baleTrackerAPI.model.user.UserDTO
import com.simongoller.baleTrackerAPI.repositroy.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class UserService(
    @Autowired private val userRepository: UserRepository
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

    fun updateProfilePicture(image: MultipartFile): ResponseEntity<*> {
        getCurrentUser()?.let {
            it.profileImage = image.bytes
            userRepository.save(it)
            return ResponseEntity.ok("updated")
        }
        return ResponseEntity.badRequest().body("not updated")
    }

    fun getProfilePicture(): ResponseEntity<ByteArray?> {
        getCurrentUser()?.let { user ->
            user.profileImage?.let { image ->
                return ResponseEntity.ok(image)
            }
        }
        return ResponseEntity.badRequest().body(null)
    }

    fun deleteProfilePicture(): ResponseEntity<*> {
        getCurrentUser()?.let {
            it.profileImage = null
            return ResponseEntity.ok(null)
        }
        return ResponseEntity.badRequest().body(null)
    }

    fun getAllUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userRepository.findAll())
    }

    private fun getCurrentUser(): User? {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        return userRepository.findByUsername(auth.name)
    }
}