package com.softsquared.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.softsquared.myapplication.month.MonthFragment
import com.softsquared.myapplication.today.TodayFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bn_bottom_navigation_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bn_bottom_navigation_view.selectedItemId = R.id.bni_today

    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.bni_today -> {
                val fragment = TodayFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.bni_day_list -> {
                val fragment = WeekFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.bni_month -> {
                val fragment = MonthFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}

