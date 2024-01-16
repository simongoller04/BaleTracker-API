package com.simongoller.baleTrackerAPI.model.bale

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

data class Bale(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String?,
    val crop: Crop,
    val baleType: BaleType,
    // stores the user id of the creator
    val createdBy: String,
    // stores the user id of the collector
    var collectedBy: String?,
    val creationTime: String,
    var collectionTime: String?,
    val longitude: Double,
    val latitude: Double
)