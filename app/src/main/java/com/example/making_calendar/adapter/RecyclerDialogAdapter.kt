package com.example.making_calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.making_calendar.adapter.viewholder.RecyclerDialogViewHolder
import com.example.making_calendar.data.CalendarData
import com.example.making_calendar.databinding.ItemRecyclerDialogBinding
import com.example.making_calendar.dialog.ChoiceDialog
import com.example.making_calendar.dialog.EditDialog
import kotlinx.coroutines.*
import java.time.LocalDate

class RecyclerDialogAdapter(
    private val fragmentManager: FragmentManager,
    private val targetDate: LocalDate
) :
    RecyclerView.Adapter<RecyclerDialogViewHolder>() {

    private lateinit var todoDatas: List<String>
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
            val choiceDialog = ChoiceDialog(holder.binding.itemRecyclerDialogTextView.text.toString(), "수정", "삭제")
            choiceDialog.registerEvents(object: ChoiceDialog.ChoiceDialogInterface {
                override fun onChoice1Click() {
                    val modifyTaskDialog = EditDialog(todoDatas[holder.adapterPosition], "수정하기")
                    modifyTaskDialog.registerEvent(object: EditDialog.EditDialogInterface {
                        override fun onEditDialogButtonClick(text: String) {
                            holder.binding.itemRecyclerDialogTextView.text = text
                            // TODO("데이터베이스를 업데이트 하려면, Task를 식별할 수 있는 날짜가 필요,,")
                            modifyTaskDialog.dismiss()
                            choiceDialog.dismiss()
                        }
                    })
                    modifyTaskDialog.show(fragmentManager, "modifyTaskDialog_show")
                }

                override fun onChoice2Click() {
                    CoroutineScope(Dispatchers.Main).launch {
                        CoroutineScope(Dispatchers.IO).async {
                            CalendarData.deleteTask(targetDate, todoDatas[holder.adapterPosition])
                            todoDatas = CalendarData.loadTodosByDate(targetDate)!!
                        }.await()
                        notifyDataSetChanged()
                        recyclerDialogInterface.onItemClick(targetDate, todoDatas[holder.adapterPosition])
                    }
                    choiceDialog.dismiss()
                }

            })

            choiceDialog.show(fragmentManager, "Choicefragment_show")

        }
    }

    override fun getItemCount(): Int {
        return todoDatas.size
    }

    fun refreshDataByDate(targetDate: LocalDate) {
        CoroutineScope(Dispatchers.IO).launch {
            todoDatas = CalendarData.loadTodosByDate(targetDate)!!
        }
    }
}