package com.telesoftas.newsapp.utlis

import android.util.Log
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DateFormatterImpl @Inject constructor() : DateFormatter {
    override val dateFormat = "yyyy-MMMM-dd HH:mm"
    private val formatter = DateTimeFormatter.ofPattern(dateFormat)

    override fun formatDate(date: String): String? {
        return try {
            ZonedDateTime.parse(date).format(formatter)
        } catch (e: Exception) {
            Log.e("DateFormatter", "Failed to format date: $date", e)
            null
        }
    }
}