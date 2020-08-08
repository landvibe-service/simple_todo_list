package com.softsquared.myapplication.today

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.myapplication.R
import com.softsquared.myapplication.month.BaseCalendar
import com.softsquared.myapplication.month.ViewHolderHelper
import kotlinx.android.synthetic.main.item_schedule.view.*
import java.text.SimpleDateFormat
import java.util.*

//이거도 adapter와 fragment간 관계 조금 정리 해바바~ 한번 시도해보길!!
class DialogRecyclerAdapter(val dialog: DialogPlanAdder, val set_data: MutableSet<String>) :
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
            cur  = cur + "-" + curDate
        }
        holder.itemView.tv_date.text = baseCalendar.data[position].toString()
        val item_layout = holder.itemView.findViewById<LinearLayout>(R.id.ll_item_dialog)
        if(set_data.contains(cur)){
            Log.e("포함됨 : ", cur)
            item_layout.setBackgroundColor(Color.argb(255, 100, 100, 100))
        }else{
            item_layout.setBackgroundColor(Color.argb(255, 100, 200, 200))
        }
        holder.itemView.setOnClickListener {
            Log.e("선택된 날짜", cur)
            if(set_data.contains(cur)){
                set_data.remove(cur)
                item_layout.setBackgroundColor(Color.argb(255, 100, 200, 200))
                Log.e("set size : ", set_data.size.toString())
            }else{
                set_data.add(cur)
                item_layout.setBackgroundColor(Color.argb(255, 100, 100, 100))
                Log.e("set size : ", set_data.size.toString())
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