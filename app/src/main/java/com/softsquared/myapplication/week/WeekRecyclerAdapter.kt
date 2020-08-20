package com.softsquared.myapplication.week

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.myapplication.MainViewModel
import com.softsquared.myapplication.R
import com.softsquared.myapplication.month.BaseCalendar
import com.softsquared.myapplication.month.InMonthRecyclerAdapter
import com.softsquared.myapplication.month.ViewHolderHelper
import com.softsquared.myapplication.today.TodayFragment
import kotlinx.android.synthetic.main.item_schedule.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


//fragment랑 adapter가 구조가 약간 꼬여있는 것 같아~ adapter에서 fragment를 가질 수 밖에 없는 지금 그런 구존데 구조를 정리해봐바 한번 ㅎㅎ
class WeekRecyclerAdapter(weekFragment: WeekFragment, viewModel: MainViewModel) :
    RecyclerView.Adapter<ViewHolderHelper>() {
    val weekFragment = weekFragment
    val baseCalendar = BaseCalendar()
    lateinit var curYearMonth: String
    val viewModel = viewModel
    val day_arr: ArrayList<String> =
        ArrayList(listOf("sun", "mon", "tue", "wed", "thu", "fri", "sat"))

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

    @SuppressLint("NewApi", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolderHelper, position: Int) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 30)

        holder.itemView.layoutParams = params
        holder.itemView.tv_day.text = day_arr[position % BaseCalendar.DAYS_OF_WEEK]
        if (position % BaseCalendar.DAYS_OF_WEEK == 0) {
            holder.itemView.tv_date.setTextColor(
                Color.parseColor(
                    "#ff1200"
                )
            )
            holder.itemView.tv_day.setTextColor(
                Color.parseColor(
                    "#ff1200"
                )
            )
        } else if (position % BaseCalendar.DAYS_OF_WEEK > 0 && position % BaseCalendar.DAYS_OF_WEEK < 6) {
            holder.itemView.tv_date.setTextColor(
                Color.parseColor(
                    "#676d6e"
                )
            )
            holder.itemView.tv_day.setTextColor(
                Color.parseColor(
                    "#676d6e"
                )
            )
        } else if (position % BaseCalendar.DAYS_OF_WEEK == 6) {
            holder.itemView.tv_date.setTextColor(
                Color.parseColor(
                    "#0012f0"
                )
            )
            holder.itemView.tv_day.setTextColor(
                Color.parseColor(
                    "#0012f0"
                )
            )
        }

        val sdf = SimpleDateFormat("yyyy-MM", Locale.KOREA)
        var cur = sdf.format(baseCalendar.calendar.time)
        var curDate = baseCalendar.data[position].toString()
        if (baseCalendar.data[position] < 10) {
            curDate = "0" + curDate
        }
        if (position < baseCalendar.prevMonthTailOffset) {
            holder.itemView.tv_date.alpha = 0.3f
            holder.itemView.tv_day.alpha = 0.3f
            cur = baseCalendar.getPrevMonth() + "-" + curDate
        } else if (position >= baseCalendar.prevMonthTailOffset + baseCalendar.currentMonthMaxDate) {
            holder.itemView.tv_date.alpha = 0.3f
            holder.itemView.tv_day.alpha = 0.3f
            cur = baseCalendar.getNextMonth() + "-" + curDate
        } else {
            holder.itemView.tv_date.alpha = 1f
            holder.itemView.tv_day.alpha = 1f
            cur = cur + "-" + curDate
        }
        if (cur.equals(LocalDate.now().toString())) {
            holder.itemView.tv_today_marker.text = "Today!"
        }
        holder.itemView.tv_date.text = baseCalendar.data[position].toString()

        var dayArray = viewModel.getDayList(cur)
        if (dayArray.size == 0) {
            holder.itemView.layoutParams.height = 100
        }
        var inMonthRecyclerAdapter = InMonthRecyclerAdapter(
            weekFragment.activity!!,
            ArrayList(dayArray),
            cur,
            weekFragment = this.weekFragment
        )
        holder.itemView.rv_item_contents.adapter = inMonthRecyclerAdapter
        val lm = LinearLayoutManager(weekFragment.activity)
        holder.itemView.rv_item_contents.layoutManager = lm

        holder.itemView.setOnClickListener {
            weekFragment.mainActivity.reloadTodayFragment(TodayFragment(cur))
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
        return weekFragment.refreshCurrentWeek(calendar)
    }
}