package com.softsquared.myapplication.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.softsquared.myapplication.MainViewModel
import com.softsquared.myapplication.R
import com.softsquared.myapplication.month.BaseCalendar
import com.softsquared.myapplication.month.MonthRecyclerAdapter
import kotlinx.android.synthetic.main.new_app_widget.*

class WidgetActivity: AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_app_widget)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val monthRecyclerAdapter = MonthRecyclerAdapter(viewModel)
        gv_schedule.adapter = monthRecyclerAdapter
    }
}