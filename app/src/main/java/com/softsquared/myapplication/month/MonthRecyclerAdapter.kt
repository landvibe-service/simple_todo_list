package com.softsquared.myapplication.month

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.myapplication.R
import com.softsquared.myapplication.db.AppDatabase
import com.softsquared.myapplication.today.TodayFragment
import kotlinx.android.synthetic.main.item_schedule.view.*
import java.util.*

class MonthRecyclerAdapter(val monthFragment: MonthFragment, val todayDB: AppDatabase) :
    RecyclerView.Adapter<ViewHolderHelper>() {

    val baseCalendar = BaseCalendar()
    lateinit var curYearMonth: String

    init {
        baseCalendar.initBaseCalendar {
            curYearMonth = refreshView(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHelper {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return ViewHolderHelper(view)
    }

    override fun getItemCount(): Int {
        return BaseCalendar.LOW_OF_CALENDAR * BaseCalendar.DAYS_OF_WEEK
    }

    override fun onBindViewHolder(holder: ViewHolderHelper, position: Int) {
        if (position % BaseCalendar.DAYS_OF_WEEK == 0) holder.itemView.tv_date.setTextColor(
            Color.parseColor(
                "#ff1200"
            )
        )
        else holder.itemView.tv_date.setTextColor(Color.parseColor("#676d6e"))

        if (position < baseCalendar.prevMonthTailOffset || position >= baseCalendar.prevMonthTailOffset + baseCalendar.currentMonthMaxDate) {
            holder.itemView.tv_date.alpha = 0.3f
        } else {
            holder.itemView.tv_date.alpha = 1f
        }
        holder.itemView.tv_date.text = baseCalendar.data[position].toString()
        var cur = "${curYearMonth + "-" + baseCalendar.data[position].toString()}"

        var inMonthRecyclerAdapter = InMonthRecyclerAdapter(
            monthFragment.activity!!,
            ArrayList(todayDB.todoDao().getDayList(cur))
        )
        holder.itemView.rv_item_contents.adapter = inMonthRecyclerAdapter

        val lm = LinearLayoutManager(monthFragment.activity)
        holder.itemView.rv_item_contents.layoutManager = lm

        todayDB.todoDao().getDayList(cur)

        holder.itemView.setOnClickListener {
            Log.e(cur, "is clicked")
            val fragment = TodayFragment(cur)
            monthFragment.activity!!.supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragment, fragment.javaClass.getSimpleName()) .commit()
        }
    }

    fun changeToPrevMonth() {
        baseCalendar.changeToPrevMonth {
            curYearMonth = refreshView(it)
        }
    }

    fun changeToNextMonth() {
        baseCalendar.changeToNextMonth {
            curYearMonth = refreshView(it)
        }
    }

    private fun refreshView(calendar: Calendar): String {
        notifyDataSetChanged()
        return monthFragment.refreshCurrentMonth(calendar)
    }
}