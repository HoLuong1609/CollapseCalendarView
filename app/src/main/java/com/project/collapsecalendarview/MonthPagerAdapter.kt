package com.project.collapsecalendarview

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.wefika.calendar.CollapseCalendarView.OnDateSelect
import com.wefika.calendar.manager.CalendarManager
import org.joda.time.LocalDate

class MonthPagerAdapter(
    fa: FragmentActivity,
    private val viewPager: ViewPager2,
    private var mListener: OnDateSelect
) : FragmentStateAdapter(fa), CalendarManager.StateChangeListener {

    private val fragments = arrayListOf<CalendarFragment>()
    private val managerList = arrayListOf<CalendarManager>()

    init {
        val manager = CalendarManager(
            LocalDate.now(),
            CalendarManager.State.MONTH,
            LocalDate.now().minusYears(1),
            LocalDate.now().plusYears(1),
            this
        )
        managerList.addAll(generateManagerList(manager))
        for (calendarManager in managerList) {
            fragments.add(CalendarFragment(calendarManager))
        }
    }

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        val fragment = fragments[position]
        fragment.setCalendarManager(managerList[position])
        return fragment
    }

    override fun onStateChanged(state: CalendarManager.State) {
        Log.e("LuongHH", "onStateChanged - $state")
        val currentManager = managerList[viewPager.currentItem]
        currentManager.state = state
        managerList.clear()
        managerList.addAll(generateManagerList(currentManager.copy()))      // need to call constructor function of calendar manager
        for (i in 0 until managerList.size) {
            fragments[i].setCalendarManager(managerList[i])
        }
    }

    private fun generateManagerList(manager: CalendarManager): List<CalendarManager> {
        val list = arrayListOf<CalendarManager>()
        var temp = manager.copy()
        for (i in 0 until 10) {
            temp.prev()
            temp = temp.copy()
            list.add(temp)
        }
        list.reverse()
        list.add(manager)
        temp = manager.copy()
        for (i in 0 until 10) {
            temp.next()
            temp = temp.copy()
            list.add(temp)
        }
        return list
    }
}