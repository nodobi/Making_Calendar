package com.example.making_calendar.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.making_calendar.adapter.viewholder.CalendarViewHolder
import com.example.making_calendar.data.CalendarData
import com.example.making_calendar.data.database.Task
import com.example.making_calendar.databinding.ItemTextBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.WeekFields

class CalendarAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<CalendarViewHolder>() {
    private var dateData: List<LocalDate>? = null
    private var previousSelectedItems: List<CalendarViewHolder?>? = null
    private var recyclerWidth = 0
    private var recyclerHeight = 0
    private var width = 0
    private var height = 0

    init {
        refreshData()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        if(width == 0 && height == 0) {
            recyclerWidth = parent.width
            recyclerHeight = parent.height
            resizeView()
        }
        val myViewHolder =
            CalendarViewHolder(ItemTextBinding.inflate(LayoutInflater.from(parent.context)))

        return myViewHolder
    }


    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.itemView.layoutParams = RecyclerView.LayoutParams(width, height)
        holder.localDate = dateData?.get(holder.adapterPosition)
        holder.dateView.text = holder.localDate?.dayOfMonth.toString()
        var taskList: List<Task>? = null
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                taskList = CalendarData.loadTasksByDate(holder.localDate!!)
            }.await()
            holder.onBind(taskList, context)
        }


    }

    override fun getItemCount(): Int {
        return dateData!!.size
    }

    // 이번달 날짜 계산
    fun refreshData() {
        dateData = CalendarData.curMonthDateList()
        resizeView()
    }

    fun resizeView() {
        Log.d("hyeok", "ResizeView| w: $width, h: $height")
        val weekCnt: Int = CalendarData.curDate.plusMonths(1).withDayOfMonth(1).minusDays(1)
            .get(WeekFields.SUNDAY_START.weekOfYear()) - CalendarData.curDate.withDayOfMonth(1)
            .get(WeekFields.SUNDAY_START.weekOfYear()) + 1
        this.width = recyclerWidth / 7
        this.height = recyclerHeight / weekCnt
    }

    fun clearSelectedItems() {
        if(previousSelectedItems != null) {
            for (item in previousSelectedItems!!) {
                item?.itemView?.isSelected = false
            }
        }
    }

    fun selectItems(selection: List<CalendarViewHolder?>) {
        clearSelectedItems()
        for (item in selection) {
            item?.itemView?.isSelected = true
        }
        previousSelectedItems = selection
    }
}