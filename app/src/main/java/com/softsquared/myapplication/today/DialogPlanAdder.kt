package com.softsquared.myapplication.today

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.myapplication.R
import com.softsquared.myapplication.month.BaseCalendar
import com.softsquared.myapplication.month.MonthRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_month.*
import java.text.SimpleDateFormat
import java.util.*

class DialogPlanAdder(context: Context){
    private val dlg = Dialog(context)
    private lateinit var lblDesc : TextView
    private lateinit var btnOK : Button
    private lateinit var btnCancel : Button
    private lateinit var rv_dialog_calendar: RecyclerView
    private lateinit var tv_dialog_current_month: TextView
    lateinit var scheduleRecyclerViewAdapter: DialogRecyclerAdapter
    lateinit var ibtn_dialog_prev: ImageButton
    lateinit var ibtn_dialog_next: ImageButton
    val context = context
    val set_data = mutableSetOf<String>()

    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_calendar)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        rv_dialog_calendar = dlg.findViewById(R.id.rv_dialog_calendar)
        btnOK = dlg.findViewById(R.id.btn_dialog_calendar_ok)
        btnCancel = dlg.findViewById(R.id.btn_dialog_calendar_cancle)
        tv_dialog_current_month = dlg.findViewById(R.id.tv_dialog_current_month)
        ibtn_dialog_prev = dlg.findViewById(R.id.ibtn_dialog_prev)
        ibtn_dialog_next = dlg.findViewById(R.id.ibtn_dialog_next)
        initView()

        btnOK.setOnClickListener {
            //TODO: 부모 액티비티로 내용을 돌려주기 위해 작성할 코드
            Log.e("adder에서 set", set_data.toString())


            dlg.dismiss()
        }

        btnCancel.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }

    fun initView() {
        scheduleRecyclerViewAdapter = DialogRecyclerAdapter(this, set_data)
        rv_dialog_calendar.layoutManager = GridLayoutManager(context, BaseCalendar.DAYS_OF_WEEK)
        rv_dialog_calendar.adapter = scheduleRecyclerViewAdapter
        //rv_dialog_calendar.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        //rv_dialog_calendar.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        ibtn_dialog_prev.setOnClickListener {
            scheduleRecyclerViewAdapter.changeToPrevMonth()
        }
        ibtn_dialog_next.setOnClickListener {
            scheduleRecyclerViewAdapter.changeToNextMonth()
        }
    }

    fun refreshCurrentMonth(calendar: Calendar) : String{
        val sdf = SimpleDateFormat("yyyy-MM", Locale.KOREAN)
        tv_dialog_current_month.text = sdf.format(calendar.time)
        Log.e("refreshCurrentMonth", tv_dialog_current_month.text.toString())
        return tv_dialog_current_month.text.toString()
    }
}