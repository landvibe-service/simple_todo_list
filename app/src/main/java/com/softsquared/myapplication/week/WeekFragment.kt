package com.softsquared.myapplication.week

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.myapplication.MainViewModel
import com.softsquared.myapplication.R
import kotlinx.android.synthetic.main.fragment_week.*
import java.text.SimpleDateFormat
import java.util.*

class WeekFragment : Fragment {
    constructor(vm: MainViewModel) {
        this.viewModel = vm
    }

    lateinit var weekRecyclerAdapter: WeekRecyclerAdapter
    val viewModel: MainViewModel
    val df = SimpleDateFormat("yyyy-MM", Locale.KOREA)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_week, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_current_week.text = df.format(viewModel.getCalendar().time)
        weekRecyclerAdapter = WeekRecyclerAdapter(viewModel)
        val spaceDecoration = VerticalSpaceItemDecoration(20)
        rv_week.addItemDecoration(spaceDecoration)
        rv_week.layoutManager = LinearLayoutManager(context)
        rv_week.adapter = weekRecyclerAdapter

        fragment_week_today_button.setOnClickListener {
            weekRecyclerAdapter.changeToCurMonth()
            tv_current_week.text =
                df.format(weekRecyclerAdapter.baseCalendar.calendar.time).toString()
            rv_week.adapter?.notifyDataSetChanged()
        }
        tv_prev_week.setOnClickListener {
            weekRecyclerAdapter.changeToPrevMonth()
            tv_current_week.text =
                df.format(weekRecyclerAdapter.baseCalendar.calendar.time).toString()
            weekRecyclerAdapter.notifyDataSetChanged()
        }
        tv_next_week.setOnClickListener {
            weekRecyclerAdapter.changeToNextMonth()
            tv_current_week.text =
                df.format(weekRecyclerAdapter.baseCalendar.calendar.time).toString()
            weekRecyclerAdapter.notifyDataSetChanged()
        }
    }

    fun reloadView() {
        tv_current_week.text = df.format(weekRecyclerAdapter.baseCalendar.calendar.time)
        rv_week?.adapter?.notifyDataSetChanged()
    }

    inner class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = verticalSpaceHeight
        }
    }
}