package com.simongoller.baleTrackerAPI.controller

import com.simongoller.baleTrackerAPI.model.user.User
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
@CrossOrigin("*")
class TestController {

    @GetMapping("/hello")
    fun helloWorld(): String {
        return "Hello, World!"
    }

    @GetMapping("/helloAuth")
    fun helloWorldAuth(): String {
        return "Hello, World! (Authenticated)"
    }
}