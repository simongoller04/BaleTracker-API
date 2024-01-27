package com.simongoller.baleTrackerAPI.repositroy

import com.simongoller.baleTrackerAPI.model.bale.Bale
import com.simongoller.baleTrackerAPI.model.user.UserDTO
import org.springframework.data.mongodb.repository.MongoRepository

interface BaleRepository: MongoRepository<Bale, String> {
    fun findByCreatedBy(createdBy: UserDTO): List<Bale>?
}