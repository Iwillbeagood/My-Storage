package com.example.mystorage.utils

import java.text.SimpleDateFormat
import java.util.*

object CalculateDurationUtil {
    fun calculateDuration(dateTime: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("UTC")
        val date = format.parse(dateTime)
        val currentDate = Date()

        val durationInMillis = currentDate.time - date!!.time
        val minutesInMillis = 60 * 1000
        val hoursInMillis = 60 * minutesInMillis
        val daysInMillis = 24 * hoursInMillis

        val days = durationInMillis / daysInMillis
        val hours = (durationInMillis - days * daysInMillis) / hoursInMillis

        return "물건 사용을 완료한지\n ${days}일 ${hours}시간이 지났습니다"
    }
}