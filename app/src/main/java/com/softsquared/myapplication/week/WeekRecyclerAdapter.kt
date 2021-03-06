package com.softsquared.myapplication.week

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.myapplication.MainViewModel
import com.softsquared.myapplication.R
import com.softsquared.myapplication.month.BaseCalendar
import com.softsquared.myapplication.month.InMonthRecyclerAdapter
import com.softsquared.myapplication.month.ViewHolderHelper
import kotlinx.android.synthetic.main.item_schedule.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class WeekRecyclerAdapter(viewModel: MainViewModel) :
    RecyclerView.Adapter<ViewHolderHelper>() {
    lateinit var curYearMonth: String
    val viewModel = viewModel
    val df = SimpleDateFormat("yyyy-mm-dd", Locale.KOREA)
    val sdf = SimpleDateFormat("yyyy-MM", Locale.KOREA)
    val baseCalendar = BaseCalendar()
    val day_arr: ArrayList<String> =
        ArrayList(listOf("sun", "mon", "tue", "wed", "thu", "fri", "sat"))

    init {
        baseCalendar.initBaseCalendar {
            curYearMonth = df.format(viewModel.getCalendar().time).toString()
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
        val marginParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        marginParam.setMargins(10, 0, 0, 0)
        holder.itemView.ll_today_list.layoutParams = marginParam

        holder.itemView.layoutParams = params
        holder.itemView.tv_day.text = day_arr[(position % BaseCalendar.DAYS_OF_WEEK)]

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
            holder.itemView.tv_today_marker.isVisible = true
            holder.itemView.tv_today_marker.gravity = Gravity.RIGHT
        } else {
            holder.itemView.tv_today_marker.isVisible = false
        }
        holder.itemView.tv_date.text = baseCalendar.data[position].toString()
        var dayArray = viewModel.getDayList(cur)

        if (dayArray.size == 0) {
            holder.itemView.tv_empty_item.visibility = View.VISIBLE
        } else {
            holder.itemView.tv_empty_item.visibility = View.GONE
        }
        var inMonthRecyclerAdapter = InMonthRecyclerAdapter(
            ArrayList(dayArray),
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

    fun changeToPrevMonth() {
        baseCalendar.changeToPrevMonth {
            curYearMonth = df.format(it.time).toString()
        }
    }

    fun changeToCurMonth() {
        baseCalendar.changeToCurMonth {
            curYearMonth = df.format(it.time).toString()
        }
    }

    fun changeToNextMonth() {
        baseCalendar.changeToNextMonth {
            curYearMonth = df.format(it.time).toString()
        }
    }
}