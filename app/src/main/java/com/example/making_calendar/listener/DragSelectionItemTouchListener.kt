package com.example.making_calendar.listener

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

private const val TOUCH_TAG = "Touch_Event"

class DragSelectionItemTouchListener(
    context: Context,
    private val listener: CalendarItemInteractionListener
) : RecyclerView.OnItemTouchListener {
    private val mGestureDetector = GestureDetector(context, LongGestureDetector())
    private var mViewHolderLongPressed: RecyclerView.ViewHolder? = null
    private var mViewHolderInFocus: RecyclerView.ViewHolder? = null
    private var mPreviousViewHolder: RecyclerView.ViewHolder? = null
    private var mSelectedViewHolderList: MutableList<RecyclerView.ViewHolder?> = arrayListOf()

    fun cancelLongPress() {
        mViewHolderLongPressed = null
        mPreviousViewHolder = null
    }

    fun selectViewHolder(rv: RecyclerView) {
        val start: Int =
            Math.min(mViewHolderLongPressed!!.adapterPosition, mViewHolderInFocus!!.adapterPosition)
        val end: Int =
            Math.max(mViewHolderLongPressed!!.adapterPosition, mViewHolderInFocus!!.adapterPosition)
        var mSelectedViewHolderPos: MutableList<Int> = mutableListOf()
        mSelectedViewHolderList = mutableListOf()
        for (idx in start..end) {
            mSelectedViewHolderList.add(rv.findViewHolderForAdapterPosition(idx))
            mSelectedViewHolderPos.add(idx)
        }

        listener.onMultipleViewHoldersSelected(rv, mSelectedViewHolderList)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TOUCH_TAG, "onInterceptTouchEvent")
        return true
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        val childViewUnder: View? = rv.findChildViewUnder(e.x, e.y)
        if (childViewUnder != null) {
            mViewHolderInFocus = rv.findContainingViewHolder(childViewUnder)
        }
        mGestureDetector.onTouchEvent(e)
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                // LongPress 상태에서 움직이는 경우,
                if (mViewHolderLongPressed != null) {
                    mPreviousViewHolder = mViewHolderInFocus
                    selectViewHolder(rv)
                }
            }
            MotionEvent.ACTION_UP -> {
                if (mViewHolderLongPressed != null) {
                    listener.onLongItemReleased(rv, mSelectedViewHolderList)
                    cancelLongPress()
                }
            }
        }
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }


    inner class LongGestureDetector : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            val rv = mViewHolderInFocus!!.itemView.parent as RecyclerView
            listener.onItemClicked(
                rv,
                mViewHolderInFocus,
                mViewHolderInFocus!!.adapterPosition)
            return super.onSingleTapUp(e)
        }

        override fun onLongPress(e: MotionEvent?) {
            val rv = mViewHolderInFocus!!.itemView.parent as RecyclerView
            mViewHolderLongPressed = mViewHolderInFocus
            mPreviousViewHolder = mViewHolderLongPressed

            listener.onLongItemClicked(
                rv,
                mViewHolderLongPressed,
                mViewHolderLongPressed!!.adapterPosition
            )
            selectViewHolder(rv)
            super.onLongPress(e)
        }
    }
}