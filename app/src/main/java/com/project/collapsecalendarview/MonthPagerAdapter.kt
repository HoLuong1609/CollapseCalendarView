package com.project.collapsecalendarview

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wefika.calendar.CollapseCalendarView.OnDateSelect

class MonthPagerAdapter(
    fa: FragmentActivity,
    private var mListener: OnDateSelect
) : FragmentStateAdapter(fa) {

    private val fragments = arrayListOf<Fragment>()

    init {
        fragments.add(CalendarFragment.newInstance("", ""))
        fragments.add(CalendarFragment.newInstance("", ""))
        fragments.add(CalendarFragment.newInstance("", ""))
        fragments.add(CalendarFragment.newInstance("", ""))
        fragments.add(CalendarFragment.newInstance("", ""))
        fragments.add(CalendarFragment.newInstance("", ""))
        fragments.add(CalendarFragment.newInstance("", ""))
        fragments.add(CalendarFragment.newInstance("", ""))
        fragments.add(CalendarFragment.newInstance("", ""))
        fragments.add(CalendarFragment.newInstance("", ""))
        fragments.add(CalendarFragment.newInstance("", ""))
    }

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]

}