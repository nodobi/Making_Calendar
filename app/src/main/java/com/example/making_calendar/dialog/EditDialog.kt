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
    editDialogOnClickListener : EditDialogInterface
) : DialogFragment() {
    private var _binding: DialogEditBinding? = null
    private val binding get() = _binding!!


    var editDialogOnClickListener: EditDialogInterface? = null
    private var text: String? = null;
    private var buttonText : String? = null;

    init {
        this.editDialogOnClickListener = editDialogOnClickListener
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

        binding.dialogEditButton.setOnClickListener {
            val text = binding.dialogEditEditText.text.toString()
            editDialogOnClickListener?.onEditDialogButtonClick(text)
            dismiss()
        }
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

}
