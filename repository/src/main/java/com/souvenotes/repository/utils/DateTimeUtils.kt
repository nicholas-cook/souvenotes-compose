package com.souvenotes.repository.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateTimeUtils {

    private const val SAME_YEAR_FORMAT = "EEE, MMM d 'at' h:mm a"
    private const val PREV_YEAR_FORMAT = "EEE, MMM d, yyyy 'at' h:mm a"

    fun getDateTimeText(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val now = LocalDateTime.now()
        return if (now.year > dateTime.year) {
            dateTime.format(DateTimeFormatter.ofPattern(PREV_YEAR_FORMAT))
        } else {
            dateTime.format(DateTimeFormatter.ofPattern(SAME_YEAR_FORMAT))
        }
    }
}