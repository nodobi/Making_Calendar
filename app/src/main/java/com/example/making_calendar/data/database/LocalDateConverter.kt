package com.example.making_calendar.data.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateConverter {
    private val dateFormat = DateTimeFormatter.ISO_DATE

    @TypeConverter
    fun strToLocalDate(value: String): LocalDate = LocalDate.parse(value, dateFormat)
    @TypeConverter
    fun localDateToStr(date: LocalDate): String = date.format(dateFormat)
}