package com.softsquared.myapplication.week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.myapplication.BaseFragment
import com.softsquared.myapplication.MainActivity
import com.softsquared.myapplication.R
import com.softsquared.myapplication.db.AppDatabase
import kotlinx.android.synthetic.main.fragment_month.*
import kotlinx.android.synthetic.main.fragment_week.*
import java.text.SimpleDateFormat
import java.util.*

class WeekFragment : Fragment {
    lateinit var today_db: AppDatabase
    lateinit var weekRecyclerAdapter: WeekRecyclerAdapter
    lateinit var tv_current_week: TextView
    val mainActivity: MainActivity
    constructor(mainActivity: MainActivity){
        this.mainActivity = mainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_week, container, false)
        tv_current_week = rootView.findViewById(R.id.tv_current_week)
        today_db = activity?.let { AppDatabase.getInstance(it) }!!
        weekRecyclerAdapter = WeekRecyclerAdapter(this, today_db)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_week.adapter = weekRecyclerAdapter
        rv_week.layoutManager = LinearLayoutManager(context)
        fragment_week_today_button.setOnClickListener {
            rv_week.adapter = WeekRecyclerAdapter(this, today_db)
        }
        tv_prev_week.setOnClickListener {
            weekRecyclerAdapter.changeToPrevMonth()
        }
        tv_next_week.setOnClickListener {
            weekRecyclerAdapter.changeToNextMonth()
        }
    }
    fun reloadView(){
        rv_week?.adapter?.notifyDataSetChanged()
    }
    fun refreshCurrentWeek(calendar: Calendar): String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.KOREAN)
        tv_current_week.text = sdf.format(calendar.time).toString()
        return tv_current_week.text.toString()
    }
}