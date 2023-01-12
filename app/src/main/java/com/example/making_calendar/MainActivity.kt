package com.example.making_calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.making_calendar.adapter.CalendarAdapter
import com.example.making_calendar.database.Task
import com.example.making_calendar.databinding.ActivityMainBinding
import com.example.making_calendar.dialog.TaskDialog
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var calendarAdapter: CalendarAdapter

    var taskDialog: TaskDialog? = null
    lateinit var dateYearTextView: TextView
    lateinit var dateMonthTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 소프트 키보드가 올라갔을 때 화면에 아무 적용도 하지 않음
        // SOFT_INPUT_ADJUST_PAN 은 액티비티를 위로 올리고 ADJUST_RESIZE는 액티비티 사이즈를 조절한다.
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        dateYearTextView = binding.dateYearText
        dateMonthTextView = binding.dateMonthText
        var task: Task = Task(LocalDate.now().toString(), LocalTime.now().toString(), "")
        Log.d("hyeok", "task.id = ${task.id}")
        makeCalendar()
        clickMoveEvent()
        dateYearTextView.text = "${LocalDate.now().year}"
        dateMonthTextView.text = "${LocalDate.now().month}"

    }

    fun clickMoveEvent() {
        binding.nextMonthImg.setOnClickListener {
            Log.d("hyeok", "Click NextMonthImgBtn")
            calendarAdapter.calendarDate = calendarAdapter.calendarDate.plusMonths(1)
            calendarAdapter.refreshData()
            calendarAdapter.notifyDataSetChanged()

            binding.dateYearText.text = "${calendarAdapter.calendarDate.year}"
            binding.dateMonthText.text =
                "${calendarAdapter.calendarDate.month.getDisplayName(TextStyle.SHORT, Locale.US)}"
        }

        binding.prevMonthImg.setOnClickListener {
            Log.d("hyeok", "Click PrevMonthImgBtn")
            calendarAdapter.calendarDate = calendarAdapter.calendarDate.minusMonths(1)
            calendarAdapter.refreshData()
            calendarAdapter.notifyDataSetChanged()

            binding.dateYearText.text = "${calendarAdapter.calendarDate.year}"
            binding.dateMonthText.text =
                "${calendarAdapter.calendarDate.month.getDisplayName(TextStyle.SHORT, Locale.US)}"
        }
    }

    fun makeCalendar() {
        val calendarRecycler = binding.recyclerCalendar

        val myGridLayout = GridLayoutManager(applicationContext, 7)
        myGridLayout.orientation = GridLayoutManager.VERTICAL

        val dataSet: MutableList<String> = mutableListOf()

        calendarRecycler.layoutManager = myGridLayout

        calendarAdapter = CalendarAdapter(supportFragmentManager, applicationContext)

        calendarRecycler.adapter = calendarAdapter

        var itemDecoration1 =
            DividerItemDecoration(applicationContext, DividerItemDecoration.HORIZONTAL)
        var itemDecoration2 =
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)

        calendarRecycler.addItemDecoration(itemDecoration1)
        calendarRecycler.addItemDecoration(itemDecoration2)
    }
}