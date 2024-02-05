package com.simongoller.baleTrackerAPI.service

import com.simongoller.baleTrackerAPI.model.bale.Bale
import com.simongoller.baleTrackerAPI.model.bale.BaleCreateDTO
import com.simongoller.baleTrackerAPI.model.bale.BaleType
import com.simongoller.baleTrackerAPI.model.bale.Crop
import com.simongoller.baleTrackerAPI.model.query.BaleQuery
import com.simongoller.baleTrackerAPI.repositroy.BaleRepository
import com.simongoller.baleTrackerAPI.utils.CurrentUserUtils
import com.simongoller.baleTrackerAPI.utils.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class BaleService(
    @Autowired private val baleRepository: BaleRepository,
    @Autowired private val mt: MongoTemplate,
    @Autowired private val currentUserUtils: CurrentUserUtils,
    private val timeUtils: TimeUtils = TimeUtils()
) {
    fun createBale(baleCreateDTO: BaleCreateDTO): ResponseEntity<*> {
        val bale = currentUserUtils.getUserId()?.let {
            Bale(null,
                baleCreateDTO.crop,
                baleCreateDTO.baleType,
                it,
                timeUtils.getCurrentDateTimeInFormat(),
                null,
                null,
                baleCreateDTO.coordinate,
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
            bale.get().collectedBy = currentUserUtils.getUserId()
            bale.get().collectionTime = timeUtils.getCurrentDateTimeInFormat()
            baleRepository.save(bale.get())
            ResponseEntity.ok(bale.get())
        } else {
            ResponseEntity.badRequest().body(null)
        }
    }

    fun getCreatedBales(): ResponseEntity<List<Bale>?> {
        currentUserUtils.getUserId()?.let {
            val bales = baleRepository.findByCreatedBy(it)
            return ResponseEntity.ok(bales)
        }
        return ResponseEntity.badRequest().body(null)
    }

    fun getCollectedBales(): ResponseEntity<List<Bale>?> {
        currentUserUtils.getUserId()?.let {
            val bales = baleRepository.findByCollectedBy(it)
            return ResponseEntity.ok(bales)
        }
        return ResponseEntity.badRequest().body(null)
    }

    fun getAllBales(): ResponseEntity<List<Bale>?> {
        currentUserUtils.getUserId()?.let {
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

    fun queryBales(baleQuery: BaleQuery): ResponseEntity<MutableList<Bale>?> {
        var query = Query()
        if (baleQuery.crop != Crop.ALL) {
            query.addCriteria(Criteria.where("crop").`is`(baleQuery.crop))
        }
        if (baleQuery.baleType != BaleType.ALL) {
            query.addCriteria(Criteria.where("baleType").`is`(baleQuery.baleType))
        }
        if (baleQuery.createdBy != null) {
            query.addCriteria(Criteria.where("createdBy").`is`(baleQuery.createdBy))
        }
        if (baleQuery.creationTime != null) {
            query.addCriteria(Criteria.where("creationTime").gte(baleQuery.creationTime.start).lt(baleQuery.creationTime.end))
        }
        if (baleQuery.collectedBy != null) {
            query.addCriteria(Criteria.where("collectedBy").`is`(baleQuery.collectedBy))
        }
        if (baleQuery.collectionTime != null) {
            query.addCriteria(Criteria.where("collectionTime").gte(baleQuery.collectionTime!!.start).lt(baleQuery.collectionTime!!.end))
        }
        if (baleQuery.coordinate != null) {
            query.addCriteria(Criteria.where("coordinate").`is`(baleQuery.coordinate))
        }
        if (baleQuery.farm != null) {
            query.addCriteria(Criteria.where("farm").`is`(baleQuery.farm))
        }

        var bales = mt.find(query, Bale::class.java)

        return ResponseEntity.ok(bales)
    }

    // just for testing remove later
    fun getAll(): ResponseEntity<List<Bale>?>{
        return ResponseEntity.ok(baleRepository.findAll())
    }

    fun deleteAll(): ResponseEntity<*> {
        baleRepository.deleteAll()
        return ResponseEntity.ok(null)
    }
}