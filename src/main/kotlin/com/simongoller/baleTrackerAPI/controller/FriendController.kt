package com.simongoller.baleTrackerAPI.controller

import com.simongoller.baleTrackerAPI.service.FriendService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/friend")
@CrossOrigin("*")
class FriendController(
    private val friendService: FriendService
) {
    @GetMapping("/url")
    fun createFriendRequestUrl(): ResponseEntity<String?> {
        return friendService.createFriendRequestUrl()
    }
}