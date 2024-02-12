package com.simongoller.baleTrackerAPI.model.farm

import com.simongoller.baleTrackerAPI.model.bale.Coordinate
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import org.springframework.data.annotation.Id
import java.time.Instant

data class Farm(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String?,
    val name: String,
    val description: String?,
    val coordinate: Coordinate?,
    val createdBy: String,
    val creationTime: Instant,
    var members: MutableList<String>?,
    var image: ByteArray?
) {
    fun toFarmDTO(): FarmDTO {
        return FarmDTO(id!!, name, description, coordinate, createdBy, creationTime, members)
    }
}
