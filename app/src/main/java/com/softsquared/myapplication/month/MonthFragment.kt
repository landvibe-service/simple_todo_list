package com.softsquared.myapplication.month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.softsquared.myapplication.MainViewModel
import com.softsquared.myapplication.R
import kotlinx.android.synthetic.main.fragment_month.*
import java.text.SimpleDateFormat
import java.util.*

class MonthFragment : Fragment {
    constructor(vm: MainViewModel) {
        this.viewModel = vm
    }

    lateinit var monthRecyclerAdapter: MonthRecyclerAdapter
    val viewModel: MainViewModel
    val df = SimpleDateFormat("yyyy-MM", Locale.KOREAN)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_month, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    fun reloadView() {
        tv_current_month.text = df.format(monthRecyclerAdapter.baseCalendar.calendar.time)
        rv_schedule.adapter?.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        monthRecyclerAdapter = MonthRecyclerAdapter(viewModel)
        tv_current_month.text = df.format(monthRecyclerAdapter.baseCalendar.calendar.time)
    }

    private fun initView() {
        rv_schedule.layoutManager = GridLayoutManager(context, BaseCalendar.DAYS_OF_WEEK)
        rv_schedule.adapter = monthRecyclerAdapter
        rv_schedule.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.HORIZONTAL
            )
        )
        rv_schedule.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        fragment_month_today_button.setOnClickListener {
            monthRecyclerAdapter.changeToCurMonth()
            tv_current_month.text = df.format(monthRecyclerAdapter.baseCalendar.calendar.time)
            rv_schedule.adapter?.notifyDataSetChanged()
        }
        tv_prev_month.setOnClickListener {
            monthRecyclerAdapter.changeToPrevMonth()
            tv_current_month.text = df.format(monthRecyclerAdapter.baseCalendar.calendar.time)
            monthRecyclerAdapter.notifyDataSetChanged()
        }
        tv_next_month.setOnClickListener {
            monthRecyclerAdapter.changeToNextMonth()
            tv_current_month.text = df.format(monthRecyclerAdapter.baseCalendar.calendar.time)
            monthRecyclerAdapter.notifyDataSetChanged()
        }
    }
}