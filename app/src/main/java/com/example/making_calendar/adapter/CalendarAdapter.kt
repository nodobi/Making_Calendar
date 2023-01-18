package com.example.making_calendar.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.making_calendar.R
import com.example.making_calendar.adapter.viewholder.CalendarViewHolder
import com.example.making_calendar.data.CalendarData
import com.example.making_calendar.data.database.Task
import com.example.making_calendar.databinding.ItemTextBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.WeekFields

class CalendarAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    private var dateData: List<LocalDate>? = null
    private var calendarAdapterInterface: CalendarAdapterInterface? = null
    private var width = 0
    private var height = 0

    init {
        refreshData()
        notifyDataSetChanged()
    }

    interface CalendarAdapterInterface {
        fun onItemClick(holder: CalendarViewHolder)
        fun onItemLongClick(holder: CalendarViewHolder)
    }

    fun registerEvents(calendarAdapterInterface: CalendarAdapterInterface) {
        this.calendarAdapterInterface = calendarAdapterInterface
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        Log.d("hyeok", "height: $height, width: $width")

        val myViewHolder =
            CalendarViewHolder(ItemTextBinding.inflate(LayoutInflater.from(parent.context)))

        myViewHolder.itemView.layoutParams = RecyclerView.LayoutParams(width, height)
        return myViewHolder
    }



    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {

        val bind = holder.binding
        holder.localDate = dateData?.get(holder.adapterPosition)
        holder.dateView.text = holder.localDate?.dayOfMonth.toString()
        var taskList: List<Task>? = null
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                taskList = CalendarData.loadTasksByDate(holder.localDate!!)
            }.await()
            holder.onBind(taskList, context)
        }



        bind.root.setOnClickListener {
            Log.d("hyeok", "onClickListener")
//            this.calendarAdapterInterface?.onItemClick(holder)
        }

        // 리사이클러 뷰 홀더에 클릭 이벤트 추가
        bind.root.setOnLongClickListener {
            Log.d("hyeok", "onLongClickListener")
//            this.calendarAdapterInterface?.onItemLongClick(holder)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return dateData!!.size
    }

    // 이번달 날짜 계산
    fun refreshData() {
        dateData = CalendarData.curMonthDateList()
    }

    fun resizeView(width: Int, height: Int) {
        val weekCnt: Int = CalendarData.curDate.plusMonths(1).withDayOfMonth(1).minusDays(1)
            .get(WeekFields.SUNDAY_START.weekOfYear()) - CalendarData.curDate.withDayOfMonth(1)
            .get(WeekFields.SUNDAY_START.weekOfYear()) + 1
        this.width = width / 7
        this.height = height / weekCnt
    }


}