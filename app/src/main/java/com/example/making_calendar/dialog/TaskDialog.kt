package com.example.making_calendar.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.making_calendar.databinding.DialogTaskBinding

class TaskDialog(
    TaskDialogInterface: TaskDialogInterface,
    text: String, id: Int
) : DialogFragment() {

    private var _binding: DialogTaskBinding? = null
    private val binding get() = _binding!!

    private var taskDialogInterface: TaskDialogInterface? = null

    private var text: String? = null
    private var id: Int? = null

    init {
        this.text = text
        this.id = id
        this.taskDialogInterface = taskDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogTaskBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.okBtn.setOnClickListener {
            Log.d("hyeok", "TaskDialog.kt YesBtn")
            this.taskDialogInterface?.onYesButtonClick()
        }
        binding.noBtn.setOnClickListener {
            Log.d("hyeok", "TaskDialog.kt NoBtn")
            this.taskDialogInterface?.onNoButtonClick()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

interface TaskDialogInterface {
    fun onYesButtonClick()
    fun onNoButtonClick()
}