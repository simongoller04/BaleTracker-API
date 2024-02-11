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
import java.lang.Error
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
                timeUtils.getCurrentDateTime(),
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
        val bale = baleRepository.findById(id)
        return if (bale != null) {
            bale.get().collectedBy = currentUserUtils.getUserId()
            bale.get().collectionTime = timeUtils.getCurrentDateTime()
            baleRepository.save(bale.get())
            ResponseEntity.ok(bale.get())
        } else {
            ResponseEntity.badRequest().body(null)
        }
    }

    fun deleteBale(id: String): ResponseEntity<*> {
        return if (!baleRepository.findById(id).isEmpty) {
            baleRepository.deleteById(id)
            ResponseEntity.ok(null)
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
        val query = Query()

        if (baleQuery.crop != Crop.ALL) {
            query.addCriteria(Criteria.where("crop").`is`(baleQuery.crop))
        }
        if (baleQuery.baleType != BaleType.ALL) {
            query.addCriteria(Criteria.where("baleType").`is`(baleQuery.baleType))
        }
        if (baleQuery.createdBy != null) {
            query.addCriteria(Criteria.where("createdBy").`is`(baleQuery.createdBy))
        }
        if (baleQuery.creationTimeSpan != null) {
            val startTime = Instant.parse(baleQuery.creationTimeSpan.start)
            val endTime = Instant.parse(baleQuery.creationTimeSpan.end)
            query.addCriteria(Criteria.where("creationTime").gte(startTime).lt(endTime))
        }
        if (baleQuery.collectedBy != null) {
            query.addCriteria(Criteria.where("collectedBy").`is`(baleQuery.collectedBy))
        }
        if (baleQuery.collectionTimeSpan != null) {
            val startTime = Instant.parse(baleQuery.collectionTimeSpan.start)
            val endTime = Instant.parse(baleQuery.collectionTimeSpan.end)
            query.addCriteria(Criteria.where("collectionTime").gte(startTime).lt(endTime))
        }
        if (baleQuery.coordinate != null) {
            query.addCriteria(Criteria.where("coordinate").`is`(baleQuery.coordinate))
        }
        if (baleQuery.farm != null) {
            query.addCriteria(Criteria.where("farm").`is`(baleQuery.farm))
        }

        val bales = mt.find(query, Bale::class.java)

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