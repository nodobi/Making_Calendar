package com.example.making_calendar.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "task_table")
data class Task (
    var sDate: LocalDate,
    var eDate: LocalDate,
    var sTime: LocalTime?,
    var eTime: LocalTime?,
    var todo: String,
){
    @PrimaryKey(autoGenerate = true) var id: Long = 0;
}
