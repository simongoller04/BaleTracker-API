package com.simongoller.baleTrackerAPI.controller

import com.simongoller.baleTrackerAPI.model.farm.FarmCreateDTO
import com.simongoller.baleTrackerAPI.model.farm.FarmDTO
import com.simongoller.baleTrackerAPI.service.FarmService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/farm")
@CrossOrigin("*")
class FarmController(
    private val farmService: FarmService
) {
    @PostMapping("/create")
    fun createFarm(@RequestBody farmCreateDTO: FarmCreateDTO): ResponseEntity<*> {
        return farmService.createFarm(farmCreateDTO)
    }

    // the owner of the farm can add members
    @PostMapping("/{id}/members")
    fun addMembers(@PathVariable id: String, @RequestBody members: List<String>): ResponseEntity<*> {
        return farmService.addMembers(id, members)
    }

    // return all farms where the currently authenticated user is a member
    @GetMapping()
    fun getFarms(): ResponseEntity<List<FarmDTO>?> {
        return farmService.getFarms()
    }
}