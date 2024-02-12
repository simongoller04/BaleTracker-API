package com.simongoller.baleTrackerAPI.model.farm

import com.simongoller.baleTrackerAPI.model.bale.Coordinate

data class FarmCreateDTO(
    val name: String,
    val description: String?,
    val coordinate: Coordinate?,
    val members: MutableList<String>?,
    var image: ByteArray?
)
