package com.project.collapsecalendarview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wefika.calendar.manager.CalendarManager
import kotlinx.android.synthetic.main.fragment_calendar.calendar

class CalendarFragment(private var mCalendarManager: CalendarManager) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar.init(mCalendarManager)
        Log.e("LuongHH", "CalendarFragment - onViewCreated")
    }

    fun setCalendarManager(calendarManager: CalendarManager) {
        mCalendarManager = calendarManager
        if (isAdded) {
            calendar.init(mCalendarManager)
        }
    }
}