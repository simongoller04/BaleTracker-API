package com.simongoller.baleTrackerAPI.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimeUtils {
    fun getCurrentDateTimeInFormat(): String {
        val currentLocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return formatter.format(currentLocalDateTime)
    }

    fun getCurrentDateTime(): Instant {
        return Instant.now()
    }
}