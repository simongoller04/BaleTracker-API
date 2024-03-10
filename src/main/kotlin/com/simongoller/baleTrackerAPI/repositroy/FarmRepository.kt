package com.simongoller.baleTrackerAPI.repositroy

import com.simongoller.baleTrackerAPI.model.farm.Farm
import org.springframework.data.mongodb.repository.MongoRepository

interface FarmRepository: MongoRepository<Farm, String> {
    fun findFarmById(id: String): Farm?
    fun findFarmByName(name: String): Farm?
    fun findFarmByImageKey(imageKey: String): Farm?
}