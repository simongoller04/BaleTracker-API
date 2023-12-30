package com.simongoller.baleTrackerAPI.service

import com.simongoller.baleTrackerAPI.constants.StringConstants
import com.simongoller.baleTrackerAPI.model.user.UserLoginDTO
import com.simongoller.baleTrackerAPI.model.user.UserRegisterDTO
import com.simongoller.baleTrackerAPI.repositroy.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired

@SpringBootTest
class AuthServiceTest {
    companion object {
        private const val EMAIL_1 = "user1@example.com"
        private const val USERNAME_1 = "username1"
        private const val PASSWORD_1 = "password1"

        private const val EMAIL_2 = "user2@example.com"
        private const val USERNAME_2 = "username2"
        private const val PASSWORD_2 = "password2"
    }

    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @BeforeEach
    fun setUp() {
        // Clear registered users before each test
        userRepository.deleteAll()
    }

    @Test
    fun `should register a new user`() {
        val userRegisterDTO = UserRegisterDTO(EMAIL_1, USERNAME_1, PASSWORD_1)

        val response: ResponseEntity<String> = authService.register(userRegisterDTO)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("${StringConstants.USER_CREATED} ${userRegisterDTO.username}", response.body)

        val user = userRepository.findByUsername(userRegisterDTO.username)
        assertNotNull(user)
        assertEquals(userRegisterDTO.email, user?.email)
        assertEquals(userRegisterDTO.username, user?.username)
        assertTrue(passwordEncoder.matches(userRegisterDTO.password, user?.password))
    }

    @Test
    fun `should prevent duplicate email registration`() {
        // Register a user initially
        val userRegisterDTO = UserRegisterDTO(EMAIL_1, USERNAME_1, PASSWORD_1)
        authService.register(userRegisterDTO)

        // Try to register the same email again
        val duplicateRegisterDTO = UserRegisterDTO(EMAIL_1, USERNAME_2, PASSWORD_2)
        val response: ResponseEntity<String> = authService.register(duplicateRegisterDTO)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(StringConstants.EMAIL_TAKEN, response.body)
    }

    @Test
    fun `should prevent duplicate username registration`() {
        // Register a user initially
        val userRegisterDTO = UserRegisterDTO(EMAIL_1, USERNAME_1, PASSWORD_1)
        authService.register(userRegisterDTO)

        // Try to register the same username again
        val duplicateRegisterDTO = UserRegisterDTO(EMAIL_2, USERNAME_1, PASSWORD_2)
        val response: ResponseEntity<String> = authService.register(duplicateRegisterDTO)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(StringConstants.USERNAME_TAKEN, response.body)
    }

    @Test
    fun `should log in a registered user`() {
        // Register a user
        val userRegisterDTO = UserRegisterDTO(EMAIL_1, USERNAME_1, PASSWORD_1)
        authService.register(userRegisterDTO)

        // Try to log in with the registered user's credentials
        val userLoginDTO = UserLoginDTO(USERNAME_1, PASSWORD_1)
        val response: ResponseEntity<String> = authService.login(userLoginDTO)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertTrue(response.body!!.isNotBlank())
    }

    @Test
    fun `should reject login with incorrect credentials`() {
        // Register a user
        val userRegisterDTO = UserRegisterDTO(EMAIL_1, USERNAME_1, PASSWORD_1)
        authService.register(userRegisterDTO)

        // Try to log in with incorrect credentials
        val userLoginDTO = UserLoginDTO(EMAIL_2, PASSWORD_2)
        val response: ResponseEntity<String> = authService.login(userLoginDTO)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(StringConstants.INVALID_CREDENTIALS, response.body)
    }
}