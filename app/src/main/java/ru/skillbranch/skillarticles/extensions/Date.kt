package ru.skillbranch.skillarticles.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.round

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.shortFormat(): String {
    val pattern = if (this.isSameDay(Date())) "HH:mm" else "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.isSameDay(date: Date): Boolean {
    val day1 = this.time / DAY
    val day2 = date.time / DAY
    return day1 == day2
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val difDate = abs(this.time - date.time)
    val message: String
    val isNeg = this.time < date.time

    when (difDate) {
        in 0..1 * SECOND -> message = "только что"
        in 1 * SECOND..45 * SECOND -> message =
            if (isNeg) "несколько секунд назад" else "через несколько секунд"
        in 45 * SECOND..75 * SECOND -> message = if (isNeg) "минуту назад" else "минуту"
        in 75 * SECOND..45 * MINUTE -> message =
            if (isNeg) "${declineMinutes(round(difDate.toDouble() / MINUTE).toInt())} назад" else "через ${
                declineMinutes(round(difDate.toDouble() / MINUTE).toInt())
            }"
        in 45 * MINUTE..75 * MINUTE -> message = if (isNeg) "час назад" else {
            "через час"
        }
        in 75 * MINUTE..22 * HOUR -> message =
            if (isNeg) "${declineHours(round(difDate.toDouble() / HOUR).toInt())} назад" else "через ${
                declineHours(round(difDate.toDouble() / HOUR).toInt())
            }"
        in 22 * HOUR..26 * HOUR -> message = if (isNeg) "день назад" else "через день"
        in 26 * HOUR..360 * DAY -> message =
            if (isNeg) "${declineDays(round(difDate.toDouble() / DAY).toInt())} назад" else "через ${
                declineDays(round(difDate.toDouble() / DAY).toInt())
            }"
        else -> message = if (isNeg) "более года назад" else "более чем через год"
    }

    return message
}

private fun declineMinutes(minutes: Int): String {
    val out: String

    if (minutes % 100 in 5..20) out = "$minutes минут" else {
        when (minutes % 10) {
            1 -> out = "$minutes минуту"
            in 2..4 -> out = "$minutes минуты"
            else -> out = "$minutes минут"
        }
    }

    return out.toString()
}

private fun declineHours(hours: Int): String {
    val out: String

    if (hours % 100 in 5..20) out = "$hours часов" else {
        when (hours % 10) {
            1 -> out = "$hours час"
            in 2..4 -> out = "$hours часа"
            else -> out = "$hours часов"
        }
    }

    return out.toString()
}

private fun declineDays(days: Int): String {
    val out: String

    if (days % 100 in 5..20) out = "$days дней" else {
        when (days % 10) {
            1 -> out = "$days день"
            in 2..4 -> out = "$days дня"
            else -> out = "$days дней"
        }
    }

    return out.toString()
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}