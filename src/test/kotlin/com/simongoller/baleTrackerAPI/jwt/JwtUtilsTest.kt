package com.simongoller.baleTrackerAPI.jwt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.test.util.ReflectionTestUtils
import java.util.*

@ExtendWith(MockitoExtension::class)
class JwtUtilsTest {

    private var jwtSecret: String = "12345678901234567891234567891234567890123456789123456789"
    private var jwtExpirationMs: Long = 3000

    @Test
    fun `should generate valid JWT token`() {
        val jwtUtils = JwtUtils(jwtSecret, jwtExpirationMs)
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret)
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs)

        val authentication = createMockAuthentication()

        val token = jwtUtils.generateToken(authentication)
        val username = jwtUtils.getUsernameFromJWT(token)
        val isValid = jwtUtils.validateToken(token)

        assertEquals(authentication.name, username)
        assertTrue(isValid)
    }

    @Test
    fun `should return false for expired JWT token`() {
        val jwtUtils = JwtUtils(jwtSecret, jwtExpirationMs)
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret)
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", -1L) // Expired token

        val authentication = createMockAuthentication()

        val token = jwtUtils.generateToken(authentication)
        val isValid = jwtUtils.validateToken(token)

        assertFalse(isValid)
    }

    @Test
    fun `should return false for invalid JWT token`() {
        val jwtUtils = JwtUtils(jwtSecret, jwtExpirationMs)
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret)
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs)

        val token = "invalid_token"

        val isValid = jwtUtils.validateToken(token)

        assertFalse(isValid)
    }

    private fun createMockAuthentication(): Authentication {
        return object : Authentication {
            override fun getAuthorities(): Collection<GrantedAuthority> = emptySet()

            override fun setAuthenticated(isAuthenticated: Boolean) {}

            override fun getName(): String = "testuser"

            override fun getCredentials(): Any = Any()

            override fun getPrincipal(): Any = Any()

            override fun isAuthenticated(): Boolean = true

            override fun getDetails(): Any = Any()
        }
    }
}
