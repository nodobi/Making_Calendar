package com.example.making_calendar.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.making_calendar.database.TaskDatabase
import com.example.making_calendar.databinding.ItemRecyclerDialogBinding
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RecyclerDialogAdapter(
    private val context : Context,
    private val targetDate: LocalDate
) :
    RecyclerView.Adapter<RecyclerDialogAdapter.RecyclerDialogViewHolder>() {

    private var testData : MutableList<String> = mutableListOf()
    private val db : TaskDatabase? = TaskDatabase.getInstance(context)

    class RecyclerDialogViewHolder(val binding: ItemRecyclerDialogBinding) :
        ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerDialogViewHolder {
        val recyclerDialogViewHolder =
            RecyclerDialogViewHolder(ItemRecyclerDialogBinding.inflate(LayoutInflater.from(parent.context)))
        val recycler = recyclerDialogViewHolder.binding.itemRecyclerDialogContainer
        return recyclerDialogViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerDialogViewHolder, position: Int) {
        holder.binding.itemRecyclerDialogTextView.text = testData[position]

        holder.binding.itemRecyclerDialogContainer.setOnClickListener {
            val db: TaskDatabase? = TaskDatabase.getInstance(context)

            CoroutineScope(Dispatchers.IO).launch {
                db!!.taskDao().deleteTaskByDateWithTodo(targetDate.format(DateTimeFormatter.ISO_DATE), holder.binding.itemRecyclerDialogTextView.text.toString())
                Log.d("hyeok", "You delete ${testData[position].toString()} Todo: ${holder.binding.itemRecyclerDialogTextView.text.toString()}")
                refreshDataByDate(targetDate)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return testData.size
    }

    fun refreshDataByDate(targetDate : LocalDate) {
        val db = TaskDatabase.getInstance(context)
        CoroutineScope(Dispatchers.IO).launch {
            testData = async(Dispatchers.IO) {
                db!!.taskDao().getTodoListByDate(
                    targetDate.format(
                        DateTimeFormatter.ISO_DATE
                    )
                )?.toMutableList()
            }.await()!!
            notifyDataSetChanged()
        }
    }
}