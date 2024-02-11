package com.simongoller.baleTrackerAPI.controller

import com.simongoller.baleTrackerAPI.model.bale.Bale
import com.simongoller.baleTrackerAPI.model.bale.BaleCreateDTO
import com.simongoller.baleTrackerAPI.model.query.BaleQuery
import com.simongoller.baleTrackerAPI.service.BaleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bale")
@CrossOrigin("*")
class BaleController(
    private val baleService: BaleService
) {
    // create a new bale
    @PostMapping("/create")
    fun register(@RequestBody baleCreateDTO: BaleCreateDTO): ResponseEntity<*> {
        return baleService.createBale(baleCreateDTO)
    }

    // collect bale with the given id
    @PutMapping("/collect/{id}")
    fun collectBale(@PathVariable id: String): ResponseEntity<*> {
        return baleService.collectBale(id)
    }

    // delete the bale with the given id
    @DeleteMapping("/delete/{id}")
    fun deleteBale(@PathVariable id: String): ResponseEntity<*> {
        return baleService.deleteBale(id)
    }

    // get all bales created by the currently authenticated user
    @GetMapping("/get/created")
    fun getCreatedBales(): ResponseEntity<List<Bale>?> {
        return baleService.getCreatedBales()
    }

    // get all bales collected by the currently authenticated user
    @GetMapping("/get/collected")
    fun getCollectedBales(): ResponseEntity<List<Bale>?> {
        return baleService.getCollectedBales()
    }

    // get all bales created or collected by the currently authenticated user
    @GetMapping("/get/all")
    fun getAllBales(): ResponseEntity<List<Bale>?> {
        return baleService.getAllBales()
    }

    // get all bales where farm equal the given id
    @GetMapping("/get/all/farm/{id}")
    fun getAllBalesFromFarm(@PathVariable id: String): ResponseEntity<List<Bale>?> {
        return baleService.getAllBalesFromFarm(id)
    }

    @PostMapping("/query")
    fun queryBales(@RequestBody query: BaleQuery): ResponseEntity<MutableList<Bale>?> {
        return baleService.queryBales(query)
    }

    // just for testing remove later
    @GetMapping("/get/all/testing")
    fun getAll(): ResponseEntity<List<Bale>?> {
        return baleService.getAll()
    }

    @DeleteMapping("/get/all/testing")
    fun deleteAll(): ResponseEntity<*> {
        return baleService.deleteAll()
    }
}