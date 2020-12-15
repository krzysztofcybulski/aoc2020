package me.kcybulski

import me.kcybulski.utils.lines

class RambunctiousRecitation {

    fun say(numbers: List<Int>): Int {

        val positions = mutableMapOf<Int, Int>()
        numbers.dropLast(1).forEach { positions[it] = numbers.indexOf(it) }

        var last = numbers.last()
        var i = numbers.size

        while(i != 30000000) {
            val newLast = positions[last]
                ?.let { i - it - 1 }
                ?: 0
            positions[last] = i - 1
            last = newLast
            i++
        }

        return last
    }

}

fun main() {
    val lines = lines("15RambunctiousRecitation").first()
        .split(",")
        .map { it.toInt() }
    println(RambunctiousRecitation().say(lines))
}
