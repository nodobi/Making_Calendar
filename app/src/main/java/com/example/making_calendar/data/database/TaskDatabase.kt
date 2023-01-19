package com.example.making_calendar.data.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec

@Database(
    version = 2,
    entities = [Task::class]
)
@TypeConverters(
    LocalDateConverter::class,
    LocalTimeConverter::class
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

//    @DeleteTable(tableName = "task_table")
//    class TempMigration : AutoMigrationSpec {}

    companion object {
        private var instance: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase? {
            if (instance == null) {
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