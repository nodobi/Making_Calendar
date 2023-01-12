package com.example.making_calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.making_calendar.adapter.viewholder.RecyclerDialogViewHolder
import com.example.making_calendar.data.CalendarData
import com.example.making_calendar.databinding.ItemRecyclerDialogBinding
import kotlinx.coroutines.*
import java.time.LocalDate

class RecyclerDialogAdapter(
    private val targetDate: LocalDate
) :
    RecyclerView.Adapter<RecyclerDialogViewHolder>() {

    private lateinit var todoDatas : List<String>
    private lateinit var recyclerDialogInterface: RecyclerDialogInterface
    
    interface RecyclerDialogInterface {
        fun onItemClick(targetDate: LocalDate, todo: String)
    }

    fun registerEvent(recyclerDialogInterface: RecyclerDialogInterface) {
        this.recyclerDialogInterface = recyclerDialogInterface
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerDialogViewHolder {
        val recyclerDialogViewHolder =
            RecyclerDialogViewHolder(ItemRecyclerDialogBinding.inflate(LayoutInflater.from(parent.context)))

        return recyclerDialogViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerDialogViewHolder, position: Int) {
        holder.binding.itemRecyclerDialogTextView.text = todoDatas[position]

        holder.binding.itemRecyclerDialogContainer.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                CalendarData.deleteTask(targetDate, holder.binding.itemRecyclerDialogTextView.text.toString())
                refreshDataByDate(targetDate)
                // TODO("CalendarAdapter한테도 dataSet이 바뀌었다는걸 알려야함")

            }
        }
    }

    override fun getItemCount(): Int {
        return todoDatas.size
    }

    fun refreshDataByDate(targetDate : LocalDate) {
        CoroutineScope(Dispatchers.IO).launch {
            todoDatas = CalendarData.loadTodosByDate(targetDate)!!
            CoroutineScope(Dispatchers.Main).launch {
                notifyDataSetChanged()
            }
        }
    }
}