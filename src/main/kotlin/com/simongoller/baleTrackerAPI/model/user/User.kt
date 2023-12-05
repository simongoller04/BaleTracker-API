package com.simongoller.baleTrackerAPI.model.user

import org.springframework.data.annotation.Id
import jakarta.persistence.*

data class User(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    val email: String,
    val username: String,
    val password: String
)
