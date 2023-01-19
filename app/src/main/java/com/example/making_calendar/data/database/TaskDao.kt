package com.example.making_calendar.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(task: Task)
    @Delete
    fun delete(task: Task)
    @Update
    fun update(task: Task)

    @Query("SELECT * FROM task_table WHERE date(:date) BETWEEN sDate AND eDate")
    fun getTaskListByDate(date: LocalDate) : List<Task>?
}