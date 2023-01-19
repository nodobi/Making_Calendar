package com.example.making_calendar.listener

import androidx.recyclerview.widget.RecyclerView

interface CalendarItemInteractionListener {
    fun onLongItemClicked(
        recyclerView: RecyclerView?,
        mViewHolderTouched: RecyclerView.ViewHolder?,
        position: Int
    )

    fun onItemClicked(
        recyclerView: RecyclerView?,
        mViewHolderTouched: RecyclerView.ViewHolder?,
        position: Int
    )

    fun onMultipleViewHoldersSelected(rv: RecyclerView?, selection: List<RecyclerView.ViewHolder?>?)
    fun onLongItemReleased(rv: RecyclerView?, selection: List<RecyclerView.ViewHolder?>?)

}