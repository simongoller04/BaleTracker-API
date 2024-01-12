package com.simongoller.baleTrackerAPI.repositroy

import com.simongoller.baleTrackerAPI.model.token.RefreshToken
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository: MongoRepository<RefreshToken, String> {
    fun findByToken(token: String): RefreshToken?
}

