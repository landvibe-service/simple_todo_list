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
import java.text.SimpleDateFormat
import java.util.*


//fragment랑 adapter가 구조가 약간 꼬여있는 것 같아~ adapter에서 fragment를 가질 수 밖에 없는 지금 그런 구존데 구조를 정리해봐바 한번 ㅎㅎ
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

        val sdf = SimpleDateFormat("yyyy-MM", Locale.KOREAN)

        var cur = sdf.format(baseCalendar.calendar.time)
        var curDate = baseCalendar.data[position].toString()
        if(baseCalendar.data[position] < 10) {
            curDate = "0"+curDate
        }
        if (position < baseCalendar.prevMonthTailOffset) {
            holder.itemView.tv_date.alpha = 0.3f
            cur = baseCalendar.getPrevMonth() + "-" + curDate
        }else if(position >= baseCalendar.prevMonthTailOffset + baseCalendar.currentMonthMaxDate){
            holder.itemView.tv_date.alpha = 0.3f
            cur = baseCalendar.getNextMonth() + "-" + curDate
        }
        else {
            holder.itemView.tv_date.alpha = 1f
            cur  = cur + "-" + curDate
        }
        holder.itemView.tv_date.text = baseCalendar.data[position].toString()


        var inMonthRecyclerAdapter = InMonthRecyclerAdapter(
            monthFragment.activity!!,
            ArrayList(todayDB.todoDao().getDayList(cur)),
            cur,
            monthFragment
        )
        holder.itemView.rv_item_contents.adapter = inMonthRecyclerAdapter
        val lm = LinearLayoutManager(monthFragment.activity)
        holder.itemView.rv_item_contents.layoutManager = lm

        holder.itemView.setOnClickListener {
            var nCal = baseCalendar.calendar
            monthFragment.mainActivity.reloadTodayFragment(TodayFragment(cur))
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