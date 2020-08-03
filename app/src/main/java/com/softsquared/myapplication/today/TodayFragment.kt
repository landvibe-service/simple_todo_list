package com.softsquared.myapplication.today

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.myapplication.BaseFragment
import com.softsquared.myapplication.R
import com.softsquared.myapplication.db.AppDatabase
import com.softsquared.myapplication.db.Todo
import com.softsquared.myapplication.month.BaseCalendar
import kotlinx.android.synthetic.main.fragment_today.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TodayFragment : BaseFragment {
    lateinit var todayRecyclerView: RecyclerView

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
//    constructor(curCal: Calendar) {
//        var curDay = df.format(curCal.time)
//        Log.e("constructor 1개 : ", curDay)
//        today_date = curDay
//    }
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

        Log.e("TodayOnCreateView", today_date)
        tv_toolbar.setText(today_date)
        tv_fragment_today_day.setText(arr_day[cal.get(Calendar.DAY_OF_WEEK) - 1])


        if (today_db != null) {
            list = ArrayList(today_db.todoDao().getDayList(today_date))
        }
        val adapter =
            TodayRecyclerAdapter(activity!!, this, today_date, list) { item ->
            }

        // adapter 할당 to rv
        todayRecyclerView.adapter = adapter
        val lm = LinearLayoutManager(activity)
        todayRecyclerView.layoutManager = lm


        rootView.findViewById<ImageButton>(R.id.ibtn_prev_arrow).setOnClickListener {
            var n_cal = cal
//            n_cal.add(Calendar.DATE, -1)
            cal.add(Calendar.DATE, -1)
            Log.e("cal in button", df.format(cal.time))

            if (today_db != null) {
//                loadView(n_cal, today_db)
                loadView(cal, today_db)
            }
        }
        rootView.findViewById<ImageButton>(R.id.ibtn_next_arrow).setOnClickListener {
            var n_cal = cal
//            n_cal.add(Calendar.DATE, +1)
            cal.add(Calendar.DATE, +1)
            Log.e("cal in button", df.format(cal.time))
            if (today_db != null) {
//                loadView(n_cal, today_db)
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
                showAlertDialog(today_db, 1, Todo("-", false, "-", -1))
            }
        }
        return rootView
    }

    fun showAlertDialog(today_db: AppDatabase, cmd: Int, todo: Todo) {
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
        val dialogText = dialogView.findViewById<EditText>(R.id.et_dialog_contents)
        val dialogDate = dialogView.findViewById<EditText>(R.id.et_dialog_date)
        if (cmd == 2) {
            dialogText.setText(todo.contents)
            dialogDate.setText(todo.day)
        }

        dialogDate.setText(today_date)
        var selected_date = today_date
        dialogDate.setOnClickListener {
            // 여기서 다이얼로그 띄우기
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dlg = activity?.let { it1 -> DialogPlanAdder(it1) }
            dlg?.start()


//            val dpd = DatePickerDialog(
//                activity!!,
//                DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
//                    cal.set(Calendar.YEAR, year)
//                    cal.set(Calendar.MONTH, monthOfYear)
//                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//
//                    dialogDate.setText(df.format(cal.time))
//                    selected_date = dialogDate.text.toString()
//                    cal.time = Date()
//                },
//                year,
//                month,
//                day
//            )
//            dpd.show()

        }
        val builder = AlertDialog.Builder(activity!!)
        builder.setView(dialogView)
            .setPositiveButton("확인") { dialogInterface, i ->

                Log.e("dialog", "확인 클릭")
                if (cmd == 1) {
                    insertTodoData(dialogView, today_db)
                } else {
                    updateTodoDate(dialogView, todo, today_db)
                }
            }
            .setNegativeButton("취소") { dialogInterface, i ->
            }
            .show()
    }

    fun updateTodoDate(dialogView: View, todo: Todo, today_db: AppDatabase) {
        val dialogText = dialogView.findViewById<EditText>(R.id.et_dialog_contents)
        val dialogDate = dialogView.findViewById<EditText>(R.id.et_dialog_date)
        todo.contents = dialogText.text.toString()
        todo.day = dialogDate.text.toString()
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

    fun insertTodoData(dialogView: View, today_db: AppDatabase) {
        val dialogText = dialogView.findViewById<EditText>(R.id.et_dialog_contents)
        val dialogDate = dialogView.findViewById<EditText>(R.id.et_dialog_date)
        Log.e("insert Function", "aaa")
        var selected_date = dialogDate.text.toString()
        Log.e("null?, content", dialogText.text.toString() + "string")
        Log.e("null?, selected_date", selected_date)
        if (today_db != null) {
            Log.e("selected_date", selected_date)
            today_db.todoDao().insert(
                Todo(
                    dialogText.text.toString(),
                    false,
                    dialogDate.text.toString(),
                    -1
                )
            )
            todayRecyclerView.adapter =
                TodayRecyclerAdapter(
                    activity!!,
                    this,
                    today_date,
                    ArrayList(today_db.todoDao().getDayList(selected_date))
                ) { item ->
                }

            tv_toolbar.setText(today_date)
            cal.set(Calendar.YEAR, selected_date.subSequence(0, 4).toString().toInt())
            cal.set(Calendar.MONTH, selected_date.subSequence(5, 7).toString().toInt() - 1)
            cal.set(Calendar.DAY_OF_MONTH, selected_date.subSequence(8, 10).toString().toInt())
        }
    }
}
