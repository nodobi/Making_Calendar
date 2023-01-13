package com.example.making_calendar.dialog

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import androidx.fragment.app.DialogFragment
import com.example.making_calendar.databinding.DialogEditBinding

class EditDialog(
    hint: String?, buttonTitle: String
) : DialogFragment() {
    private var _binding: DialogEditBinding? = null
    private val binding get() = _binding!!

    private var editDialogInterface: EditDialogInterface? = null
    private var hint: String?
    private var buttonTitle : String

    init {
        this.hint = hint;
        this.buttonTitle = buttonTitle
    }

    interface EditDialogInterface {
        fun onEditDialogButtonClick(text : String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogEditBinding.inflate(inflater, container, false)
        val view = binding.root;

        renameViews()
        initButtonEvent()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
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
        dialog?.window?.attributes = params as LayoutParams
    }

    fun renameViews() {
        binding.dialogEditEditText.hint = hint
        binding.dialogEditButton.text = buttonTitle
    }

    fun initButtonEvent() {
        binding.dialogEditButton.setOnClickListener {
            val text = binding.dialogEditEditText.text.toString()
            editDialogInterface?.onEditDialogButtonClick(text)
        }
    }

    fun registerEvent(editDialogInterface: EditDialogInterface) {
        this.editDialogInterface = editDialogInterface
    }
}
