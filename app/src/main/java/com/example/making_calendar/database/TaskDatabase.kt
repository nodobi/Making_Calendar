package com.example.making_calendar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [Task::class])
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao() : TaskDao

    companion object {
        private var instance: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase? {
            if(instance == null) {
                synchronized(TaskDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskDatabase::class.java,
                        "task-database"
                    ).build()
                }
            }
            return instance
        }
    }
}