package com.softsquared.myapplication.today

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.myapplication.R
import com.softsquared.myapplication.db.AppDatabase
import com.softsquared.myapplication.db.Todo
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TodayFragment : Fragment {
    lateinit var set_arr: ArrayList<String>
    lateinit var today_db: AppDatabase
    val todayFragment = this
    var list = ArrayList<Todo>()
    var cal = Calendar.getInstance()
    val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    var today_date: String
    val arr_day = listOf("일", "월", "화", "수", "목", "금", "토")
    constructor() {
        today_date = df.format(cal.time)
        cal.time = Date()
    }
    constructor(curDay: String) {
        today_date = curDay
        cal.set(Calendar.YEAR, curDay.subSequence(0, 4).toString().toInt())
        cal.set(Calendar.MONTH, curDay.subSequence(5, 7).toString().toInt() - 1)
        cal.set(Calendar.DAY_OF_MONTH, curDay.subSequence(8, 10).toString().toInt())
    }
    fun loadView(new_cal: Calendar) {
        tv_toolbar.setText(df.format(new_cal.time))
        today_date = df.format(new_cal.time)
        tv_fragment_today_day.setText(arr_day[new_cal.get(Calendar.DAY_OF_WEEK) - 1])
        if (today_db != null) {
            rv_today_list.adapter = TodayRecyclerAdapter(
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
        today_db = AppDatabase.getInstance(activity!!)!!
        val rootView = inflater.inflate(R.layout.fragment_today, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_toolbar.setText(today_date)
        ll_swiper.setOnTouchListener(OnSwipeTouchListener())
        tv_fragment_today_day.setText(arr_day[cal.get(Calendar.DAY_OF_WEEK) - 1])
        ibtn_prev_arrow.setOnClickListener {
            cal.add(Calendar.DATE, -1)
            if (today_db != null) {
                loadView(cal)
            }
        }
        ibtn_next_arrow.setOnClickListener {
            cal.add(Calendar.DATE, +1)
            if (today_db != null) {
                loadView(cal)
            }
        }
        fragment_today_today_button.setOnClickListener {
            if (today_db != null) {
                today_date = df.format(Date())
                cal.time = Date()
                loadView(cal)
            }
        }
        btn_add.setOnClickListener {
            if (today_db != null) {
                showAlertDialog(today_db, 1, Todo("-", false, "-", today_db.todoDao().getNewGid()))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        today_date = df.format(cal.time)
        if (today_db != null) {
            list = ArrayList(today_db.todoDao().getDayList(today_date))
        }
        val adapter =
            TodayRecyclerAdapter(activity!!, this, today_date, list) { item ->
            }
        rv_today_list.adapter = adapter
        adapter.notifyDataSetChanged()
        val lm = LinearLayoutManager(activity)
        rv_today_list.layoutManager = lm
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
            }

            val builder = AlertDialog.Builder(activity!!)
            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    if (dialogView.et_dialog_contents.text.length == 0) {
                        Toast.makeText(context, "일정을 입력해 주세요.", LENGTH_LONG).show()
                    } else {
                        if (cmd == 1) {
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

        // 비동기 처리, 수정은 resume시켜주지 않아도 잘 작동.. 둘의 차이점을 찾으려 하다가 다른 작업을 먼저 하기로 일단은 정했습니다!
        lifecycleScope.launch(Dispatchers.IO){
            todo.gid = today_db.todoDao().getNewGid()
            if (today_db != null) {
                today_db.todoDao().update(todo)
                rv_today_list.adapter =
                    TodayRecyclerAdapter(
                        activity!!,
                        todayFragment,
                        today_date,
                        ArrayList(today_db.todoDao().getDayList(todo.day))
                    ) { item ->
                    }
            }
//            this@TodayFragment.onResume()
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
        if (today_db != null) {
            // 비동기 처리, resume시켜주지 않으면 즉시반영되지 않음.
            lifecycleScope.launch(Dispatchers.IO){
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
                this@TodayFragment.onResume()
            }
            cal.set(Calendar.YEAR, selected_date.subSequence(0, 4).toString().toInt())
            cal.set(Calendar.MONTH, selected_date.subSequence(5, 7).toString().toInt() - 1)
            cal.set(Calendar.DAY_OF_MONTH, selected_date.subSequence(8, 10).toString().toInt())
            tv_toolbar.setText(selected_date)
        }
    }

    inner class OnSwipeTouchListener : View.OnTouchListener {

        private val gestureDetector = GestureDetector(GestureListener())

        fun onTouch(event: MotionEvent): Boolean {
            return gestureDetector.onTouchEvent(event)
        }

        private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
            private val SWIPE_THRESHOLD = 100
            private val SWIPE_VELOCITY_THRESHOLD = 100
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                onTouch(e)
                return true
            }
            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                val result = false
                try {
                    val diffY = e2.y - e1.y
                    val diffX = e2.x - e1.x
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight()
                            } else {
                                onSwipeLeft()
                            }
                        }
                    } else {
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }

                return result
            }
        }

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            return gestureDetector.onTouchEvent(event)
        }

        open fun onSwipeRight() {
            cal.add(Calendar.DATE, -1)
            if (today_db != null) {
                loadView(cal)
            }
        }

        open fun onSwipeLeft() {
            cal.add(Calendar.DATE, +1)
            if (AppDatabase.getInstance(activity!!) != null) {
                today_db?.let { loadView(cal) }
            }
        }
    }
}
