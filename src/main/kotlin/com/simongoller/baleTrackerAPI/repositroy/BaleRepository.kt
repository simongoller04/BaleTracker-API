package com.simongoller.baleTrackerAPI.repositroy

import com.simongoller.baleTrackerAPI.model.bale.Bale
import com.simongoller.baleTrackerAPI.model.bale.BaleType
import com.simongoller.baleTrackerAPI.model.bale.Crop
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.EntityNotFoundException
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository


interface BaleRepository: MongoRepository<Bale, String> {
    fun findByCreatedBy(createdBy: String): MutableList<Bale>?
    fun findByCollectedBy(collectedBy: String): MutableList<Bale>?
    fun findByFarm(farm: String): MutableList<Bale>?
    fun findByCrop(crop: Crop): List<Bale>?
    fun findByBaleType(baleType: BaleType): List<Bale>?

    @Query("{crop: ?0, baleType: ?1, createdBy: ?2}")
    fun findBalesByCriteria(
        crop: Crop?,
        baleType: BaleType?,
        createdBy: String?
    ): MutableList<Bale>?
}

//@Repository
//class CustomBaleRepository(
//    @PersistenceContext private val entityManager: EntityManager
//) {
//    fun query() {
//        entityManager.entityManagerFactory.criteriaBuilder
//        val criteriaBuilder = entityManager.criteriaBuilder
//        val query: CriteriaQuery<Bale> = criteriaBuilder.createQuery(Bale::class.java)
//        val bale: Root<Bale> = query.from(Bale::class.java)
//    }
//}