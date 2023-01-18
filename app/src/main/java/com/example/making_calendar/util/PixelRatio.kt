package com.example.making_calendar.util

import android.app.Application
import androidx.annotation.Px
import kotlin.math.roundToInt

class PixelRatio(private val app : Application) {
    private val displayMetrics
        get() = app.resources.displayMetrics

    @Px
    fun toPixel(dp: Int) = (dp * displayMetrics.density).roundToInt()
    fun toPixelForFloatDp(dp: Float) = (dp * displayMetrics.density).roundToInt()
}