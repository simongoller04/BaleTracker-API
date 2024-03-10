package com.simongoller.baleTrackerAPI.model.farm

import com.simongoller.baleTrackerAPI.model.bale.Coordinate
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import org.springframework.data.annotation.Id
import java.time.Instant

data class Farm(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: String?,
    var name: String,
    var description: String?,
    var coordinate: Coordinate?,
    val createdBy: String,
    val creationTime: Instant,
    var members: MutableList<String>,
    var imageKey: String?,
    var image: ByteArray?
) {
    fun toFarmDTO(): FarmDTO {
        return FarmDTO(id!!, name, description, coordinate, createdBy, creationTime, members, imageKey)
    }

    fun update(farmUpdateDTO: FarmUpdateDTO) {
        name = farmUpdateDTO.name
        description = farmUpdateDTO.description
        coordinate = farmUpdateDTO.coordinate
        members = farmUpdateDTO.members
    }
}
