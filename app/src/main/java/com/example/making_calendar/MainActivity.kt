package com.example.making_calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.CalendarView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.making_calendar.adapter.CalendarAdapter
import com.example.making_calendar.adapter.RecyclerDialogAdapter
import com.example.making_calendar.adapter.viewholder.CalendarViewHolder
import com.example.making_calendar.data.CalendarData
import com.example.making_calendar.data.database.Task
import com.example.making_calendar.data.database.TaskDatabase
import com.example.making_calendar.databinding.ActivityMainBinding
import com.example.making_calendar.dialog.EditDialog
import com.example.making_calendar.dialog.RecyclerDialog
import com.example.making_calendar.dialog.TaskDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var calendarAdapter: CalendarAdapter
    lateinit var recyclerDialogAdapter: RecyclerDialogAdapter

    lateinit var db : TaskDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 소프트 키보드가 올라갔을 때 화면에 아무 적용도 하지 않음
        // SOFT_INPUT_ADJUST_PAN 은 액티비티를 위로 올리고 ADJUST_RESIZE는 액티비티 사이즈를 조절한다.
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        db = TaskDatabase.getInstance(this)!!

        CalendarData.loadDatabase(applicationContext)
        initCalendarAdapter()
        initCalendarEvents()
        clickMoveEvent()

    }

    fun clickMoveEvent() {
        binding.nextMonthImg.setOnClickListener {
            Log.d("hyeok", "Click NextMonthImgBtn")
            CalendarData.moveMonth(1)
            calendarAdapter.refreshData()
            calendarAdapter.notifyDataSetChanged()

            binding.dateYearText.text = CalendarData.curDate.year.toString()
            binding.dateMonthText.text = CalendarData.curDate.monthValue.toString()
        }

        binding.prevMonthImg.setOnClickListener {
            Log.d("hyeok", "Click PrevMonthImgBtn")
            CalendarData.moveMonth(-1)
            calendarAdapter.refreshData()
            calendarAdapter.notifyDataSetChanged()

            binding.dateYearText.text = CalendarData.curDate.year.toString()
            binding.dateMonthText.text = CalendarData.curDate.monthValue.toString()
        }
    }

    fun initCalendarAdapter() {
        // 캘린더 어뎁터
        val calendarLayout = GridLayoutManager(applicationContext, 7)
        calendarLayout.orientation = GridLayoutManager.VERTICAL
        calendarAdapter = CalendarAdapter(supportFragmentManager, applicationContext)
        var itemDecoration1 =
            DividerItemDecoration(applicationContext, DividerItemDecoration.HORIZONTAL)
        var itemDecoration2 =
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)

        binding.recyclerCalendar.apply {
            adapter = calendarAdapter
            layoutManager = calendarLayout
            addItemDecoration(itemDecoration1)
            addItemDecoration(itemDecoration2)
        }

        // 다이얼로그 어뎁터

    }

    fun initCalendarEvents() {
        calendarAdapter.registerEvents(object: CalendarAdapter.CalendarAdapterInterface {
            override fun onItemClick(holder: CalendarViewHolder) {
                val taskListDialog = RecyclerDialog(holder.localDate!!)
                taskListDialog.show(supportFragmentManager, "TaskListDialog_Show")
            }

            override fun onItemLongClick(holder: CalendarViewHolder) {
                val addTaskDialog = EditDialog(object : EditDialog.EditDialogInterface {
                    override fun onEditDialogButtonClick(text: String) {
                        holder.addTaskOnItemContainer(text)
                        CoroutineScope(Dispatchers.IO).launch {
                            val newTask = Task(
                                CalendarData.dateToString(holder.localDate!!),
                                null,
                                text
                            )
                            Log.d("hyeok", "insert!")
                            db.taskDao().insert(newTask)
                        }
                        calendarAdapter.notifyDataSetChanged()
                    }
                })
                addTaskDialog.show(supportFragmentManager, "AddTaskDialog_Show")
            }

        })
    }

}