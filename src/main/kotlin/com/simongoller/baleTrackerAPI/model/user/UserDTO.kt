package com.simongoller.baleTrackerAPI.model.user

// dto to send the user information to the frontend without the password
data class UserDTO(
    val id: String,
    val email: String,
    val username: String,
    val creationTime: String,
    val lastEditingTime: String?,
    val lastLoginTime: String?
)
