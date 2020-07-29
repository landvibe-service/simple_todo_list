package com.softsquared.myapplication.month

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.myapplication.R
import com.softsquared.myapplication.db.Todo
import com.softsquared.myapplication.today.TodayFragment

class InMonthRecyclerAdapter(
    val context: Context,
    val items: ArrayList<Todo>,
    val curDate: String,
    val monthFragment: MonthFragment
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
        holder?.bind(items[position], context)
        holder.itemView.setOnClickListener {
            monthFragment.mainActivity.reloadTodayFragment(TodayFragment(curDate))
        }
    }

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