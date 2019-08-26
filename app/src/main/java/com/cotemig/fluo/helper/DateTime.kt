package com.cotemig.fluo.helper

import java.sql.Date
import java.text.ParseException
import java.util.Calendar
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.TimeZone

class DateTime {

    var millis: Long = 0

    val date: Date?
        get() = if (this.millis != 0L) {
            Date(this.millis)
        } else {
            null
        }

    val timestamp: Timestamp?
        get() = if (this.millis != 0L) {
            Timestamp(this.millis)
        } else {
            null
        }

    val weekDay: String
        get() = this.toString("EEEE")

    val weekDaySimple: String
        get() = this.toString("E")

    val weekDayInt: Int
        get() = if (this.weekDaySimple == "Dom" || this.weekDaySimple == "Sun")
            1
        else if (this.weekDaySimple == "Seg" || this.weekDaySimple == "Mon")
            2
        else if (this.weekDaySimple == "Ter" || this.weekDaySimple == "Tue")
            3
        else if (this.weekDaySimple == "Qua" || this.weekDaySimple == "Wed")
            4
        else if (this.weekDaySimple == "Qui" || this.weekDaySimple == "Thu")
            5
        else if (this.weekDaySimple == "Sex" || this.weekDaySimple == "Fri") 6 else 7

    val day: Int
        get() = Integer.parseInt(this.toString("dd"))

    val month: Int
        get() = Integer.parseInt(this.toString("MM"))

    val year: Int
        get() = Integer.parseInt(this.toString("yyyy"))

    val hour: Int
        get() = Integer.parseInt(this.toString("HH"))

    val minute: Int
        get() = Integer.parseInt(this.toString("mm"))

    val second: Int
        get() = Integer.parseInt(this.toString("ss"))
//private Locale locale = new Locale("pt", "BR");

    constructor(d: DateTime) {
        this.millis = d.millis
    }

    @Throws(ParseException::class)
    constructor(date: String, format: String) {
        TimeZone.setDefault(TimeZone.getTimeZone(zone))
        val fmt = SimpleDateFormat(format)
        this.millis = fmt.parse(date).time
    }

    @Throws(ParseException::class)
    constructor(vararg v: Int) {
        TimeZone.setDefault(TimeZone.getTimeZone(zone))
        val date = v[2].toString() + "/" + v[1] + "/" + v[0] + " " + v[3] + ":" + v[4] + ":" + v[5]
        val format = "dd/MM/yyyy HH:mm:ss"
        val fmt = SimpleDateFormat(format)
        this.millis = fmt.parse(date).time
    }

    @Throws(ParseException::class)
    constructor(year: Int, month: Int, day: Int) {
        TimeZone.setDefault(TimeZone.getTimeZone(zone))
        val date = "$day/$month/$year"
        val format = "dd/MM/yyyy"
        val fmt = SimpleDateFormat(format)
        this.millis = fmt.parse(date).time
    }

    constructor(millis: Long) {
        TimeZone.setDefault(TimeZone.getTimeZone(zone))
        this.millis = millis
    }

    constructor(value: Timestamp?) {
        TimeZone.setDefault(TimeZone.getTimeZone(zone))
        if (value != null) {
            this.millis = value.time
        } else {
            this.millis = 0
        }
    }

    constructor(value: Date?) {
        TimeZone.setDefault(TimeZone.getTimeZone(zone))
        if (value != null) {
            this.millis = value.time
        } else {
            this.millis = 0
        }
    }

    fun toString(format: String): String {
        if (this.millis != 0L) {
//DateFormat fmt = new SimpleDateFormat(format, locale);
            val fmt = SimpleDateFormat(format)
            return fmt.format(timestamp)
        } else {
            return ""
        }
    }

    fun addSecond(v: Long) {
        this.millis += v * 1000
    }

    fun addCloneSecond(v: Long): DateTime {
        val c = this.clone()
        c.addSecond(v)
        return c
    }

    fun addMinute(v: Long) {
        addSecond(v * 60)
    }

    fun addCloneMinute(v: Long): DateTime {
        val c = this.clone()
        c.addMinute(v)
        return c
    }

    fun addHour(v: Long) {
        addMinute(v * 60)
    }

    fun addCloneHour(v: Long): DateTime {
        val c = this.clone()
        c.addHour(v)
        return c
    }

    fun addDay(v: Long) {
        addHour(v * 24)
    }

    fun addCloneDay(v: Long): DateTime {
        val c = this.clone()
        c.addDay(v)
        return c
    }

    fun addMonth(v: Int) {
        val c = Calendar.getInstance()
        c.timeInMillis = this.millis
        c.add(Calendar.MONTH, v)
        this.millis = c.timeInMillis
    }

    fun addCloneMonth(v: Int): DateTime {
        val c = this.clone()
        c.addMonth(v)
        return c
    }

    fun addYear(v: Int) {
        val c = Calendar.getInstance()
        c.timeInMillis = this.millis
        c.add(Calendar.YEAR, v)
        this.millis = c.timeInMillis
    }

