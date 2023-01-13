package com.example.making_calendar.dialog

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.making_calendar.adapter.RecyclerDialogAdapter
import com.example.making_calendar.databinding.DialogRecyclerBinding
import java.time.LocalDate

class RecyclerDialog(
    private val targetDate: LocalDate,
    private val recyclerDialogInterface: RecyclerDialogAdapter.RecyclerDialogInterface
) : DialogFragment() {
    private var _binding: DialogRecyclerBinding? = null
    private val binding get() = _binding!!
    private var recyclerDialogAdapter: RecyclerDialogAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = DialogRecyclerBinding.inflate(inflater, container, false)
        val view = binding.root

        initRecycler()
        registerEvents()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initRecycler() {
        recyclerDialogAdapter = RecyclerDialogAdapter(requireActivity().supportFragmentManager, targetDate)

        binding.dialogRecyclerRecyclerView.adapter = recyclerDialogAdapter
        var itemDecoration1 =
            DividerItemDecoration(activity?.applicationContext, DividerItemDecoration.VERTICAL)
        binding.dialogRecyclerRecyclerView.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.dialogRecyclerRecyclerView.addItemDecoration(itemDecoration1)

    }

    fun registerEvents() {
        recyclerDialogAdapter?.registerEvent(recyclerDialogInterface)
    }
}