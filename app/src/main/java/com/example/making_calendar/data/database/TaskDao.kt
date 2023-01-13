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

    @Query("SELECT * FROM task_table WHERE date = :date")
    fun getTaskListByDate(date: String) : List<Task>?

    @Query("SELECT todo FROM task_table WHERE date =:date")
    fun getTodoListByDate(date: String) : List<String>?

    @Query("UPDATE task_table SET todo = :todo WHERE date = :date AND time = :time")
    fun updateTaskByDateTime(date: String, time: String, todo: String)

    @Query("UPDATE task_table SET todo = :todo WHERE date = :date AND todo = :todo")
    fun updateTaskByDate(date: String, todo: String)

    @Query("DELETE FROM task_table WHERE date =:date AND time = :time AND todo =:todo")
    fun deleteTaskByDateTimeWithTodo(date: String, time: String?, todo: String)

    @Query("DELETE FROM task_table WHERE date =:date AND todo =:todo")
    fun deleteTaskByDateWithTodo(date: String, todo: String)

}