package com.softsquared.myapplication.month

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.myapplication.MainViewModel
import com.softsquared.myapplication.R
import com.softsquared.myapplication.db.Todo
import kotlinx.android.synthetic.main.item_schedule_in_cal.view.*
import java.util.*

class InMonthRecyclerAdapter(
    val items: ArrayList<Todo>,
    val curDate: String,
    val viewModel: MainViewModel
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
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            viewModel.getCalendar()
                .set(Calendar.YEAR, curDate.subSequence(0, 4).toString().toInt())
            viewModel.getCalendar()
                .set(Calendar.MONTH, curDate.subSequence(5, 7).toString().toInt() - 1)
            viewModel.getCalendar()
                .set(Calendar.DAY_OF_MONTH, curDate.subSequence(8, 10).toString().toInt())
            viewModel.getBottomNavigationView().selectedItemId = R.id.bni_today
        }
        if (items[position].clear) {
            holder.itemView.tv_contents_in_cal.setTextColor(
                Color.parseColor(
                    "#bbbbbb"
                )
            )
        } else {
            holder.itemView.tv_contents_in_cal.setTextColor(
                Color.parseColor(
                    "#000000"
                )
            )
        }
    }

    inner class ViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        val contents = itemView?.findViewById<TextView>(R.id.tv_contents_in_cal)
        fun bind(item: Todo) {
            if (contents != null) {
                contents.text = item.contents
            }
        }
    }
}