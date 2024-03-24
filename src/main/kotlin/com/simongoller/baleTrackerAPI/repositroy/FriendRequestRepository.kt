package com.simongoller.baleTrackerAPI.repositroy

import com.simongoller.baleTrackerAPI.model.friend.FriendRequest
import org.springframework.data.mongodb.repository.MongoRepository

interface FriendRequestRepository: MongoRepository<FriendRequest, String> {
    fun findFriendRequestById(id: String): FriendRequest?
}
