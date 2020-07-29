package com.softsquared.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.softsquared.myapplication.month.MonthFragment
import com.softsquared.myapplication.today.TodayFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    var todayFragment : TodayFragment? = null
    var weekFragment : WeekFragment? = null
    var monthFragment : MonthFragment? = null
    var transaction = supportFragmentManager.beginTransaction()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        transaction.add(R.id.main_frame_layout, todayFragment)
//        transaction.add(R.id.main_frame_layout, weekFragment)
//        transaction.add(R.id.main_frame_layout, monthFragment)

        bn_bottom_navigation_view.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )
        bn_bottom_navigation_view.selectedItemId = R.id.bni_today
    }
    fun reloadTodayFragment(todayFragment: TodayFragment){
        transaction = supportFragmentManager.beginTransaction()
        this.todayFragment?.let { transaction.remove(it) }
        this.todayFragment = todayFragment
        transaction.add(R.id.main_frame_layout, this.todayFragment!!).commit()
        bn_bottom_navigation_view.selectedItemId = R.id.bni_today
    }
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            transaction = supportFragmentManager.beginTransaction()
            when (menuItem.itemId) {
                R.id.bni_today -> {
                    if(todayFragment != null){
                        Log.e("todayFragment", "is not null")
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.show(todayFragment!!).commit()
                    }else{
                        Log.e("todayFragment", "is null")
                        todayFragment = TodayFragment()
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
                    if(weekFragment != null){
                        transaction = supportFragmentManager.beginTransaction()
                        transaction.show(weekFragment!!).commit()
                    }else{
                        weekFragment = WeekFragment()
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
                    if(monthFragment != null){
                        transaction = supportFragmentManager.beginTransaction()
                        monthFragment!!.reloadView()
                        transaction.show(monthFragment!!).commit()
                    }else{
                        monthFragment = MonthFragment(this)
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