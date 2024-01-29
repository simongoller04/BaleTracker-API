package com.simongoller.baleTrackerAPI.repositroy

import com.simongoller.baleTrackerAPI.model.bale.Bale
import com.simongoller.baleTrackerAPI.model.user.UserDTO
import org.springframework.data.mongodb.repository.MongoRepository

interface BaleRepository: MongoRepository<Bale, String> {
    fun findByCreatedBy(createdBy: String): MutableList<Bale>?
    fun findByCollectedBy(collectedBy: String): MutableList<Bale>?
    fun findByFarm(farm: String): MutableList<Bale>?
}