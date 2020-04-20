package com.doodhbhandaarvendor.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.doodhbhandaarvendor.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.util.*

class OrderDetailCalenderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail_calender)
//        val materialCalenderView= findViewById<MaterialCalendarView>(R.id.calendarView)
//        materialCalenderView.state().edit().setFirstDayOfWeek(Calendar.MONDAY)
//            .setMinimumDate(CalendarDay.from(2020, 1, 1))
//            .setMaximumDate(CalendarDay.from(2200, 12, 31))
//            .setCalendarDisplayMode(CalendarMode.WEEKS)
//            .commit();

    }
}
