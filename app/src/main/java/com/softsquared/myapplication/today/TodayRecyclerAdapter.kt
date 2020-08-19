package com.softsquared.myapplication.today

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.softsquared.myapplication.R
import com.softsquared.myapplication.db.AppDatabase
import com.softsquared.myapplication.db.Todo
import kotlinx.android.synthetic.main.item_today.view.*

class TodayRecyclerAdapter(
    val context: Context,
    val todayFragment: TodayFragment,
    val curDate: String,
    var items: ArrayList<Todo>,
    val viewBinderHelper: ViewBinderHelper = ViewBinderHelper(),
    val itemClick: (Todo) -> Unit

) :
    RecyclerView.Adapter<TodayRecyclerAdapter.ViewHolder>() {

    override fun getItemCount() = items.size
    fun removeItem(position: Int, today_db: AppDatabase) {
        val del = items.get(position)
        if (today_db != null) {
            today_db.todoDao().delete(del)
        }
        items.remove(del)

        notifyDataSetChanged()
    }

    fun modifyItem(position: Int){
        val mod = items.get(position)
        var today_db = AppDatabase.getInstance(context)
        if (today_db != null) {
            todayFragment.showAlertDialog(today_db, 2, mod)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewBinderHelper.setOpenOnlyOne(true)
        viewBinderHelper.bind(
            holder.swipeRevealLayout,
            items.get(position).id.toString()
        )
        viewBinderHelper.closeLayout((items.get(position).id.toString()))

        holder?.bind(items[position], context)
        val btn_del = holder?.btn_del
        if (btn_del != null) {
            btn_del.setOnClickListener {
            val today_db = AppDatabase.getInstance(context)
                if(today_db?.todoDao()?.getMyGroupSize(items.get(position).gid)!! > 1){
                    val builder = AlertDialog.Builder(context)
                    val dialogView = todayFragment.layoutInflater.inflate(R.layout.dialog_del_type, null)
                    val rbtn_type_one = dialogView.findViewById<RadioButton>(R.id.rbtn_type_one)
                    val rbtn_type_multi = dialogView.findViewById<RadioButton>(R.id.rbtn_type_multi)

                    builder.setView(dialogView)
                        .setPositiveButton("확인") { dialogInterface, i ->
                            if(rbtn_type_one.isChecked()){
                                removeItem(position, today_db)
                            }else{
                                today_db.todoDao().removeGroup(items.get(position).gid)
                                items.remove(items.get(position))
                                notifyDataSetChanged()
                            }
                        }
                        .setNegativeButton("취소") { dialogInterface, i ->
                        }
                        .show()
                }else{
                    removeItem(position, today_db)
                }
            }
        }
        val btn_modify = holder?.btn_modify
        btn_modify?.setOnClickListener {

            modifyItem(position)
        }

        holder.itemView.tv_contents.setOnClickListener {
            var today_db = AppDatabase.getInstance(context)

                items[position].clear = !items[position].clear
                today_db?.todoDao()?.update(items[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_today, parent, false)



        return ViewHolder(inflatedView, itemClick)
    }

    inner class ViewHolder(itemView: View?, itemClick: (Todo) -> Unit) :

        RecyclerView.ViewHolder(itemView!!) {
        val contents = itemView?.findViewById<CheckBox>(R.id.tv_contents)
        val btn_modify = itemView?.findViewById<Button>(R.id.btn_modify)
        val btn_del = itemView?.findViewById<Button>(R.id.btn_del)
        val swipeRevealLayout = itemView?.findViewById<SwipeRevealLayout>(R.id.swipelayout)


        fun bind(item: Todo, context: Context) {
            if (contents != null) {
                contents.text = item.contents
                contents.isChecked = item.clear
            }

            itemView.setOnClickListener { itemClick(item) }
        }

    }

}



