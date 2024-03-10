package com.simongoller.baleTrackerAPI.controller

import com.simongoller.baleTrackerAPI.model.farm.FarmCreateDTO
import com.simongoller.baleTrackerAPI.model.farm.FarmDTO
import com.simongoller.baleTrackerAPI.model.farm.FarmUpdateDTO
import com.simongoller.baleTrackerAPI.service.FarmService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/farm")
@CrossOrigin("*")
class FarmController(
    private val farmService: FarmService
) {
    @PostMapping("/create")
    fun createFarm(@RequestBody farmCreateDTO: FarmCreateDTO): ResponseEntity<FarmDTO?> {
        return farmService.createFarm(farmCreateDTO)
    }

    @PostMapping("/{id}/update")
    fun updateFarm(@PathVariable id: String, @RequestBody farmUpdateDTO: FarmUpdateDTO): ResponseEntity<FarmDTO?> {
        return farmService.updateFarm(id, farmUpdateDTO)
    }

    // the owner of the farm can add members
    @PostMapping("/{id}/members")
    fun addMembers(@PathVariable id: String, @RequestBody members: List<String>): ResponseEntity<*> {
        return farmService.addMembers(id, members)
    }

    @PostMapping("/media/{id}/pic")
    fun updateFarmPicture(@PathVariable id: String, @RequestParam("image") image: MultipartFile): ResponseEntity<*> {
        return farmService.updateFarmPicture(id, image)
    }

    @GetMapping("/media/{imageKey}/pic")
    fun getFarmPicture(@PathVariable imageKey: String): ResponseEntity<ByteArray?> {
        return farmService.getFarmPicture(imageKey)
    }

    @DeleteMapping("/media/{id}/pic")
    fun deleteFarmPicture(@PathVariable id: String): ResponseEntity<*> {
        return farmService.deleteFarmPicture(id)
    }

    // return all farms where the currently authenticated user is a member or the owner
    @GetMapping()
    fun getFarms(): ResponseEntity<List<FarmDTO>?> {
        return farmService.getFarms()
    }

    @DeleteMapping("deleteAll")
    fun deleteAllFarms(): ResponseEntity<String> {
        return farmService.deleteAllFarms()
    }
}