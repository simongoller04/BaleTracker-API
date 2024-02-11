package com.simongoller.baleTrackerAPI.model.bale

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import org.springframework.data.annotation.Id
import java.time.Instant

data class Bale(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String?,
    val crop: Crop,
    val baleType: BaleType,
    val createdBy: String,
    val creationTime: Instant,
    var collectedBy: String?,
    var collectionTime: Instant?,
    val coordinate: Coordinate,
    val farm: String?,
)

data class Coordinate(
    val latitude: Double,
    val longitude: Double
)