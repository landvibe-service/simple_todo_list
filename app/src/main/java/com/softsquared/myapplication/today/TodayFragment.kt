package com.softsquared.myapplication.today

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.myapplication.BaseFragment
import com.softsquared.myapplication.R
import com.softsquared.myapplication.db.AppDatabase
import com.softsquared.myapplication.db.Todo
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.android.synthetic.main.fragment_today.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TodayFragment : BaseFragment {
    lateinit var todayRecyclerView: RecyclerView
    lateinit var set_arr: ArrayList<String>
    var list = ArrayList<Todo>()
    var cal = Calendar.getInstance()
    val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    var today_date: String
    lateinit var tv_fragment_today_day: TextView
    val arr_day = listOf("일", "월", "화", "수", "목", "금", "토")

    constructor() {
        today_date = df.format(cal.time)
        Log.e("constructor 0개 : ", today_date)
        cal.time = Date()
    }

    constructor(curDay: String) {
        Log.e("constructor 1개 : ", curDay)
        today_date = curDay
        cal.set(Calendar.YEAR, curDay.subSequence(0, 4).toString().toInt())
        cal.set(Calendar.MONTH, curDay.subSequence(5, 7).toString().toInt() - 1)
        cal.set(Calendar.DAY_OF_MONTH, curDay.subSequence(8, 10).toString().toInt())
        Log.e("cal time", df.format(cal.time))
    }

    fun loadView(new_cal: Calendar, today_db: AppDatabase) {
        tv_toolbar.setText(df.format(new_cal.time))
        today_date = df.format(new_cal.time)
        tv_fragment_today_day.setText(arr_day[new_cal.get(Calendar.DAY_OF_WEEK) - 1])

        if (today_db != null) {
            todayRecyclerView.adapter = TodayRecyclerAdapter(
                activity!!,
                this,
                today_date,
                ArrayList(today_db.todoDao().getDayList(df.format(new_cal.time)))
            ) { item ->
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val today_db: AppDatabase? =
            AppDatabase.getInstance(activity!!)
        val rootView = inflater.inflate(R.layout.fragment_today, container, false)
        val tv_toolbar = rootView.findViewById<TextView>(R.id.tv_toolbar)

        tv_fragment_today_day = rootView.findViewById<TextView>(R.id.tv_fragment_today_day)
        todayRecyclerView = rootView.findViewById(R.id.rv_today_list)

        tv_toolbar.setText(today_date)
        tv_fragment_today_day.setText(arr_day[cal.get(Calendar.DAY_OF_WEEK) - 1])


        if (today_db != null) {
            list = ArrayList(today_db.todoDao().getDayList(today_date))
        }
        val adapter =
            TodayRecyclerAdapter(activity!!, this, today_date, list) { item ->
            }

        todayRecyclerView.adapter = adapter
        val lm = LinearLayoutManager(activity)
        todayRecyclerView.layoutManager = lm

        rootView.findViewById<ImageButton>(R.id.ibtn_prev_arrow).setOnClickListener {
            cal.add(Calendar.DATE, -1)

            if (today_db != null) {
                loadView(cal, today_db)
            }
        }
        rootView.findViewById<ImageButton>(R.id.ibtn_next_arrow).setOnClickListener {
            cal.add(Calendar.DATE, +1)
            if (today_db != null) {
                loadView(cal, today_db)
            }
        }
        rootView.findViewById<Button>(R.id.fragment_today_today_button).setOnClickListener {
            if (today_db != null) {
                today_date = df.format(Date())
                cal.time = Date()
                loadView(cal, today_db)
            }
        }

        rootView.findViewById<ImageButton>(R.id.btn_add).setOnClickListener {
            if (today_db != null) {

                showAlertDialog(today_db, 1, Todo("-", false, "-", today_db.todoDao().getNewGid()))
            }
        }
        return rootView
    }

    fun showAlertDialog(today_db: AppDatabase, cmd: Int, todo: Todo) {
        lateinit var dialogView: View
        lateinit var dialogText: EditText
        lateinit var btn_dialog_single_choice: Button
        lateinit var btn_dialog_multi_choice: Button
        lateinit var btn_dialog_set_routine: Button
        lateinit var et_dialog_sel_date: EditText

        if (cmd == 1) {
            val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
            et_dialog_sel_date = dialogView.findViewById(R.id.et_dialog_sel_date)
            dialogView.findViewById<EditText>(R.id.btn_dialog_single_choice)
            dialogView.findViewById<EditText>(R.id.btn_dialog_multi_choice)
            btn_dialog_single_choice = dialogView.findViewById(R.id.btn_dialog_single_choice)
            btn_dialog_multi_choice = dialogView.findViewById(R.id.btn_dialog_multi_choice)
            btn_dialog_set_routine = dialogView.findViewById(R.id.btn_dialog_set_routine)
            et_dialog_sel_date.setText(today_date)
            set_arr = ArrayList()
            btn_dialog_single_choice.setOnClickListener {
                var selected_date = today_date
                val year = cal.get(Calendar.YEAR)
                val month = cal.get(Calendar.MONTH)
                val day = cal.get(Calendar.DAY_OF_MONTH)
                set_arr = ArrayList()
                set_arr.add(today_date)
                val dpd = DatePickerDialog(
                    activity!!,
                    DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                        set_arr = ArrayList()
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        selected_date = (year.toString() + "-")
                        if (monthOfYear + 1 < 10) {
                            selected_date += "0"
                        }
                        selected_date += ((monthOfYear + 1).toString() + "-")
                        if (dayOfMonth < 10) {
                            selected_date += "0"
                        }
                        selected_date += dayOfMonth.toString()
                        et_dialog_sel_date.setText(selected_date)
                        set_arr.add(selected_date)
                        cal.time = Date()
                    },
                    year,
                    month,
                    day
                )
                dpd.show()
            }
            btn_dialog_multi_choice.setOnClickListener {
                set_arr = ArrayList()
                val dlg = activity?.let { it1 -> DialogPlanMultiAdder(it1) }
                dlg?.start(set_arr, et_dialog_sel_date)
            }
            btn_dialog_set_routine.setOnClickListener {
                set_arr = ArrayList()
                val dlg = activity?.let { it1 -> DialogPlanRoutineAdder(it1, activity!!, cal) }
                dlg?.start(set_arr)
                Log.e("Today_set_arr", set_arr.toString())
            }

            val builder = AlertDialog.Builder(activity!!)
            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    if (dialogView.et_dialog_contents.text.length == 0) {
                        Toast.makeText(context, "일정을 입력해 주세요.", LENGTH_LONG).show()
                    } else {
                        if (cmd == 1) {
                            Log.e("builder date_arr : ", set_arr.toString())
                            if (set_arr.size == 0) {
                                set_arr.add(today_date)
                            }
                            insertTodoData(dialogView, today_db, set_arr, todo.gid)
                        } else {
                            updateTodoDate(dialogView, todo, today_db)
                        }
                    }
                }
                .setNegativeButton("취소") { dialogInterface, i ->
                }
                .show()
        } else {
            val dialogView = layoutInflater.inflate(R.layout.custom_dialog_modify, null)
            dialogText = dialogView.findViewById(R.id.et_dialog_contents)
            var dialogDate = dialogView.findViewById<EditText>(R.id.et_dialog_date)
            dialogDate.setText(today_date)
            dialogDate.setOnClickListener {
                var selected_date = today_date
                val year = cal.get(Calendar.YEAR)
                val month = cal.get(Calendar.MONTH)
                val day = cal.get(Calendar.DAY_OF_MONTH)
                val dpd = DatePickerDialog(
                    activity!!,
                    DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        selected_date = todo.day
                        selected_date = (year.toString() + "-")
                        if (monthOfYear + 1 < 10) {
                            selected_date += "0"
                        }
                        selected_date += ((monthOfYear + 1).toString() + "-")
                        if (dayOfMonth < 10) {
                            selected_date += "0"
                        }
                        selected_date += dayOfMonth.toString()
                        dialogDate.setText(selected_date)
                        cal.time = Date()
                    },
                    year,
                    month,
                    day
                )
                dpd.show()
            }
            dialogText.setText(todo.contents)
            val builder = AlertDialog.Builder(activity!!)
            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    if (cmd == 1) {
                        Log.e("builder date_arr : ", set_arr.toString())
                        insertTodoData(dialogView, today_db, set_arr, todo.gid)
                    } else {
                        updateTodoDate(dialogView, todo, today_db)
                    }
                }
                .setNegativeButton("취소") { dialogInterface, i ->
                }
                .show()
        }
    }

    fun updateTodoDate(dialogView: View, todo: Todo, today_db: AppDatabase) {
        val dialogText = dialogView.findViewById<EditText>(R.id.et_dialog_contents)
        val dialogDate = dialogView.findViewById<EditText>(R.id.et_dialog_date)
        todo.contents = dialogText.text.toString()

        todo.day = dialogDate.text.toString()
        todo.gid = today_db.todoDao().getNewGid()
        if (today_db != null) {
            today_db.todoDao().update(todo)
            todayRecyclerView.adapter =
                TodayRecyclerAdapter(
                    activity!!,
                    this,
                    today_date,
                    ArrayList(today_db.todoDao().getDayList(todo.day))
                ) { item ->
                }
        }
        tv_toolbar.setText(todo.day)
        cal.set(Calendar.YEAR, todo.day.subSequence(0, 4).toString().toInt())
        cal.set(Calendar.MONTH, todo.day.subSequence(5, 7).toString().toInt() - 1)
        cal.set(Calendar.DAY_OF_MONTH, todo.day.subSequence(8, 10).toString().toInt())

    }

    fun insertTodoData(
        dialogView: View,
        today_db: AppDatabase,
        date_arr: ArrayList<String>,
        gid: Long
    ) {
        if (date_arr.size == 0)
            return
        date_arr.sort()
        val dialogText = dialogView.findViewById<EditText>(R.id.et_dialog_contents)
        var selected_date = date_arr.get(0)
        Log.e("insert date_arr : ", date_arr.toString())
        if (today_db != null) {
            for (i in date_arr) {
                today_db.todoDao().insert(
                    Todo(
                        dialogText.text.toString(),
                        false,
                        i,
                        gid
                    )
                )
            }
            todayRecyclerView.adapter =
                TodayRecyclerAdapter(
                    activity!!,
                    this,
                    today_date,
                    ArrayList(today_db.todoDao().getDayList(selected_date))
                ) { item ->
                }
            cal.set(Calendar.YEAR, selected_date.subSequence(0, 4).toString().toInt())
            cal.set(Calendar.MONTH, selected_date.subSequence(5, 7).toString().toInt() - 1)
            cal.set(Calendar.DAY_OF_MONTH, selected_date.subSequence(8, 10).toString().toInt())
            tv_toolbar.setText(selected_date)
        }
    }
}
