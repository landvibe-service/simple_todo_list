package com.softsquared.myapplication.month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.softsquared.myapplication.BaseFragment
import com.softsquared.myapplication.MainActivity
import com.softsquared.myapplication.MainViewModel
import com.softsquared.myapplication.R
import kotlinx.android.synthetic.main.fragment_month.*
import java.text.SimpleDateFormat
import java.util.*

//달력은 잘만들었고 잘노출한거같아
//mainactivity를 가지고 있는건 좋지 않은 것 같은데.. mainActivity를 이렇게 접근하지 않고,
//adapter에서 fragment를 가지고 있는것도 좋은 구조가 아닌 것 같고.
//fragment에서 adapter를 생성할때 click하면 할 일을 넘겨주는 쪽이 좋은거같고
//fragment에서 activity 접근할 때는 fragment.activity as MainActivity 이렇게 될 것 같은데 한번 확인해보고 모르면 물어봐줘~
class MonthFragment : BaseFragment {
    constructor(mainActivity: MainActivity) {
        this.mainActivity = mainActivity
    }

    lateinit var monthRecyclerAdapter: MonthRecyclerAdapter
    val mainActivity: MainActivity
    lateinit var scheduleRecyclerViewAdapter: MonthRecyclerAdapter
    lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_month, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        fragment_month_today_button.setOnClickListener {
            initView()
        }
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    fun reloadView() {
        //다시 adapter를 설정하는 것보다는 item만 바꿔주고, adapter.notifyDataSetChanged 이거를 호출해 주는게 좋아~
        rv_schedule.adapter?.notifyDataSetChanged()
    }

    fun initView() {
        scheduleRecyclerViewAdapter = MonthRecyclerAdapter(this, viewModel)
        rv_schedule.layoutManager = GridLayoutManager(context, BaseCalendar.DAYS_OF_WEEK)
        rv_schedule.adapter = scheduleRecyclerViewAdapter
        rv_schedule.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.HORIZONTAL
            )
        )
        rv_schedule.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        tv_prev_month.setOnClickListener {
            scheduleRecyclerViewAdapter.changeToPrevMonth()
        }
        tv_next_month.setOnClickListener {
            scheduleRecyclerViewAdapter.changeToNextMonth()
        }
    }

    fun refreshCurrentMonth(calendar: Calendar): String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.KOREAN)
        tv_current_month.text = sdf.format(calendar.time)
        return tv_current_month.text.toString()
    }
}