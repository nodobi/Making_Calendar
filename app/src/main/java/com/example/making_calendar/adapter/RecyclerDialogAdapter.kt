package com.example.making_calendar.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.making_calendar.adapter.viewholder.RecyclerDialogViewHolder
import com.example.making_calendar.data.CalendarData
import com.example.making_calendar.databinding.ItemRecyclerDialogBinding
import com.example.making_calendar.dialog.ChoiceDialog
import kotlinx.coroutines.*
import java.time.LocalDate
import java.util.Calendar

class RecyclerDialogAdapter(
    private val fragmentManager: FragmentManager,
    private val targetDate: LocalDate
) :
    RecyclerView.Adapter<RecyclerDialogViewHolder>(), ChoiceDialog.ChoiceDialogInterface {

    private lateinit var todoDatas : List<String>
    private lateinit var recyclerDialogInterface: RecyclerDialogInterface

    init {
        refreshDataByDate(targetDate)
        notifyDataSetChanged()
    }

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
            val choiceDialog = ChoiceDialog(this, "${todoDatas[position]}", "수정", "삭제")
            choiceDialog.show(fragmentManager, "Choicefragment_show")
//            CoroutineScope(Dispatchers.Main).launch {
//                CoroutineScope(Dispatchers.IO).async {
//                    CalendarData.deleteTask(targetDate, todoDatas[position])
//                    todoDatas = CalendarData.loadTodosByDate(targetDate)!!
//                }.await()
//                notifyDataSetChanged()
//                recyclerDialogInterface.onItemClick(targetDate, holder.binding.itemRecyclerDialogTextView.text.toString())
//            }

        }
    }

    override fun getItemCount(): Int {
        return todoDatas.size
    }

    fun refreshDataByDate(targetDate : LocalDate) {
        CoroutineScope(Dispatchers.IO).launch {
            todoDatas = CalendarData.loadTodosByDate(targetDate)!!
        }
    }

    override fun onChoice1Click() {
        Log.d("hyeok", "Choice 1 Click")
    }

    override fun onChoice2Click() {
        Log.d("hyeok", "Choice 2 Click")

    }
}