package me.kcybulski

import me.kcybulski.utils.lines

class RambunctiousRecitation() {

    fun say(numbers: List<Int>): Int {
        val last = numbers.last()
        val withoutLast = numbers.dropLast(1)
        return when {
            numbers.size == 2020 -> last
            last !in withoutLast -> return say(numbers + 0)
            else -> return say(numbers + (numbers.size - withoutLast.lastIndexOf(last) - 1))
        }
    }

}

fun main() {
    val lines = lines("15RambunctiousRecitation").first()
        .split(",")
        .map { it.toInt() }
    println(RambunctiousRecitation().say(lines))
}
