package com.simongoller.baleTrackerAPI.service

import com.simongoller.baleTrackerAPI.model.farm.Farm
import com.simongoller.baleTrackerAPI.model.farm.FarmCreateDTO
import com.simongoller.baleTrackerAPI.model.farm.FarmDTO
import com.simongoller.baleTrackerAPI.model.farm.FarmUpdateDTO
import com.simongoller.baleTrackerAPI.repositroy.FarmRepository
import com.simongoller.baleTrackerAPI.utils.CurrentUserUtils
import com.simongoller.baleTrackerAPI.utils.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.UpdateDefinition
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FarmService(
    @Autowired private val farmRepository: FarmRepository,
    @Autowired private val currentUserUtils: CurrentUserUtils,
    @Autowired private val mt: MongoTemplate,
    private val timeUtils: TimeUtils = TimeUtils()
) {
    fun createFarm(farmCreateDTO: FarmCreateDTO): ResponseEntity<FarmDTO?> {
        val farm = currentUserUtils.getUserId()?.let {
            val members = mutableListOf<String>()
            members.add(it)
            farmCreateDTO.members?.let { it1 -> members.addAll(it1) }
            Farm(null,
                farmCreateDTO.name,
                farmCreateDTO.description,
                farmCreateDTO.coordinate,
                it,
                timeUtils.getCurrentDateTime(),
                members,
                null)
        }

        return if (farm != null) {
            val newFarm = mt.insert<Farm>(farm)
            ResponseEntity.ok(newFarm.toFarmDTO())
        } else {
            ResponseEntity.badRequest().body(null)
        }
    }

    fun updateFarm(id: String, farmUpdateDTO: FarmUpdateDTO): ResponseEntity<FarmDTO?> {
        // TODO: not ideal with updating of the members yet, revisit later
        farmRepository.findFarmById(id)?.let { farm ->
            farm.update(farmUpdateDTO)
            farmRepository.save(farm)
            return ResponseEntity.ok(farm.toFarmDTO())
        }
        return ResponseEntity.badRequest().body(null)
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

    fun updateFarmPicture(id: String, image: MultipartFile): ResponseEntity<*> {
        farmRepository.findFarmById(id)?.let {
            it.image = image.bytes
            farmRepository.save(it)
            return ResponseEntity.ok(null)
        }
        return ResponseEntity.badRequest().body(null)
    }

    fun getFarmPicture(id: String): ResponseEntity<ByteArray?> {
        farmRepository.findFarmById(id)?.image?.let {
            return ResponseEntity.ok(it)
        }
        return ResponseEntity.badRequest().body(null)
    }

    fun deleteFarmPicture(id: String): ResponseEntity<*> {
        farmRepository.findFarmById(id)?.let {
            it.image = null
            farmRepository.save(it)
            return ResponseEntity.ok(null)
        } ?: return ResponseEntity.badRequest().body(null)
    }

    fun getFarms(): ResponseEntity<List<FarmDTO>?> {
        val query = Query()

        currentUserUtils.getUserId()?.let {
            val criteria = Criteria()
            criteria.orOperator(Criteria.where("createdBy").`is`(it), Criteria.where("members").`in`(it))
            query.addCriteria(criteria)
        } ?: return ResponseEntity.badRequest().body(null)

        val farms = mt.find(query, Farm::class.java)
        val dto = farms.map { it.toFarmDTO() }
        return ResponseEntity.ok(dto)
    }

    // just for testing

    fun deleteAllFarms(): ResponseEntity<String> {
        farmRepository.deleteAll()
        return ResponseEntity.ok("Deleted all Farms")
    }
}