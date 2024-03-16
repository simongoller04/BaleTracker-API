package com.simongoller.baleTrackerAPI.service

import com.simongoller.baleTrackerAPI.model.response.UserDeletionResponse
import com.simongoller.baleTrackerAPI.model.user.User
import com.simongoller.baleTrackerAPI.model.user.UserDTO
import com.simongoller.baleTrackerAPI.repositroy.UserRepository
import com.simongoller.baleTrackerAPI.utils.CurrentUserUtils
import com.simongoller.baleTrackerAPI.utils.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID
import javax.crypto.SecretKey

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val currentUserUtils: CurrentUserUtils,
    private val timeUtils: TimeUtils = TimeUtils()
) {
    fun getUser(): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(currentUserUtils.getUser()?.toUserDto())
    }

    fun getUser(id: String): ResponseEntity<UserDTO?> {
        val user = userRepository.findById(id)
        return if (user.isEmpty) {
            return ResponseEntity.badRequest().body(null)
        } else {
            ResponseEntity.ok(user.orElse(null).toUserDto())
        }
    }

    fun getUsers(userIds: List<String>): ResponseEntity<List<UserDTO>> {
        val users: MutableList<UserDTO> = arrayListOf()
        for (userId in userIds) {
            userRepository.findById(userId).orElse(null).toUserDto()?.let { users.add(it) }
        }
        return ResponseEntity.ok(users)
    }

    fun deleteUser(): ResponseEntity<UserDeletionResponse> {
        currentUserUtils.getUserId()?.let {
            userRepository.deleteById(it)
            return ResponseEntity.ok(UserDeletionResponse.DELETED)
        }
        return ResponseEntity.badRequest().body(UserDeletionResponse.NOT_DELETED)
    }

    fun updateProfilePicture(image: MultipartFile): ResponseEntity<*> {
        currentUserUtils.getUser()?.let {
            it.imageKey = UUID.randomUUID().toString()
            it.profileImage = image.bytes
            it.lastEditingTime = timeUtils.getCurrentDateTimeInFormat()
            userRepository.save(it)
            return ResponseEntity.ok(null)
        }
        return ResponseEntity.badRequest().body(null)
    }

    fun getProfilePicture(imageKey: String): ResponseEntity<ByteArray?> {
        userRepository.findByImageKey(imageKey)?.profileImage?.let {
            return ResponseEntity.ok(it)
        }
        return ResponseEntity.badRequest().body(null)
    }

    fun deleteProfilePicture(): ResponseEntity<*> {
        currentUserUtils.getUser()?.let {
            it.imageKey = null
            it.profileImage = null
            it.lastEditingTime = timeUtils.getCurrentDateTimeInFormat()
            userRepository.save(it)
            return ResponseEntity.ok(null)
        }
        return ResponseEntity.badRequest().body(null)
    }

    fun getAllUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userRepository.findAll())
    }

    fun deleteAllUsers(): ResponseEntity<*> {
        userRepository.deleteAll()
        return ResponseEntity.ok(null)
    }
}