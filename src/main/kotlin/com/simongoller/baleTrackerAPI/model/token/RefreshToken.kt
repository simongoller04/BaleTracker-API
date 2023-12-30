package com.simongoller.baleTrackerAPI.model.token

import com.simongoller.baleTrackerAPI.model.user.User
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import org.springframework.data.annotation.Id
import java.util.Date

data class RefreshToken(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    val user: User,
    val token: String,
    val expiryDate: Date
)
