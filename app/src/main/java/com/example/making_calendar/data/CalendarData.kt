package com.example.making_calendar.data

import android.content.Context
import android.util.Log
import com.example.making_calendar.data.database.Task
import com.example.making_calendar.data.database.TaskDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields

object CalendarData {
    var curDate: LocalDate = LocalDate.now()
    var curTime: LocalTime = LocalTime.now()
    private lateinit var db: TaskDatabase

    fun dateToString(date: LocalDate) = date.format(DateTimeFormatter.ISO_DATE)
    fun timeToString(time: LocalTime) = time.format(DateTimeFormatter.ISO_TIME)

    fun stringToDate(dateString: String) = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE)
    fun stringToTime(timeString: String) = LocalDate.parse(timeString, DateTimeFormatter.ISO_TIME)

    fun loadDatabase(context: Context) {
        db = TaskDatabase.getInstance(context)!!
    }
    fun moveMonth(month: Long) {
        if(month > 0)
            curDate = curDate.plusMonths(month)
        else
            curDate = curDate.minusMonths(month * -1)
    }

    fun loadTasksByDate(date: LocalDate): List<Task>? {
        var taskList: List<Task>? = db.taskDao().getTaskListByDate(date)
        return taskList
    }

    fun curMonthDateList(): List<LocalDate> {
        var curMonthDates : MutableList<LocalDate> = mutableListOf()
        val weekCnt: Int = curDate.plusMonths(1).withDayOfMonth(1).minusDays(1)
            .get(WeekFields.SUNDAY_START.weekOfYear()) - curDate.withDayOfMonth(1)
            .get(WeekFields.SUNDAY_START.weekOfYear()) + 1

        var firstDateDisplayedOn: LocalDate = curDate.withDayOfMonth(1)
        firstDateDisplayedOn =
            firstDateDisplayedOn.minusDays((firstDateDisplayedOn.get(WeekFields.SUNDAY_START.dayOfWeek()) - 1).toLong())

        for (i in 0 until (weekCnt * 7)) {
            curMonthDates.add(firstDateDisplayedOn.plusDays(i.toLong()))
        }

        return curMonthDates.toList()
    }

    fun deleteTask(task: Task) {
        db.taskDao().delete(task)
    }

}