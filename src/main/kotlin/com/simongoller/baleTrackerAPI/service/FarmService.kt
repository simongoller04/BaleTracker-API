package com.simongoller.baleTrackerAPI.service

import com.simongoller.baleTrackerAPI.model.bale.Bale
import com.simongoller.baleTrackerAPI.model.bale.Crop
import com.simongoller.baleTrackerAPI.model.farm.Farm
import com.simongoller.baleTrackerAPI.model.farm.FarmCreateDTO
import com.simongoller.baleTrackerAPI.model.farm.FarmDTO
import com.simongoller.baleTrackerAPI.repositroy.FarmRepository
import com.simongoller.baleTrackerAPI.utils.CurrentUserUtils
import com.simongoller.baleTrackerAPI.utils.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class FarmService(
    @Autowired private val farmRepository: FarmRepository,
    @Autowired private val currentUserUtils: CurrentUserUtils,
    @Autowired private val mt: MongoTemplate,
    private val timeUtils: TimeUtils = TimeUtils()
) {
    fun createFarm(farmCreateDTO: FarmCreateDTO): ResponseEntity<*> {
        val farm = currentUserUtils.getUserId()?.let {
            Farm(null,
                farmCreateDTO.name,
                farmCreateDTO.description,
                farmCreateDTO.coordinate,
                it,
                timeUtils.getCurrentDateTime(),
                farmCreateDTO.members,
                farmCreateDTO.image)
        }

        return if (farm != null) {
            farmRepository.save(farm)
            ResponseEntity.ok(null)
        } else {
            ResponseEntity.badRequest().body(null)
        }
    }

    fun getFarms(): ResponseEntity<List<FarmDTO>?> {
        val query = Query()

        currentUserUtils.getUserId()?.let {
            query.addCriteria(Criteria.where("members").`in`(it))
        } ?: return ResponseEntity.badRequest().body(null)

        val farms = mt.find(query, Farm::class.java)
        val dto = farms.map { it.toFarmDTO() }
        return ResponseEntity.ok(dto)
    }

    fun addMembers(id: String, members: List<String>): ResponseEntity<*> {
        farmRepository.findFarmById(id)?.let {
            currentUserUtils.getUserId()?.let { userId ->
                if (it.createdBy == userId) {
                    it.members?.addAll(members)
                    farmRepository.save(it)
                    return ResponseEntity.ok(null)
                }
                return ResponseEntity.badRequest().body(null)
            }
        } ?: return ResponseEntity.badRequest().body(null)
    }
}