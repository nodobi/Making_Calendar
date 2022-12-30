package com.example.making_calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.example.making_calendar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendarRecycler = binding.recyclerCalendar

        val myGridLayout = GridLayoutManager(applicationContext, 7)
        myGridLayout.orientation = GridLayoutManager.VERTICAL

        val dataSet : MutableList<String> = mutableListOf()
        for(i in 1..45) {
            dataSet.add("Day$i")
        }
        calendarRecycler.layoutManager = myGridLayout

        val adapter = CalendarAdapter(dataSet)
        calendarRecycler.adapter = adapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("hyeok", "onOptionsItemSelected")
        return super.onOptionsItemSelected(item)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        Log.d("hyeok", "onContextItemSelected")
        return super.onContextItemSelected(item)
    }
}