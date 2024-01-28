package com.simongoller.baleTrackerAPI.controller

import com.simongoller.baleTrackerAPI.model.bale.BaleCreateDTO
import com.simongoller.baleTrackerAPI.service.BaleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bale")
@CrossOrigin("*")
class BaleController(
    private val baleService: BaleService
) {
    @PostMapping("/create")
    fun register(@RequestBody baleCreateDTO: BaleCreateDTO): ResponseEntity<*> {
        return baleService.createBale(baleCreateDTO)
    }

    @PutMapping("/collect/{id}")
    fun collectBale(@PathVariable id: String): ResponseEntity<*> {
        return baleService.collectBale(id)
    }

    // add query to get all bales where creationId is currently authenticated user
    @GetMapping("/getAll")
    fun getAllBales(): ResponseEntity<*> {
        return baleService.getBales()
    }
}