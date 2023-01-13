package com.example.making_calendar.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.making_calendar.adapter.viewholder.CalendarViewHolder
import com.example.making_calendar.data.CalendarData
import com.example.making_calendar.databinding.ItemTextBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.WeekFields

class CalendarAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    var calendarDate: LocalDateTime = LocalDateTime.now()
    private lateinit var data: List<LocalDate>

    private lateinit var calendarAdapterInterface: CalendarAdapterInterface

    init {
        refreshData()
    }

    interface CalendarAdapterInterface {
        fun onItemClick(holder: CalendarViewHolder)
        fun onItemLongClick(holder: CalendarViewHolder)
    }

    fun registerEvents(calendarAdapterInterface: CalendarAdapterInterface) {
        this.calendarAdapterInterface = calendarAdapterInterface
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val weekCnt: Int = calendarDate.plusMonths(1).withDayOfMonth(1).minusDays(1)
            .get(WeekFields.SUNDAY_START.weekOfYear()) - calendarDate.withDayOfMonth(1)
            .get(WeekFields.SUNDAY_START.weekOfYear()) + 1
        val height = parent.height / weekCnt
        val width = parent.width / 7

        Log.d("hyeok", "height: $height, width: $width")

        val myViewHolder =
            CalendarViewHolder(ItemTextBinding.inflate(LayoutInflater.from(parent.context)))

        myViewHolder.itemView.layoutParams = RecyclerView.LayoutParams(width, height)
        return myViewHolder
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val bind = holder.binding
        holder.localDate = data[position]
        holder.dateView.text = data[position].dayOfMonth.toString()
        var todoList: List<String>?
        CoroutineScope(Dispatchers.IO).launch {
            todoList = CalendarData.loadTodosByDate(data[position])
            CoroutineScope(Dispatchers.Main).launch {
                holder.onBind(data[position], todoList!!, context)
            }
        }

        bind.root.setOnClickListener {
            this.calendarAdapterInterface?.onItemClick(holder)
        }

        // 리사이클러 뷰 홀더에 클릭 이벤트 추가
        bind.root.setOnLongClickListener {
            this.calendarAdapterInterface?.onItemLongClick(holder)
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }


    // 이번달 날짜 계산
    fun refreshData() {
        data = CalendarData.curMonthDateList()
    }


}