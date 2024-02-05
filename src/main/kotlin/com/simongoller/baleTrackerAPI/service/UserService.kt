package com.simongoller.baleTrackerAPI.service

import com.simongoller.baleTrackerAPI.model.response.UserDeletionResponse
import com.simongoller.baleTrackerAPI.model.user.User
import com.simongoller.baleTrackerAPI.model.user.UserDTO
import com.simongoller.baleTrackerAPI.repositroy.UserRepository
import com.simongoller.baleTrackerAPI.utils.CurrentUserUtils
import com.simongoller.baleTrackerAPI.utils.TimeUtils
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
    @Autowired private val userRepository: UserRepository,
    @Autowired private val currentUserUtils: CurrentUserUtils,
    private val timeUtils: TimeUtils = TimeUtils()
) {
    private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)

    fun getUser(): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(currentUserUtils.getUser()?.toUserDto())
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
            it.profileImage = image.bytes
            it.lastEditingTime = timeUtils.getCurrentDateTimeInFormat()
            userRepository.save(it)
            return ResponseEntity.ok(null)
        }
        return ResponseEntity.badRequest().body(null)
    }

    fun getProfilePicture(): ResponseEntity<ByteArray?> {
        currentUserUtils.getUser()?.let {
            it.profileImage?.let { image ->
                return ResponseEntity.ok(image)
            }
        }
        return ResponseEntity.badRequest().body(null)
    }

    fun deleteProfilePicture(): ResponseEntity<*> {
        currentUserUtils.getUser()?.let {
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