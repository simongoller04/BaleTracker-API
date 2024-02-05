package com.simongoller.baleTrackerAPI.model.query

import com.simongoller.baleTrackerAPI.model.bale.BaleType
import com.simongoller.baleTrackerAPI.model.bale.Coordinate
import com.simongoller.baleTrackerAPI.model.bale.Crop
import com.simongoller.baleTrackerAPI.model.bale.TimeSpan

data class BaleQuery(
    val crop: Crop?,
    val baleType: BaleType?,
    val createdBy: String?,
    val creationTime: TimeSpan?,
    val collectedBy: String?,
    val collectionTime: TimeSpan?,
    val coordinate: Coordinate?,
    val farm: String?
)