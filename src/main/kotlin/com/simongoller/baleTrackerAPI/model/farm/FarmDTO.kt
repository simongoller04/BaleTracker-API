package com.simongoller.baleTrackerAPI.model.farm

import com.simongoller.baleTrackerAPI.model.bale.Coordinate
import java.time.Instant

data class FarmDTO(
    val id: String,
    val name: String,
    val description: String?,
    val coordinate: Coordinate?,
    val createdBy: String,
    val creationTime: Instant,
    val members: MutableList<String>?,
) {
    fun toFarm(): Farm {
        return Farm(null, name, description, coordinate, createdBy, creationTime, members, null)
    }
}
