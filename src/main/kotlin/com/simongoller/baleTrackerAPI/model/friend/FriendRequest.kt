package com.simongoller.baleTrackerAPI.model.friend

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.criteria.CriteriaBuilder.In
import org.springframework.data.annotation.Id
import java.time.Instant
import java.util.UUID

data class FriendRequest(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: String?, // Assuming the ID is of type Long
    val createdBy: String,
    val createdByUsername: String,
    val creationTime: Instant,
    var url: String
) {
    constructor(createdBy: String, createdByUsername: String, creationTime: Instant) : this(null, createdBy, createdByUsername, creationTime,"")

    fun createUrl(): String {
        this.url = "baleTrackerApp//$id/friend=$createdBy%$createdByUsername"
        return url
    }
}
