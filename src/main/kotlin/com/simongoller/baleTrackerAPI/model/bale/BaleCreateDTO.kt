package com.simongoller.baleTrackerAPI.model.bale

data class BaleCreateDTO(
    val crop: Crop,
    val baleType: BaleType,
    val longitude: Double,
    val latitude: Double,
    val farm: String?
)
