package com.softsquared.myapplication.month

import java.text.SimpleDateFormat
import java.util.*




/**
 * Created by WoochanLee on 25/03/2019.
 */
class BaseCalendar {

    companion object {
        const val DAYS_OF_WEEK = 7
        const val LOW_OF_CALENDAR = 6
    }

    val calendar = Calendar.getInstance()
    var prevMonthTailOffset = 0
    var nextMonthHeadOffset = 0
    var currentMonthMaxDate = 0
    val df = SimpleDateFormat("yyyy-mm-dd", Locale.KOREA)

    var data = arrayListOf<Int>()


    init {
        calendar.time = Date()
    }

    /**
     * Init calendar.
     */
    fun initBaseCalendar(refreshCallback: (Calendar) -> Unit) {
        makeMonthDate(refreshCallback)
    }

    /**
     * Change to prev month.
     */
    fun changeToCurMonth(refreshCallback: (Calendar) -> Unit){
        calendar.time = Date()
        makeMonthDate(refreshCallback)
        calendar.time = Date()
    }

    fun changeToPrevMonth(refreshCallback: (Calendar) -> Unit) {
        if(calendar.get(Calendar.MONTH) == 0){
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1)
            calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        }else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1)
        }
        makeMonthDate(refreshCallback)
    }
    fun getPrevMonth() : String{
        var ret = Calendar.getInstance()
        if(calendar.get(Calendar.MONTH) == 0){
            ret.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1)
            ret.set(Calendar.MONTH, Calendar.DECEMBER)
        }else {
            ret.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1)
        }
        val sdf = SimpleDateFormat("yyyy-MM", Locale.KOREAN)
        return sdf.format(ret.time)
    }
    /**
     * Change to next month.
     */
    fun changeToNextMonth(refreshCallback: (Calendar) -> Unit) {
        if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER){
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1)
            calendar.set(Calendar.MONTH, 0)
        }else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1)
        }
        makeMonthDate(refreshCallback)
    }
    fun getNextMonth() : String{
        var ret = Calendar.getInstance()
        if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER){
            ret.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1)
            ret.set(Calendar.MONTH, 0)
        }else {
            ret.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1)
        }
        val sdf = SimpleDateFormat("yyyy-MM", Locale.KOREAN)
        return sdf.format(ret.time)
    }


    /**
     * make month date.
     */
    private fun makeMonthDate(refreshCallback: (Calendar) -> Unit) {
        data.clear()
        calendar.set(Calendar.DATE, 1)
        currentMonthMaxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        prevMonthTailOffset = calendar.get(Calendar.DAY_OF_WEEK) - 1
        makePrevMonthTail(calendar.clone() as Calendar)
        makeCurrentMonth(calendar)
        nextMonthHeadOffset = LOW_OF_CALENDAR * DAYS_OF_WEEK - (prevMonthTailOffset + currentMonthMaxDate)
        makeNextMonthHead()
        refreshCallback(calendar)
    }

    /**
     * Generate data for the last month displayed before the first day of the current calendar.
     */
    private fun makePrevMonthTail(calendar: Calendar) {
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1)
        val maxDate = calendar.getActualMaximum(Calendar.DATE)
        var maxOffsetDate = maxDate - prevMonthTailOffset
        for (i in 1..prevMonthTailOffset) data.add(++maxOffsetDate)
    }

    /**
     * Generate data for the current calendar.
     */
    private fun makeCurrentMonth(calendar: Calendar) {
        for (i in 1..calendar.getActualMaximum(Calendar.DATE)) data.add(i)
    }

    /**
     * Generate data for the next month displayed before the last day of the current calendar.
     */
    private fun makeNextMonthHead() {
        var date = 1

        for (i in 1..nextMonthHeadOffset) data.add(date++)
    }
}