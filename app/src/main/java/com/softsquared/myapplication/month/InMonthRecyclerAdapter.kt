package com.softsquared.myapplication.month

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.myapplication.R
import com.softsquared.myapplication.db.Todo
import com.softsquared.myapplication.today.TodayFragment
import com.softsquared.myapplication.week.WeekFragment
import kotlinx.android.synthetic.main.item_schedule_in_cal.view.*

class InMonthRecyclerAdapter(
    val context: Context,
    val items: ArrayList<Todo>,
    val curDate: String,
    val monthFragment: MonthFragment? = null,
    val weekFragment: WeekFragment? = null
) : RecyclerView.Adapter<InMonthRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_schedule_in_cal, parent, false)
        return ViewHolder(inflatedView)
    }
    override fun getItemCount(): Int {
        return items.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], context)
        if(monthFragment != null){
            holder.itemView.setOnClickListener {
                monthFragment.mainActivity.reloadTodayFragment(TodayFragment(curDate))
            }
        }else if(weekFragment != null){
            holder.itemView.setOnClickListener {
                weekFragment?.mainActivity?.reloadTodayFragment(TodayFragment(curDate))
            }
        }
        if(items[position].clear){
            holder.itemView.tv_contents_in_cal.setTextColor(
                Color.parseColor(
                    "#bbbbbb"
                )
            )
        }
        else{
            holder.itemView.tv_contents_in_cal.setTextColor(
                Color.parseColor(
                    "#000000"
                )
            )
        }
    }
    //!!는 안쓰는게 좋아~
    inner class ViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        val contents = itemView?.findViewById<TextView>(R.id.tv_contents_in_cal)
        fun bind(item: Todo, context: Context) {
            if (contents != null) {
                contents.text = item.contents
            }
        }
    }
}