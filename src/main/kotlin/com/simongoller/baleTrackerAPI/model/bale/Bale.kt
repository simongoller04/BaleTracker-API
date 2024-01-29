package com.simongoller.baleTrackerAPI.model.bale

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import org.springframework.data.annotation.Id

data class Bale(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String?,
    val crop: Crop,
    val baleType: BaleType,
    val createdBy: String,
    val creationTime: String,
    var collectedBy: String?,
    var collectionTime: String?,
    val longitude: Double,
    val latitude: Double,
    val farm: String?
)