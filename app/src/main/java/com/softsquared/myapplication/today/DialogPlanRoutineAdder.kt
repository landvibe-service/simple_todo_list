package com.softsquared.myapplication.today

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Window
import android.widget.DatePicker
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.softsquared.myapplication.R
import kotlinx.android.synthetic.main.custom_dialog_set_routine.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DialogPlanRoutineAdder(context: Context, activity: Activity, cal: Calendar) {
    private val dlg = Dialog(context)
    val context = context
    val activity = activity
    val cal = cal
    var check_arr = ArrayList<Boolean>(listOf(false, false, false, false, false, false, false))

    fun start(arr: ArrayList<String>) {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.custom_dialog_set_routine)
        cal.time = Date()
        check_arr = ArrayList(listOf(false, false, false, false, false, false, false))
        dlg.et_dialog_start_date.setOnClickListener {

            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                activity!!,
                DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    var selected_date = (year.toString() + "-")
                    if (monthOfYear + 1 < 10) {
                        selected_date += "0"
                    }
                    selected_date += ((monthOfYear + 1).toString() + "-")
                    if (dayOfMonth < 10) {
                        selected_date += "0"
                    }
                    selected_date += dayOfMonth.toString()
                    dlg.et_dialog_start_date.setText(selected_date)

                    cal.time = Date()
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        dlg.et_dialog_end_date.setOnClickListener {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                activity!!,
                DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    var selected_date = (year.toString() + "-")
                    if (monthOfYear + 1 < 10) {
                        selected_date += "0"
                    }
                    selected_date += ((monthOfYear + 1).toString() + "-")
                    if (dayOfMonth < 10) {
                        selected_date += "0"
                    }
                    selected_date += dayOfMonth.toString()
                    dlg.et_dialog_end_date.setText(selected_date)

                    cal.time = Date()
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        dlg.tv_dialog_sun.setOnClickListener {
            if (check_arr[0] == true) {
                check_arr[0] = false
                dlg.tv_dialog_sun.setBackgroundColor(Color.TRANSPARENT)
            } else {
                check_arr[0] = true
                dlg.tv_dialog_sun.setBackgroundResource(R.drawable.ic_selected_back)
            }
        }
        dlg.tv_dialog_mon.setOnClickListener {
            if (check_arr[1] == true) {
                check_arr[1] = false
                dlg.tv_dialog_mon.setBackgroundColor(Color.TRANSPARENT)
            } else {
                check_arr[1] = true
                dlg.tv_dialog_mon.setBackgroundResource(R.drawable.ic_selected_back)
            }
        }
        dlg.tv_dialog_tue.setOnClickListener {
            if (check_arr[2] == true) {
                check_arr[2] = false
                dlg.tv_dialog_tue.setBackgroundColor(Color.TRANSPARENT)
            } else {
                check_arr[2] = true
                dlg.tv_dialog_tue.setBackgroundResource(R.drawable.ic_selected_back)
            }
        }
        dlg.tv_dialog_wed.setOnClickListener {
            if (check_arr[3] == true) {
                check_arr[3] = false
                dlg.tv_dialog_wed.setBackgroundColor(Color.TRANSPARENT)
            } else {
                check_arr[3] = true
                dlg.tv_dialog_wed.setBackgroundResource(R.drawable.ic_selected_back)
            }
        }
        dlg.tv_dialog_thu.setOnClickListener {
            if (check_arr[4] == true) {
                check_arr[4] = false
                dlg.tv_dialog_thu.setBackgroundColor(Color.TRANSPARENT)
            } else {
                check_arr[4] = true
                dlg.tv_dialog_thu.setBackgroundResource(R.drawable.ic_selected_back)
            }
        }
        dlg.tv_dialog_fri.setOnClickListener {
            if (check_arr[5] == true) {
                check_arr[5] = false
                dlg.tv_dialog_fri.setBackgroundColor(Color.TRANSPARENT)
            } else {
                check_arr[5] = true
                dlg.tv_dialog_fri.setBackgroundResource(R.drawable.ic_selected_back)
            }
        }
        dlg.tv_dialog_sat.setOnClickListener {
            if (check_arr[6] == true) {
                check_arr[6] = false
                dlg.tv_dialog_sat.setBackgroundColor(Color.TRANSPARENT)
            } else {
                check_arr[6] = true
                dlg.tv_dialog_sat.setBackgroundResource(R.drawable.ic_selected_back)
            }
        }

        dlg.btn_dialog_routine_ok.setOnClickListener {
            val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            var start_day = dlg.et_dialog_start_date.text.toString()
            var end_day = dlg.et_dialog_end_date.text.toString()

            var flag = false

            for (i in check_arr) {
                if (i) {
                    flag = i
                    break
                }
            }

            if (start_day.isEmpty()) {
                Toast.makeText(context, "시작 날짜를 입력하세요.", LENGTH_SHORT).show()
            } else if (end_day.isEmpty()) {
                Toast.makeText(context, "끝나는 날짜를 입력하세요.", LENGTH_SHORT).show()
            } else if (start_day > end_day) {
                Toast.makeText(context, "끝나는 날을 시작하는 날 이후로 설정해 주세요.", LENGTH_SHORT).show()
            } else if (!flag) {
                Toast.makeText(context, "반복할 요일을 설정해 주세요.", LENGTH_SHORT).show()
            } else {
                cal.set(Calendar.YEAR, start_day.subSequence(0, 4).toString().toInt())
                cal.set(Calendar.MONTH, start_day.subSequence(5, 7).toString().toInt() - 1)
                cal.set(Calendar.DAY_OF_MONTH, start_day.subSequence(8, 10).toString().toInt())

                while (true) {
                    if (df.format(cal.time).toString() <= end_day) {
                        if (!check_arr[cal.get(Calendar.DAY_OF_WEEK) - 1]) {
                            cal.add(Calendar.DATE, +1)
                            continue
                        }
                        val year = cal.get(Calendar.YEAR)
                        val month = cal.get(Calendar.MONTH) + 1
                        val day = cal.get(Calendar.DAY_OF_MONTH)
                        var add_now = year.toString() + "-"
                        if (month < 10) {
                            add_now += "0"
                        }
                        add_now += (month.toString() + "-")
                        if (day < 10) {
                            add_now += "0"
                        }
                        add_now += day.toString()
                        arr.add(add_now)
                        cal.add(Calendar.DATE, +1)
                    } else {
                        break
                    }
                }
                cal.time = Date()
                dlg.dismiss()
            }
        }
        dlg.btn_dialog_routine_cancle.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }
}