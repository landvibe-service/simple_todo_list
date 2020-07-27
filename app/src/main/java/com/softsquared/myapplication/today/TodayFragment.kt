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
import com.softsquared.myapplication.*
import com.softsquared.myapplication.db.AppDatabase
import com.softsquared.myapplication.db.Todo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_today.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TodayFragment : BaseFragment {
    lateinit var todayRecyclerView: RecyclerView
    var list = ArrayList<Todo>()
    val cal = Calendar.getInstance()
    val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    lateinit var today_date: String


    constructor(){
        today_date = df.format(cal.time)
    }
    constructor(curDay: String){
        Log.e("curDay : ", curDay)
        today_date = curDay
    }

    fun loadView(new_date: String, today_db: AppDatabase) {
        // 날짜 변경 현재 날짜 변경 리사이클러뷰 리로드
        tv_toolbar.setText(new_date)
        if (today_db != null) {
            todayRecyclerView.adapter = TodayRecyclerAdapter(
                activity!!,
                ArrayList(today_db.todoDao().getDayList(new_date))
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
        todayRecyclerView = rootView.findViewById(R.id.rv_today_list)
        cal.time = Date()
        Log.e("today_date : ", today_date)
        tv_toolbar.setText(today_date)


        if (today_db != null) {
            list = ArrayList(today_db.todoDao().getDayList(today_date))
        }
        val adapter =
            TodayRecyclerAdapter(activity!!, list) { item ->
            }

        // adapter 할당 to rv
        todayRecyclerView.adapter = adapter
        val lm = LinearLayoutManager(activity)
        todayRecyclerView.layoutManager = lm


        rootView.findViewById<ImageButton>(R.id.ibtn_prev_arrow).setOnClickListener {
            var n_cal = cal
            n_cal.add(Calendar.DATE, -1)
            if (today_db != null) {
                loadView(df.format(n_cal.time), today_db)
            }
        }
        rootView.findViewById<ImageButton>(R.id.ibtn_next_arrow).setOnClickListener {
            var n_cal = cal
            n_cal.add(Calendar.DATE, +1)
            if (today_db != null) {
                loadView(df.format(n_cal.time), today_db)
            }
        }


        rootView.findViewById<ImageButton>(R.id.btn_add).setOnClickListener {
            val builder = AlertDialog.Builder(activity!!)
            val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
            val dialogText = dialogView.findViewById<EditText>(R.id.et_dialog_contents)
            val dialogDate = dialogView.findViewById<EditText>(R.id.et_dialog_date)
            val btn_sel_date = dialogView.findViewById<Button>(R.id.btn_sel_date)
            btn_sel_date.setOnClickListener {
                val year = cal.get(Calendar.YEAR)
                val month = cal.get(Calendar.MONTH)
                val day = cal.get(Calendar.DAY_OF_MONTH)
                val dpd = DatePickerDialog(
                    activity!!,
                    DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        dialogDate.setText(df.format(cal.time))
                        cal.time = Date()
                    },
                    year,
                    month,
                    day
                )
                dpd.show()
            }
            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    if (today_db != null) {
                        today_db.todoDao().insert(
                            Todo(
                                dialogText.text.toString(),
                                false,
                                dialogDate.text.toString()
                            )
                        )
                        todayRecyclerView.adapter =
                            TodayRecyclerAdapter(
                                activity!!,
                                ArrayList(today_db.todoDao().getDayList(today_date))
                            ) { item ->
                            }
                    }
                }
                .setNegativeButton("취소") { dialogInterface, i ->
                }
                .show()
        }
        return rootView
    }
}
