package com.simongoller.baleTrackerAPI.repositroy

import com.simongoller.baleTrackerAPI.model.user.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String>, CrudRepository<User, String> {
    fun findByEmail(email: String): User?
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}