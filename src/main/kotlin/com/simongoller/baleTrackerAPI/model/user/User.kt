package com.simongoller.baleTrackerAPI.model.user

import org.springframework.data.annotation.Id
import jakarta.persistence.*

// user class used to internally store the users and their information
data class User(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String?,
    val email: String,
    val username: String,
    val password: String,
    val creationTime: String,
    var lastEditingTime: String?,
    var lastLoginTime: String?,
    var profileImage: ByteArray?
)
