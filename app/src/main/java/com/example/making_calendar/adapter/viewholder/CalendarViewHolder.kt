package com.example.making_calendar.adapter.viewholder

import android.content.Context
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.making_calendar.data.CalendarData
import com.example.making_calendar.databinding.ItemTextBinding
import java.time.LocalDate

class CalendarViewHolder(val binding: ItemTextBinding) : RecyclerView.ViewHolder(binding.root) {
    val dateView: TextView = binding.textView
    val taskTextViewContainer: LinearLayout = binding.itemTaskContainer
    var localDate: LocalDate? = null
    var context: Context? = null

    fun addTaskOnItemContainer(taskText: String) {
        val textView = TextView(context)
        textView.apply {
            text = taskText
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(10, 0, 0, 0)
            }
        }
        taskTextViewContainer.addView(textView)
    }

    fun onBind(localDate: LocalDate, todoList : List<String>, context: Context) {
        this.localDate = localDate
        this.context = context
        dateView.text = localDate.dayOfMonth.toString()
        Log.d("hyeok", "curDate: ${CalendarData.dateToString(localDate)}, todo: ${todoList.toString()}")
        taskTextViewContainer.removeAllViews()
        for(todo in todoList) {
            addTaskOnItemContainer(todo)
        }
    }
}