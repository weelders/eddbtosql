package com.weelders.eddbtosql

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

/*--------------------------------------------*/
/*--COLORS--*/
/*--------------------------------------------*/

val ANSI_RESET = "\u001B[0m"
val ANSI_GREEN = "\u001B[32m"
val ANSI_YELLOW = "\u001B[33m"
val ANSI_RED = "\u001B[31m"

/*--------------------------------------------*/
/*--FUNCTIONS--*/
/*--------------------------------------------*/

//Convert timestamp to string for display
fun convertLongToTime(time: Long): String
{
    val date = Date(time)
    val format = SimpleDateFormat("dd-MM-yy HH:mm:ss")
    return format.format(date)
}

//Info in server's console
fun traceUpdate(tag: String, msg: String)
{
    val timeNow = Instant.now().toEpochMilli()
    println("${convertLongToTime(timeNow)} $ANSI_YELLOW--- $tag ---$ANSI_RESET $ANSI_GREEN $msg $ANSI_RESET")
}

//Info in server's console
fun traceServerRequest(tag: String)
{
    val timeNow = Instant.now().toEpochMilli()
    println("${convertLongToTime(timeNow)} $ANSI_RED-- SERVEUR REQUEST --$ANSI_RESET $ANSI_GREEN $tag $ANSI_RESET")
}

//Calcul distance between 2 points in 3D
fun distance3DCalculation(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double
{
    return sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2) + (z2 - z1).pow(2))
}