package com.example.making_calendar.data.database

import androidx.room.TypeConverter
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LocalTimeConverter {
    private val timeFormat = DateTimeFormatter.ISO_TIME

    @TypeConverter
    fun strToLocalTime(value: String?): LocalTime? = if(value == null) null else LocalTime.parse(value, timeFormat)
    @TypeConverter
    fun localTimeToStr(time: LocalTime?): String? = time?.format(timeFormat)
}