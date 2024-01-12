package com.simongoller.baleTrackerAPI.repositroy

import com.simongoller.baleTrackerAPI.model.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
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

@Repository
class UserRepositoryImpl(
    @Autowired private val mongoTemplate: MongoTemplate
) {
    fun saveUser(user: User): User {
        mongoTemplate.save<Any>(user)
        return user
    }

    fun findUserById(id: String?): User? {
        val query = Query(Criteria.where("_id").`is`(id))
        return mongoTemplate.findOne(query, User::class.java)
    }

    fun findAllUsers(): List<User> {
        return mongoTemplate.findAll(User::class.java)
    }

    // Custom query example
    fun findUsersByAgeGreaterThan(age: Int): List<User> {
        val query = Query(Criteria.where("age").gt(age))
        return mongoTemplate.find(query, User::class.java)
    }

    fun deleteUser(user: User) {
        mongoTemplate.remove(user)
    }
}