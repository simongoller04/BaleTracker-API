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
        val bale = getCurrentUser()?.id?.let {
            Bale(null,
                baleCreateDTO.crop,
                baleCreateDTO.baleType,
                it,
                timeUtils.getCurrentDateTimeInFormat(),
                null,
                null,
                baleCreateDTO.longitude,
                baleCreateDTO.latitude,
                baleCreateDTO.farm
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
            bale.get().collectedBy = getCurrentUser()?.id
            bale.get().collectionTime = timeUtils.getCurrentDateTimeInFormat()
            baleRepository.save(bale.get())
            ResponseEntity.ok(bale.get())
        } else {
            ResponseEntity.badRequest().body(null)
        }
    }

    fun getCreatedBales(): ResponseEntity<List<Bale>?> {
        getCurrentUser()?.id?.let {
            val bales = baleRepository.findByCreatedBy(it)
            return ResponseEntity.ok(bales)
        }
        return ResponseEntity.badRequest().body(null)
    }

    fun getCollectedBales(): ResponseEntity<List<Bale>?> {
        getCurrentUser()?.id?.let {
            val bales = baleRepository.findByCollectedBy(it)
            return ResponseEntity.ok(bales)
        }
        return ResponseEntity.badRequest().body(null)
    }

    fun getAllBales(): ResponseEntity<List<Bale>?> {
        getCurrentUser()?.id?.let {
            val created = baleRepository.findByCreatedBy(it)
            val collected = baleRepository.findByCollectedBy(it)
            val combined = created?.let { created ->
                collected?.let { collected ->
                    created + collected
                } ?: created
            } ?: collected
            return ResponseEntity.ok(combined)
        }
        return ResponseEntity.badRequest().body(null)
    }

    fun getAllBalesFromFarm(farm: String): ResponseEntity<List<Bale>?> {
        val bales = baleRepository.findByFarm(farm)
        return if (bales != null) {
            ResponseEntity.ok(bales)
        } else {
            ResponseEntity.badRequest().body(null)
        }
    }

    // just for testing remove later
    fun getAll(): ResponseEntity<List<Bale>?>{
        return ResponseEntity.ok(baleRepository.findAll())
    }

    private fun getCurrentUser(): User? {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        return userRepository.findByUsername(auth.name)
    }
}