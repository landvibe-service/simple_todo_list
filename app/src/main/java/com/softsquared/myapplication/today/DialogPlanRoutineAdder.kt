package com.softsquared.myapplication.today

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Window
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import com.softsquared.myapplication.MainActivity
import com.softsquared.myapplication.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DialogPlanRoutineAdder(context: Context, activity: Activity, cal: Calendar) {
    private val dlg = Dialog(context)
    val context = context
    private lateinit var btnOK: Button
    private lateinit var btnCancel: Button
    lateinit var et_dialog_start_date: EditText
    lateinit var et_dialog_end_date: EditText
    val activity = activity
    val cal = cal
    var check_arr = ArrayList<Boolean>(listOf(false, false, false, false, false, false, false))
    lateinit var tv_dialog_sun: TextView
    lateinit var tv_dialog_mon: TextView
    lateinit var tv_dialog_tue: TextView
    lateinit var tv_dialog_wed: TextView
    lateinit var tv_dialog_thu: TextView
    lateinit var tv_dialog_fri: TextView
    lateinit var tv_dialog_sat: TextView
    fun start(arr: ArrayList<String>){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.custom_dialog_set_routine)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(true)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히도록
        btnOK = dlg.findViewById(R.id.btn_dialog_routine_ok)
        btnCancel = dlg.findViewById(R.id.btn_dialog_routine_cancle)
        et_dialog_start_date = dlg.findViewById(R.id.et_dialog_start_date)
        et_dialog_end_date = dlg.findViewById(R.id.et_dialog_end_date)
        tv_dialog_sun = dlg.findViewById(R.id.tv_dialog_sun)
        tv_dialog_mon = dlg.findViewById(R.id.tv_dialog_mon)
        tv_dialog_tue = dlg.findViewById(R.id.tv_dialog_tue)
        tv_dialog_wed = dlg.findViewById(R.id.tv_dialog_wed)
        tv_dialog_thu = dlg.findViewById(R.id.tv_dialog_thu)
        tv_dialog_fri = dlg.findViewById(R.id.tv_dialog_fri)
        tv_dialog_sat = dlg.findViewById(R.id.tv_dialog_sat)
        cal.time = Date()
        check_arr = ArrayList(listOf(false, false, false, false, false, false, false))
        et_dialog_start_date.setOnClickListener{

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
                    if(monthOfYear + 1 < 10){
                        selected_date += "0"
                    }
                    selected_date += ((monthOfYear + 1).toString() + "-")
                    if(dayOfMonth < 10){
                        selected_date += "0"
                    }
                    selected_date += dayOfMonth.toString()
                    et_dialog_start_date.setText(selected_date)

                    cal.time = Date()
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        et_dialog_end_date.setOnClickListener {
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
                    if(monthOfYear + 1 < 10){
                        selected_date += "0"
                    }
                    selected_date += ((monthOfYear + 1).toString() + "-")
                    if(dayOfMonth < 10){
                        selected_date += "0"
                    }
                    selected_date += dayOfMonth.toString()
                    et_dialog_end_date.setText(selected_date)

                    cal.time = Date()
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        tv_dialog_sun.setOnClickListener{
            if(check_arr[0] == true){
                check_arr[0] = false
                tv_dialog_sun.setBackgroundColor(Color.TRANSPARENT)
            }else{
                check_arr[0] = true
                tv_dialog_sun.setBackgroundResource(R.drawable.ic_selected_back)
            }
        }
        tv_dialog_mon.setOnClickListener{
            if(check_arr[1] == true){
                check_arr[1] = false
                tv_dialog_mon.setBackgroundColor(Color.TRANSPARENT)
            }else{
                check_arr[1] = true
                tv_dialog_mon.setBackgroundResource(R.drawable.ic_selected_back)
            }
        }
        tv_dialog_tue.setOnClickListener{
            if(check_arr[2] == true){
                check_arr[2] = false
                tv_dialog_tue.setBackgroundColor(Color.TRANSPARENT)
            }else{
                check_arr[2] = true
                tv_dialog_tue.setBackgroundResource(R.drawable.ic_selected_back)
            }
        }
        tv_dialog_wed.setOnClickListener{
            if(check_arr[3] == true){
                check_arr[3] = false
                tv_dialog_wed.setBackgroundColor(Color.TRANSPARENT)
            }else{
                check_arr[3] = true
                tv_dialog_wed.setBackgroundResource(R.drawable.ic_selected_back)
            }
        }
        tv_dialog_thu.setOnClickListener{
            if(check_arr[4] == true){
                check_arr[4] = false
                tv_dialog_thu.setBackgroundColor(Color.TRANSPARENT)
            }else{
                check_arr[4] = true
                tv_dialog_thu.setBackgroundResource(R.drawable.ic_selected_back)
            }
        }
        tv_dialog_fri.setOnClickListener{
            if(check_arr[5] == true){
                check_arr[5] = false
                tv_dialog_fri.setBackgroundColor(Color.TRANSPARENT)
            }else{
                check_arr[5] = true
                tv_dialog_fri.setBackgroundResource(R.drawable.ic_selected_back)
            }
        }
        tv_dialog_sat.setOnClickListener{
            if(check_arr[6] == true){
                check_arr[6] = false
                tv_dialog_sat.setBackgroundColor(Color.TRANSPARENT)
            }else{
                check_arr[6] = true
                tv_dialog_sat.setBackgroundResource(R.drawable.ic_selected_back)
            }
        }

        btnOK.setOnClickListener {
            // 시작날짜부터 조건에 맞는걸 arr에 넣기
            val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            var start_day = et_dialog_start_date.text.toString()
            var end_day = et_dialog_end_date.text.toString()

            var flag = false

            for(i in check_arr){
                if(i){
                    flag = i
                    break
                }
            }

            if(start_day.isEmpty()){
                Toast.makeText(context, "시작 날짜를 입력하세요.", LENGTH_SHORT).show()
            }else if(end_day.isEmpty()){
                Toast.makeText(context, "끝나는 날짜를 입력하세요.", LENGTH_SHORT).show()
            }else if(start_day > end_day){
                Toast.makeText(context, "끝나는 날을 시작하는 날 이후로 설정해 주세요.", LENGTH_SHORT).show()
            }else if(!flag){
                Toast.makeText(context, "반복할 요일을 설정해 주세요.", LENGTH_SHORT).show()
            }
            else{
                cal.set(Calendar.YEAR, start_day.subSequence(0, 4).toString().toInt())
                cal.set(Calendar.MONTH, start_day.subSequence(5, 7).toString().toInt() - 1)
                cal.set(Calendar.DAY_OF_MONTH, start_day.subSequence(8, 10).toString().toInt())

                while(true) {
                    Log.e("후보 날짜", df.format(cal.time).toString())
                    if(df.format(cal.time).toString() <= end_day){
                        Log.e("맞는 조건의 날짜", df.format(cal.time).toString())
                        if(check_arr[cal.get(Calendar.DAY_OF_WEEK) - 1]){
                            Log.e("진짜로 맞는 조건의 날짜", df.format(cal.time).toString())
                        }else{
                            cal.add(Calendar.DATE, +1)
                            continue
                        }
                        val year = cal.get(Calendar.YEAR)
                        val month = cal.get(Calendar.MONTH) + 1
                        val day = cal.get(Calendar.DAY_OF_MONTH)
                        var add_now = year.toString() + "-"
                        if(month < 10){
                            add_now += "0"
                        }
                        add_now += (month.toString() + "-")
                        if(day < 10){
                            add_now += "0"
                        }
                        add_now += day.toString()
                        arr.add(add_now)
                        cal.add(Calendar.DATE, +1)
                    }else{
                        Log.e("맞는 조건의 날짜", "없음")
                        break
                    }
                }
                cal.time = Date()
                dlg.dismiss()
            }
        }

        btnCancel.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }
}