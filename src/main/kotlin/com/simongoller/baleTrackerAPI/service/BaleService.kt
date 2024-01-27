package com.simongoller.baleTrackerAPI.service

import com.simongoller.baleTrackerAPI.model.bale.Bale
import com.simongoller.baleTrackerAPI.model.bale.BaleCreateDTO
import com.simongoller.baleTrackerAPI.model.user.User
import com.simongoller.baleTrackerAPI.repositroy.BaleRepository
import com.simongoller.baleTrackerAPI.repositroy.UserRepository
import com.simongoller.baleTrackerAPI.utils.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class BaleService(
    @Autowired private val baleRepository: BaleRepository,
    @Autowired private val userRepository: UserRepository,
    private val timeUtils: TimeUtils = TimeUtils()
) {
    fun createBale(baleCreateDTO: BaleCreateDTO): ResponseEntity<*> {
        val bale = getCurrentUser()?.toUserDto()?.let { user ->
            Bale(null,
                baleCreateDTO.crop,
                baleCreateDTO.baleType,
                user,
                null,
                timeUtils.getCurrentDateTimeInFormat(),
                null,
                baleCreateDTO.longitude,
                baleCreateDTO.latitude
            )
        }
        return if (bale != null) {
            baleRepository.save(bale)
            ResponseEntity.ok(bale)
        } else {
            ResponseEntity.badRequest().body(null)
        }
    }

    fun collectBale(id: String): ResponseEntity<*> {
        var bale = baleRepository.findById(id)
        return if (bale != null) {
            bale.get().collectedBy = getCurrentUser()?.toUserDto()
            bale.get().collectionTime = timeUtils.getCurrentDateTimeInFormat()
            baleRepository.save(bale.get())
            ResponseEntity.ok(bale.get())
        } else {
            ResponseEntity.badRequest().body(null)
        }
    }

    fun getBales(): ResponseEntity<List<Bale>?> {
        getCurrentUser()?.toUserDto()?.let {
            val bales = baleRepository.findByCreatedBy(it)
            return ResponseEntity.ok(bales)
        } 
        return ResponseEntity.badRequest().body(null)
    }

    private fun getCurrentUser(): User? {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        return userRepository.findByUsername(auth.name)
    }
}