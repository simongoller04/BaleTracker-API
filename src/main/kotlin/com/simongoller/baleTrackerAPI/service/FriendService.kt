package com.simongoller.baleTrackerAPI.service

import com.simongoller.baleTrackerAPI.model.friend.FriendRequest
import com.simongoller.baleTrackerAPI.repositroy.FriendRequestRepository
import com.simongoller.baleTrackerAPI.utils.CurrentUserUtils
import com.simongoller.baleTrackerAPI.utils.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.insert
import org.springframework.data.mongodb.core.query.UpdateDefinition
import org.springframework.data.mongodb.core.update
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class FriendService(
    @Autowired private val friendRequestRepository: FriendRequestRepository,
    @Autowired private val currentUserUtils: CurrentUserUtils,
    @Autowired private val mt: MongoTemplate,
    private val timeUtils: TimeUtils = TimeUtils(),
) {
    fun createFriendRequestUrl(): ResponseEntity<String?> {
        // TODO: not optimal
        currentUserUtils.getUser()?.let {
            val request = FriendRequest(currentUserUtils.getUserId(), currentUserUtils.getUsername(), timeUtils.getCurrentDateTime())
            val friendRequest = mt.insert<FriendRequest>(request)
            val url = friendRequest.createUrl()
            mt.insert<FriendRequest>(request)
            return ResponseEntity.ok(url)
        }
        return ResponseEntity.badRequest().body(null)
    }
}