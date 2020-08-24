package com.softsquared.myapplication.today

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import com.softsquared.myapplication.R
import com.softsquared.myapplication.month.BaseCalendar
import kotlinx.android.synthetic.main.dialog_calendar.*
import java.text.SimpleDateFormat
import java.util.*

class DialogPlanMultiAdder(context: Context) {
    private val dlg = Dialog(context)
    lateinit var scheduleRecyclerViewAdapter: DialogRecyclerAdapter
    val context = context
    var set_data = mutableSetOf<String>()

    fun start(arr: ArrayList<String>, et_dialog_sel_date: EditText) {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.dialog_calendar)
        dlg.setCancelable(true)
        initView()
        dlg.btn_dialog_calendar_ok.setOnClickListener {
            for (value in set_data) {
                arr.add(value)
            }
            dlg.dismiss()
            if (arr.size > 0) {
                et_dialog_sel_date.setText("${arr[0] + " 포함 '" + arr.size + "'건"}")
            }
        }
        dlg.btn_dialog_calendar_cancle.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }

    fun initView() {
        scheduleRecyclerViewAdapter = DialogRecyclerAdapter(this, set_data)
        dlg.rv_dialog_calendar.layoutManager = GridLayoutManager(context, BaseCalendar.DAYS_OF_WEEK)
        dlg.rv_dialog_calendar.adapter = scheduleRecyclerViewAdapter
        dlg.ibtn_dialog_prev.setOnClickListener {
            scheduleRecyclerViewAdapter.changeToPrevMonth()
        }
        dlg.ibtn_dialog_next.setOnClickListener {
            scheduleRecyclerViewAdapter.changeToNextMonth()
        }
    }

    fun refreshCurrentMonth(calendar: Calendar): String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.KOREAN)
        dlg.tv_dialog_current_month.text = sdf.format(calendar.time)
        return dlg.tv_dialog_current_month.text.toString()
    }
}