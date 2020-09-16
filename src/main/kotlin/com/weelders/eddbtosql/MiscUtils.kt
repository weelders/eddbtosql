package com.weelders.eddbtosql

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

val ANSI_RESET = "\u001B[0m"
val ANSI_GREEN = "\u001B[32m"
val ANSI_YELLOW = "\u001B[33m"
val ANSI_RED = "\u001B[31m"


fun convertLongToTime(time: Long): String
{
    val date = Date(time)
    val format = SimpleDateFormat("dd-MM-yy HH:mm:ss")
    return format.format(date)
}

fun traceUpdate(tag: String, msg: String)
{
    val timeNow = Instant.now().toEpochMilli()
    println("${convertLongToTime(timeNow)} $ANSI_YELLOW--- $tag ---$ANSI_RESET $ANSI_GREEN $msg $ANSI_RESET")
}

fun traceServerRequest(tag: String)
{
    val timeNow = Instant.now().toEpochMilli()
    println("${convertLongToTime(timeNow)} $ANSI_RED-- SERVEUR REQUEST --$ANSI_RESET $ANSI_GREEN $tag $ANSI_RESET")
}