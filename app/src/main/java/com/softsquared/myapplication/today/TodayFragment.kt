package com.softsquared.myapplication.today

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.myapplication.MainViewModel
import com.softsquared.myapplication.R
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
    val todayFragment = this
    var list = ArrayList<Todo>()
    val cal: Calendar
    val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    var today_date: String
    val arr_day = listOf("일", "월", "화", "수", "목", "금", "토")
    val viewModel: MainViewModel

    constructor(vm: MainViewModel) {
        viewModel = vm
        cal = viewModel.getCalendar()
        today_date = df.format(cal.time)
    }

    fun loadView() {
        tv_toolbar.setText(df.format(cal.time))
        today_date = df.format(cal.time)
        tv_fragment_today_day.setText(arr_day[cal.get(Calendar.DAY_OF_WEEK) - 1])
        list = ArrayList(viewModel.getDayList(df.format(cal.time)))
        if (list.size == 0) {
            tv_empty_today_item.visibility = View.VISIBLE
        } else {
            tv_empty_today_item.visibility = View.GONE
        }
        rv_today_list.adapter = TodayRecyclerAdapter(
            activity!!,
            this,
            today_date,
            list,
            viewModel = viewModel
        ) { item ->
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            loadView()
        }
        ibtn_next_arrow.setOnClickListener {
            cal.add(Calendar.DATE, +1)
            loadView()
        }
        fragment_today_today_button.setOnClickListener {
            today_date = df.format(Date())
            cal.time = Date()
            loadView()
        }
        btn_add.setOnClickListener {
            showAlertDialog(1, Todo("-", false, "-", viewModel.getNewGid()))
        }
    }

    override fun onResume() {
        super.onResume()
        today_date = df.format(cal.time)
        list = ArrayList(viewModel.getDayList(today_date))
        val lm = LinearLayoutManager(activity)
        if (list.size == 0) {
            tv_empty_today_item.visibility = View.VISIBLE
        } else {
            tv_empty_today_item.visibility = View.GONE
        }
        rv_today_list.layoutManager = lm
        val adapter =
            TodayRecyclerAdapter(
                activity!!,
                this,
                today_date,
                list,
                viewModel = viewModel
            ) { item ->
            }
        rv_today_list.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    fun showAlertDialog(cmd: Int, todo: Todo) {
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
                            insertTodoData(dialogView, set_arr, todo.gid)
                        } else {
                            updateTodoDate(dialogView, todo)
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
                        insertTodoData(dialogView, set_arr, todo.gid)
                    } else {
                        updateTodoDate(dialogView, todo)
                    }
                }
                .setNegativeButton("취소") { dialogInterface, i ->
                }
                .show()
        }
    }

    fun updateTodoDate(dialogView: View, todo: Todo) {
        val dialogText = dialogView.findViewById<EditText>(R.id.et_dialog_contents)
        val dialogDate = dialogView.findViewById<EditText>(R.id.et_dialog_date)

        todo.contents = dialogText.text.toString()
        todo.day = dialogDate.text.toString()

        // 비동기 처리, 수정은 resume시켜주지 않아도 잘 작동.. 둘의 차이점을 찾으려 하다가 다른 작업을 먼저 하기로 일단은 정했습니다!
        lifecycleScope.launch(Dispatchers.IO) {
            todo.gid = viewModel.getNewGid()
            viewModel.update(todo)
            list = ArrayList(viewModel.getDayList(df.format(cal.time)))
            if (list.size == 0) {
                tv_empty_today_item.visibility = View.VISIBLE
            } else {
                tv_empty_today_item.visibility = View.GONE
            }
            rv_today_list.adapter =
                TodayRecyclerAdapter(
                    activity!!,
                    todayFragment,
                    today_date,
                    list,
                    viewModel = viewModel
                ) { item ->
                }
        }
        tv_toolbar.setText(todo.day)

        cal.set(Calendar.YEAR, todo.day.subSequence(0, 4).toString().toInt())
        cal.set(Calendar.MONTH, todo.day.subSequence(5, 7).toString().toInt() - 1)
        cal.set(Calendar.DAY_OF_MONTH, todo.day.subSequence(8, 10).toString().toInt())
        tv_fragment_today_day.setText(arr_day[cal.get(Calendar.DAY_OF_WEEK) - 1])
    }

    fun insertTodoData(
        dialogView: View,
        date_arr: ArrayList<String>,
        gid: Long
    ) {
        if (date_arr.size == 0)
            return
        date_arr.sort()
        val dialogText = dialogView.findViewById<EditText>(R.id.et_dialog_contents)
        var selected_date = date_arr.get(0)
        for (i in date_arr) {
            viewModel.insert(
                Todo(
                    dialogText.text.toString(),
                    false,
                    i,
                    gid
                )
            )
        }

        cal.set(Calendar.YEAR, selected_date.subSequence(0, 4).toString().toInt())
        cal.set(Calendar.MONTH, selected_date.subSequence(5, 7).toString().toInt() - 1)
        cal.set(Calendar.DAY_OF_MONTH, selected_date.subSequence(8, 10).toString().toInt())
        tv_toolbar.setText(selected_date)
        loadView()
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
            loadView()
        }

        open fun onSwipeLeft() {
            cal.add(Calendar.DATE, +1)
            loadView()
        }
    }
}
