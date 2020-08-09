package com.softsquared.myapplication.today

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.myapplication.R
import com.softsquared.myapplication.month.BaseCalendar
import com.softsquared.myapplication.month.ViewHolderHelper
import kotlinx.android.synthetic.main.item_schedule.view.*
import java.text.SimpleDateFormat
import java.util.*

class DialogRecyclerAdapter(val dialog: DialogPlanMultiAdder, val set_data: MutableSet<String>) :
    RecyclerView.Adapter<ViewHolderHelper>() {
    val baseCalendar = BaseCalendar()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHelper {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_schedule_dialog, parent, false)
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
        val item_layout = holder.itemView.findViewById<LinearLayout>(R.id.ll_item_dialog)
        val item_text = holder.itemView.findViewById<TextView>(R.id.tv_date)
        if (set_data.contains(cur)) {
            item_text.setBackgroundResource(R.drawable.ic_selected_back)
        } else {
            item_text.setBackgroundColor(Color.TRANSPARENT)
        }
        holder.itemView.setOnClickListener {
            if (set_data.contains(cur)) {
                set_data.remove(cur)
                item_text.setBackgroundColor(Color.TRANSPARENT)
            } else {
                set_data.add(cur)
                item_text.setBackgroundResource(R.drawable.ic_selected_back)
            }
        }
    }


    lateinit var curYearMonth: String

    init {
        baseCalendar.initBaseCalendar {
            curYearMonth = refreshView(it)
        }
    }

    private fun refreshView(calendar: Calendar): String {
        notifyDataSetChanged()
        return dialog.refreshCurrentMonth(calendar)
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
}