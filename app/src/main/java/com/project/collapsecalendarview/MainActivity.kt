package com.project.collapsecalendarview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wefika.calendar.manager.CalendarManager
import org.joda.time.LocalDate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manager = CalendarManager(
            LocalDate.now(),
            CalendarManager.State.MONTH,
            LocalDate.now(),
            LocalDate.now().plusYears(1)
        )
        calendar.init(manager)
    }
}