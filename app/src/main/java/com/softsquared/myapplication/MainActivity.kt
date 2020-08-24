package com.softsquared.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.softsquared.myapplication.month.MonthFragment
import com.softsquared.myapplication.today.TodayFragment
import com.softsquared.myapplication.week.WeekFragment
import kotlinx.android.synthetic.main.activity_main.*

//뭔가 코드 정리할수 있을 것 같은데 한번 해보고 다음에 같이 보자! !!, ? 이런걸 조금 줄일 방법도 찾아보고
class MainActivity : AppCompatActivity() {
    var todayFragment: TodayFragment? = null
    var weekFragment: WeekFragment? = null
    var monthFragment: MonthFragment? = null
    var transaction = supportFragmentManager.beginTransaction()
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        viewModel.setBottomNavigationView(bn_bottom_navigation_view)
        viewModel.getBottomNavigationView().setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )
        viewModel.getBottomNavigationView().selectedItemId = R.id.bni_today
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            transaction = supportFragmentManager.beginTransaction()
            when (menuItem.itemId) {
                R.id.bni_today -> {
                    if (todayFragment != null) {
                        transaction = supportFragmentManager.beginTransaction()
                        todayFragment?.loadView()
                        transaction.show(todayFragment!!).commit()
                    } else {
                        todayFragment = TodayFragment(viewModel)
                        transaction.add(R.id.main_frame_layout, todayFragment!!)
                        transaction.show(todayFragment!!).commit()
                    }
                    weekFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(weekFragment!!).commit()
                    }
                    monthFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(monthFragment!!).commit()
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bni_day_list -> {
                    if (weekFragment != null) {
                        transaction = supportFragmentManager.beginTransaction()
                        weekFragment!!.reloadView()
                        transaction.show(weekFragment!!).commit()
                    } else {
                        weekFragment = WeekFragment(viewModel)
                        transaction.add(R.id.main_frame_layout, weekFragment!!)
                        transaction.show(weekFragment!!).commit()
                    }
                    todayFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(todayFragment!!).commit()
                    }
                    monthFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(monthFragment!!).commit()
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bni_month -> {
                    if (monthFragment != null) {
                        transaction = supportFragmentManager.beginTransaction()
                        monthFragment!!.reloadView()
                        transaction.show(monthFragment!!).commit()
                    } else {
                        monthFragment = MonthFragment(viewModel)
                        transaction.add(R.id.main_frame_layout, monthFragment!!)
                        transaction.show(monthFragment!!).commit()
                    }
                    todayFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(todayFragment!!).commit()
                    }
                    weekFragment?.let {
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.hide(weekFragment!!).commit()
                    }

                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}