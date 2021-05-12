package ru.skillbranch.skillarticles.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

private const val SECOND = 1000L
private const val MINUTE = 60L * SECOND
private const val HOUR = 60L * MINUTE
private const val DAY = 24L * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, timeUnit: TimeUnits): Date {
    return this.apply {
        time += when (timeUnit) {
            TimeUnits.SECOND -> value * SECOND
            TimeUnits.DAY -> value * DAY
            TimeUnits.HOUR -> value * HOUR
            TimeUnits.MINUTE -> value * MINUTE
        }
    }

}

fun Date.shortFormat(): String {
    return this.format(if (isSameDay(Date())) "HH:mm" else "dd.MM.yy")
}

fun Date.isSameDay(date: Date): Boolean {
    return this.time / DAY == date.time / DAY
}

fun Date.humanizeDiff(date: Date = Date()): String {

    return when {
        abs(this.time - date.time) <= SECOND && date > this -> "только что"
        this.time < date.time - 360 * DAY -> "более года назад"
        this.time > date.time + 360 * DAY -> "более чем через год"
        else -> {
            val seconds = (abs(this.time - date.time) / SECOND).toInt()
            val template = if (this > date) "через %s" else "%s назад"
            template.format(
                when {
                    seconds in 1..45 -> "несколько секунд"
                    seconds in 45..75 -> "минуту"
                    seconds in 75..45 * 60 -> pluralForm(seconds / 60, TimeUnits.MINUTE)
                    seconds in 45 * 60..75 * 60 -> "час"
                    seconds in 75 * 60..22 * 3600 -> pluralForm(seconds / 3600, TimeUnits.HOUR)
                    seconds in 22 * 3600..26 * 3600 -> "день"
                    seconds in 26 * 3600..360 * 86400 -> pluralForm(seconds / 86400, TimeUnits.DAY)
                    else -> throw IllegalStateException()
                }
            )
        }

    }
}


private fun pluralForm(value: Int, unit: TimeUnits): String {


    val form = when {
        value % 10 == 1 && value != 11 -> 0
        value % 10 in 2..4 -> 1
        else -> 2
    }
    val forms = listOf(
        "%s секунду",
        "%s секунды",
        "%s секунд",
        "%s минуту",
        "%s минуты",
        "%s минут",
        "%s час",
        "%s часа",
        "%s часов",
        "%s день",
        "%s дня",
        "%s дней"
    )
    return forms[form + 3 * when (unit) {
        TimeUnits.SECOND -> 0
        TimeUnits.MINUTE -> 1
        TimeUnits.HOUR -> 2
        TimeUnits.DAY -> 3
    }].format(value)
}

enum class TimeUnits {
    SECOND, MINUTE, HOUR, DAY;

    fun plural(value: Int): String = pluralForm(value, this)
}