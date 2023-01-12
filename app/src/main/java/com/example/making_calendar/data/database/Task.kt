package com.example.making_calendar.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "task_table")
data class Task (
    var date: String,
    var time: String?,
    var todo: String,
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0;
}
