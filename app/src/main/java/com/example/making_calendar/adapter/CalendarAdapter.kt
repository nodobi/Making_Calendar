package com.example.making_calendar.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.making_calendar.database.Task
import com.example.making_calendar.database.TaskDatabase
import com.example.making_calendar.databinding.ItemTextBinding
import com.example.making_calendar.dialog.EditDialog
import com.example.making_calendar.dialog.RecyclerDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields

class CalendarAdapter(
    private val fragmentManager: FragmentManager,
    private val context: Context
) :
    RecyclerView.Adapter<CalendarAdapter.MyViewHolder>() {
    var calendarDate: LocalDateTime = LocalDateTime.now()
    private var data: MutableList<LocalDate> = mutableListOf()
    val db = TaskDatabase.getInstance(context.applicationContext)

    init {
        refreshData()
    }

    class MyViewHolder(val binding: ItemTextBinding) : RecyclerView.ViewHolder(binding.root) {
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

            taskTextViewContainer.removeAllViews()
            for(todo in todoList) {
                addTaskOnItemContainer(todo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val weekCnt: Int = calendarDate.plusMonths(1).withDayOfMonth(1).minusDays(1)
            .get(WeekFields.SUNDAY_START.weekOfYear()) - calendarDate.withDayOfMonth(1)
            .get(WeekFields.SUNDAY_START.weekOfYear()) + 1
        val height = parent.height / weekCnt
        val width = parent.width / 7

        Log.d("hyeok", "height: $height, width: $width")

        val myViewHolder =
            MyViewHolder(ItemTextBinding.inflate(LayoutInflater.from(parent.context)))

        myViewHolder.itemView.layoutParams = RecyclerView.LayoutParams(width, height)
        return myViewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bind = holder.binding
        holder.localDate = data[position]
        holder.dateView.text = data[position].dayOfMonth.toString()
        var todoList: List<String>?
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("hyeok", "GetTask! Pos${position}")
            todoList = db!!.taskDao().getTodoListByDate(
                data[position].format(
                    DateTimeFormatter.ISO_DATE
                )
            )

            holder.onBind(data[position], todoList!!, context)
        }

        bind.root.setOnClickListener {
            Log.d("hyeok", "Clicked $position")
            val taskListDialog = RecyclerDialog(holder.localDate!!)
            taskListDialog.show(fragmentManager, "TaskListDialog_Show")
        }

        // 리사이클러 뷰 홀더에 클릭 이벤트 추가
        bind.root.setOnLongClickListener {

            val addTaskDialog = EditDialog(object : EditDialog.EditDialogInterface {
                override fun onEditDialogButtonClick(text: String) {

                    holder.addTaskOnItemContainer(text)
                    CoroutineScope(Dispatchers.IO).launch {
                        val newTask = Task(
                            holder.localDate!!.format(DateTimeFormatter.ISO_DATE),
                            null,
                            text
                        )
                        Log.d("hyeok", "insert!")
                        db!!.taskDao().insert(newTask)
                    }
                    notifyDataSetChanged()

                }
            })
            addTaskDialog.show(fragmentManager, "AddTaskDialog_Show")

            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun refreshData() {
        data = mutableListOf()
        val weekCnt: Int = calendarDate.plusMonths(1).withDayOfMonth(1).minusDays(1)
            .get(WeekFields.SUNDAY_START.weekOfYear()) - calendarDate.withDayOfMonth(1)
            .get(WeekFields.SUNDAY_START.weekOfYear()) + 1

        var firstDateDisplayedOn: LocalDate = calendarDate.toLocalDate().withDayOfMonth(1)
        firstDateDisplayedOn =
            firstDateDisplayedOn.minusDays((firstDateDisplayedOn.get(WeekFields.SUNDAY_START.dayOfWeek()) - 1).toLong())

        for (i in 0 until (weekCnt * 7)) {
            data.add(firstDateDisplayedOn.plusDays(i.toLong()))
        }
    }


}