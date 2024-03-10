package com.example.workreminder.usecase

import java.text.SimpleDateFormat
import java.util.*

class TimeAdapter() {
    companion object {

        private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault())

        fun getTime(time: String): String {
            val date = simpleDateFormat.parse(time)
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            return timeFormat.format(date)
        }

        fun getDate(time: String): String {
            val date = simpleDateFormat.parse(time)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return dateFormat.format(date)
        }

        fun getTimeMillis(time: String): Long {
            val date = simpleDateFormat.parse(time)
            return date.time
        }

        fun getTimeString(calendar: Calendar): String {
            return simpleDateFormat.format(calendar.time)
        }
    }
}