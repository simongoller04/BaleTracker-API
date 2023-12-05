package com.simongoller.baleTrackerAPI.jwt

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys

@Component
class JwtUtils(
    @Value("\${baleTrackerAPI.app.jwtSecret}")
    private val jwtSecret: String,

    @Value("\${baleTrackerAPI.app.jwtExpirationMs}")
    private val jwtExpirationMs: Long
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))
    private val logger: Logger = LoggerFactory.getLogger(JwtUtils::class.java)

    fun generateToken(authentication: Authentication): String {
        val expirationDate = Date(Date().time + jwtExpirationMs)
        val token: String = Jwts.builder()
            .subject(authentication.name)
            .issuedAt(Date())
            .expiration(expirationDate)
            .signWith(key)
            .compact()
        logger.info("New token: $token")
        return token
    }

    fun getUsernameFromJWT(token: String?): String {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload.subject
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parse(token)
            return true
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: {}", e.message)
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: {}", e.message)
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
        }
        return false
    }
}