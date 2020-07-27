package com.softsquared.myapplication.month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.softsquared.myapplication.BaseFragment
import com.softsquared.myapplication.R
import com.softsquared.myapplication.db.AppDatabase
import kotlinx.android.synthetic.main.fragment_month.*
import java.text.SimpleDateFormat
import java.util.*

class MonthFragment : BaseFragment(){

    lateinit var scheduleRecyclerViewAdapter: MonthRecyclerAdapter
    lateinit var today_db: AppDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        var view = inflater.inflate(R.layout.fragment_month, container, false)
        today_db = AppDatabase.getInstance(activity!!)!!
        return view
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    fun initView() {

        scheduleRecyclerViewAdapter = MonthRecyclerAdapter(this, today_db)

        rv_schedule.layoutManager = GridLayoutManager(context, BaseCalendar.DAYS_OF_WEEK)
        rv_schedule.adapter = scheduleRecyclerViewAdapter
        rv_schedule.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        rv_schedule.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        tv_prev_month.setOnClickListener {
            scheduleRecyclerViewAdapter.changeToPrevMonth()
        }

        tv_next_month.setOnClickListener {
            scheduleRecyclerViewAdapter.changeToNextMonth()
        }
    }

    fun refreshCurrentMonth(calendar: Calendar) : String{
        val sdf = SimpleDateFormat("yyyy-MM", Locale.KOREAN)
        tv_current_month.text = sdf.format(calendar.time)
        return tv_current_month.text.toString()
    }
}