package com.simongoller.baleTrackerAPI.model.bale

data class BaleCreateDTO(
    val crop: Crop,
    val baleType: BaleType,
    val coordinate: Coordinate,
    val farm: String?
)