package com.example.making_calendar.dialog

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.making_calendar.databinding.DialogChoiceBinding

class ChoiceDialog(
    text: String, choice1Title: String, choice2Title: String
) : DialogFragment() {
    private var _binding: DialogChoiceBinding? = null
    private val binding get() = _binding!!

    private var choiceDialogInterface: ChoiceDialogInterface? = null
    private var text: String
    private var choice1Title: String
    private var choice2Title: String

    init {
        this.text = text
        this.choice1Title = choice1Title
        this.choice2Title = choice2Title
    }

    interface ChoiceDialogInterface {
        fun onChoice1Click()
        fun onChoice2Click()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogChoiceBinding.inflate(inflater, container, false)
        val view = binding.root

        renameViews()
        initButtonEvents()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth*0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    fun renameViews() {
        binding.dialogChoiceText.text = text
        binding.dialogChoiceChoice1.text = choice1Title
        binding.dialogChoiceChoice2.text = choice2Title
    }

    fun initButtonEvents() {
        binding.dialogChoiceChoice1.setOnClickListener {
            this.choiceDialogInterface?.onChoice1Click()
        }
        binding.dialogChoiceChoice2.setOnClickListener {
            this.choiceDialogInterface?.onChoice2Click()
        }
    }

    fun registerEvents(choiceDialogInterface: ChoiceDialogInterface) {
        this.choiceDialogInterface = choiceDialogInterface
    }
}