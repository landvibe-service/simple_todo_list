package com.softsquared.myapplication.month

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.myapplication.MainViewModel
import com.softsquared.myapplication.R
import kotlinx.android.synthetic.main.item_schedule.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class MonthRecyclerAdapter(val viewModel: MainViewModel) :
    RecyclerView.Adapter<ViewHolderHelper>() {
    val df = SimpleDateFormat("yyyy-mm-dd", Locale.KOREA)
    val baseCalendar = BaseCalendar()
    lateinit var curYearMonth: String

    init {
        baseCalendar.initBaseCalendar {
            curYearMonth = df.format(baseCalendar.calendar.time).toString()
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

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolderHelper, position: Int) {
        if (position % BaseCalendar.DAYS_OF_WEEK == 0) holder.itemView.tv_date.setTextColor(
            Color.parseColor(
                "#ff1200"
            )
        )
        else if (position % BaseCalendar.DAYS_OF_WEEK == 6) holder.itemView.tv_date.setTextColor(
            Color.parseColor(
                "#0012f0"
            )
        )
        else holder.itemView.tv_date.setTextColor(Color.parseColor("#676d6e"))

        val sdf = SimpleDateFormat("yyyy-MM", Locale.KOREAN)

        var cur = sdf.format(baseCalendar.calendar.time)
        var curDate = baseCalendar.data[position].toString()
        if (baseCalendar.data[position] < 10) {
            curDate = "0" + curDate
        }
        if (position < baseCalendar.prevMonthTailOffset) {
            holder.itemView.tv_date.alpha = 0.3f
            cur = baseCalendar.getPrevMonth() + "-" + curDate
        } else if (position >= baseCalendar.prevMonthTailOffset + baseCalendar.currentMonthMaxDate) {
            holder.itemView.tv_date.alpha = 0.3f
            cur = baseCalendar.getNextMonth() + "-" + curDate
        } else {
            holder.itemView.tv_date.alpha = 1f
            cur = cur + "-" + curDate
        }
        holder.itemView.tv_date.text = baseCalendar.data[position].toString()

        if (cur.equals(LocalDate.now().toString())) {
            holder.itemView.tv_today_marker.text = "Today!"

            holder.itemView.tv_date.isVisible = false
            holder.itemView.tv_day.isVisible = false
        }

        var inMonthRecyclerAdapter = InMonthRecyclerAdapter(
            ArrayList(viewModel.getDayList(cur)),
            cur,
            viewModel = this.viewModel
        )

        holder.itemView.rv_item_contents.adapter = inMonthRecyclerAdapter
        val lm = LinearLayoutManager(holder.containerView.context)
        holder.itemView.rv_item_contents.layoutManager = lm

        holder.itemView.setOnClickListener {
            viewModel.getCalendar().set(Calendar.YEAR, cur.subSequence(0, 4).toString().toInt())
            viewModel.getCalendar()
                .set(Calendar.MONTH, cur.subSequence(5, 7).toString().toInt() - 1)
            viewModel.getCalendar()
                .set(Calendar.DAY_OF_MONTH, cur.subSequence(8, 10).toString().toInt())
            viewModel.getBottomNavigationView().selectedItemId = R.id.bni_today
        }
    }

    fun changeToCurMonth() {
        baseCalendar.changeToCurMonth {
            curYearMonth = df.format(baseCalendar.calendar.time).toString()
        }
    }

    fun changeToPrevMonth() {
        baseCalendar.changeToPrevMonth {
            curYearMonth = df.format(baseCalendar.calendar.time).toString()
        }
    }

    fun changeToNextMonth() {
        baseCalendar.changeToNextMonth {
            curYearMonth = df.format(baseCalendar.calendar.time).toString()
        }
    }
}