    fun addCloneYear(v: Int): DateTime {
        val c = this.clone()
        c.addYear(v)
        return c
    }

    fun addDateTime(v: DateTime) {
        this.millis += v.millis
    }

    fun addCloneDateTime(v: DateTime): DateTime {
        val c = this.clone()
        c.addDateTime(v)
        return c
    }

    fun clone(): DateTime {
        return DateTime(this.millis)
    }

    fun array(): IntArray {
        val v = IntArray(7)
        v[0] = this.year
        v[1] = this.month
        v[2] = this.day
        v[3] = this.hour
        v[4] = this.minute
        v[5] = this.second
        v[6] = if (this.weekDaySimple == "Dom" || this.weekDaySimple == "Sun")
            1
        else if (this.weekDaySimple == "Seg" || this.weekDaySimple == "Mon")
            2
        else if (this.weekDaySimple == "Ter" || this.weekDaySimple == "Tue")
            3
        else if (this.weekDaySimple == "Qua" || this.weekDaySimple == "Wed")
            4
        else if (this.weekDaySimple == "Qui" || this.weekDaySimple == "Thu")
            5
        else if (this.weekDaySimple == "Sex" || this.weekDaySimple == "Fri") 6 else 7

        return v
    }

    override fun toString(): String {
        return "DateTime{" + toString("dd/MM/yyyy HH:mm:ss EEEE") + '}'.toString()
    }

    fun equalDay(d: DateTime): Boolean {
        return this.day == d.day && this.month == d.month && this.year == d.year
    }

    fun minor(d: DateTime): Boolean {
        return this.millis < d.millis
    }

    companion object {

        private val zone: String
            get() = "America/Sao_Paulo"

        fun now(): DateTime {
            TimeZone.setDefault(TimeZone.getTimeZone(zone))
            val c = Calendar.getInstance()
            return DateTime(c.timeInMillis)
        }

        @Throws(Exception::class)
        fun diffWorkingDayInSeconds(v1: DateTime, v2: DateTime): Long {
            var v1 = v1
            var v2 = v2

            if (v1.millis <= v2.millis) {

                if (v1.weekDayInt == 1) {
                    v1.addDay(1)
                    val t1 = v1.array()
                    t1[3] = 8
                    t1[4] = 0
                    t1[5] = 0
                    v1 = DateTime(*t1)
                }
                if (v2.weekDayInt == 1) {
                    v2.addDay(1)
                    val t2 = v2.array()
                    t2[3] = 8
                    t2[4] = 0
                    t2[5] = 0
                    v2 = DateTime(*t2)
                }
                if (v1.weekDayInt == 7) {
                    v1.addDay(2)
                    val t1 = v1.array()
                    t1[3] = 8
                    t1[4] = 0
                    t1[5] = 0
                    v1 = DateTime(*t1)
                }
                if (v2.weekDayInt == 7) {
                    v2.addDay(2)
                    val t2 = v2.array()
                    t2[3] = 8
                    t2[4] = 0
                    t2[5] = 0
                    v2 = DateTime(*t2)
                }

                if (v1.day == v2.day && v1.month == v2.month && v1.year == v2.year) {

                    val t1 = v1.array()
                    val t2 = v2.array()

                    if (t1[3] > 18) {
                        t1[3] = 18
                        t1[4] = 0
                        t1[5] = 0
                    }

                    if (t2[3] > 18) {
                        t2[3] = 18
                        t2[4] = 0
                        t2[5] = 0
                    }

                    if (t1[3] < 8) {
                        t1[3] = 8
                        t1[4] = 0
                        t1[5] = 0
                    }

                    if (t2[3] < 8) {
                        t2[3] = 8
                        t2[4] = 0
                        t2[5] = 0
                    }

                    if (t1[3] > 12 && t1[3] < 14) {
                        t1[3] = 14
                        t1[4] = 0
                        t1[5] = 0
                    }

                    if (t2[3] > 12 && t2[3] < 14) {
                        t2[3] = 14
                        t2[4] = 0
                        t2[5] = 0
                    }

                    if (t1[3] < 14 && t2[3] >= 14) {
                        t1[3] += 2
                    }
                    val d1 = DateTime(*t1)
                    val d2 = DateTime(*t2)

                    return d2.millis / 1000 - d1.millis / 1000

                } else {
                    var v = v1.clone()
                    val t = v.array()
                    if (t[3] == 8 && t[4] == 0 && t[5] == 0) {
                        v.addDay(1)
                        return diffWorkingDayInSeconds(v, v2) + 28800
                    } else {
                        if (t[3] < 14) {
                            t[3] += 2
                        }
                        val diff = ((18 - t[3]) * 60 * 60 + t[4] * 60 + t[5]).toLong()

                        t[3] = 8
                        t[4] = 0
                        t[5] = 0

                        v = DateTime(*t)
                        v.addDay(1)

                        return diffWorkingDayInSeconds(v, v2) + diff
                    }
                }

            } else {
                return diffWorkingDayInSeconds(v2, v1)
            }

        }
    }

}