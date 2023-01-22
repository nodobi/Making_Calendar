package com.example.making_calendar

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.making_calendar.adapter.CalendarAdapter
import com.example.making_calendar.adapter.RecyclerDialogAdapter
import com.example.making_calendar.adapter.viewholder.CalendarViewHolder
import com.example.making_calendar.data.CalendarData
import com.example.making_calendar.data.database.Task
import com.example.making_calendar.data.database.TaskDatabase
import com.example.making_calendar.databinding.ActivityMainBinding
import com.example.making_calendar.dialog.EditDialog
import com.example.making_calendar.dialog.RecyclerDialog
import com.example.making_calendar.listener.CalendarItemInteractionListener
import com.example.making_calendar.listener.DragSelectionItemTouchListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var calendarAdapter: CalendarAdapter

    lateinit var db: TaskDatabase

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

            binding.dateYearText.text = CalendarData.curDate.year.toString()
            binding.dateMonthText.text = CalendarData.curDate.monthValue.toString()
            calendarAdapter.notifyDataSetChanged()
        }

        binding.prevMonthImg.setOnClickListener {
            Log.d("hyeok", "Click PrevMonthImgBtn")
            CalendarData.moveMonth(-1)
            calendarAdapter.refreshData()

            binding.dateYearText.text = CalendarData.curDate.year.toString()
            binding.dateMonthText.text = CalendarData.curDate.monthValue.toString()
            calendarAdapter.notifyDataSetChanged()
        }
    }

    fun initCalendarAdapter() {
        // 캘린더 어뎁터
        val calendarLayout = GridLayoutManager(applicationContext, 7)
        calendarLayout.orientation = GridLayoutManager.VERTICAL
        calendarAdapter = CalendarAdapter(applicationContext)
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

    }

    fun initCalendarEvents() {

        binding.recyclerCalendar.addOnItemTouchListener(
            DragSelectionItemTouchListener(
                this,
                object : CalendarItemInteractionListener {
                    override fun onLongItemClicked(
                        recyclerView: RecyclerView?,
                        mViewHolderTouched: RecyclerView.ViewHolder?,
                        position: Int
                    ) {
                    }

                    override fun onItemClicked(
                        recyclerView: RecyclerView?,
                        mViewHolderTouched: RecyclerView.ViewHolder?,
                        position: Int
                    ) {
                        val taskListDialog = RecyclerDialog(
                            (mViewHolderTouched as CalendarViewHolder).localDate!!,
                            object : RecyclerDialogAdapter.RecyclerDialogInterface {
                                override fun onItemClick() {
                                    calendarAdapter.notifyDataSetChanged()
                                }
                            })
                        taskListDialog.show(supportFragmentManager, "TaskListDialog_Show")
                    }

                    override fun onMultipleViewHoldersSelected(
                        rv: RecyclerView?,
                        selection: List<RecyclerView.ViewHolder?>?
                    ) {
                        if (selection != null) {
                            (rv?.adapter as CalendarAdapter).selectItems(selection as List<CalendarViewHolder?>)
                        }
                    }

                    override fun onLongItemReleased(
                        rv: RecyclerView?,
                        selection: List<RecyclerView.ViewHolder?>?
                    ) {
                        (rv?.adapter as CalendarAdapter).clearSelectedItems()

                        val addTaskDialog = EditDialog(null, "등록하기")
                        addTaskDialog.registerEvent(object : EditDialog.EditDialogInterface {
                            override fun onEditDialogButtonClick(text: String) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    val newTask = Task(
                                        (selection!!.first() as CalendarViewHolder).localDate!!,
                                        (selection.last() as CalendarViewHolder).localDate!!,
                                        null,
                                        null,
                                        text
                                    )
                                    CoroutineScope(Dispatchers.IO).async {
                                        db.taskDao().insert(newTask)
                                    }.await()
                                    calendarAdapter.notifyDataSetChanged()
                                    addTaskDialog.dismiss()
                                }
                            }
                        })
                        addTaskDialog.show(supportFragmentManager, "AddTaskDialog_Show")


                    }

                })
        )



    }
}