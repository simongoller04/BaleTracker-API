package com.simongoller.baleTrackerAPI.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function
import javax.crypto.SecretKey


@Component
class JwtUtils(
    @Value("\${baleTrackerAPI.app.tokenSecret}")
    private val tokenSecret: String,

    @Value("\${baleTrackerAPI.app.accessTokenExpirationMs}")
    private val accessTokenExpiration: Long,

    @Value("\${baleTrackerAPI.app.refreshTokenExpirationMs}")
    private val refreshTokenExpiration: Long
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenSecret))
    private val logger: Logger = LoggerFactory.getLogger(JwtUtils::class.java)

    fun generateAccessToken(authentication: Authentication): String {
        val token = buildToken(authentication, accessTokenExpiration)
        logger.info("New access token: $token")
        return token
    }

    fun generateRefreshToken(authentication: Authentication): String {
        val token =  buildToken(authentication, accessTokenExpiration)
        logger.info("New refresh token: $token")
        return token
    }

    fun getUsernameFromToken(token: String): String {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload.subject
    }

    fun isTokenValid(token: String): Boolean {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parse(token)

            if (!isTokenExpired(token)) {
                return true
            }
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

    fun <T : Any> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    // MARK: Helper Functions

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts
            .parser()
            .verifyWith(key)
            .build()
            .parse(token)
            .payload as Claims
    }

    private fun buildToken(authentication: Authentication, expiration: Long): String {
        val expirationDate = Date(Date().time + expiration)
        return Jwts.builder()
            .subject(authentication.name)
            .issuedAt(Date())
            .expiration(expirationDate)
            .signWith(key)
            .compact()
    }
}