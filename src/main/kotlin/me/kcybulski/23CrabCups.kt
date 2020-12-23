package me.kcybulski

import me.kcybulski.utils.lines
import java.lang.RuntimeException
import kotlin.math.max

class CrabCups(var cups: MutableList<Int>, var currentIndex: Int = 0) {

    fun move() {
        println("cups: ${cups.joinToString(" ") { if (it == cups[currentIndex]) "(${it})" else it.toString()} }")

        val pickedUp = cups.takeRound(currentIndex + 1, 3)
        val current = cups[currentIndex]
        cups.removeAll(pickedUp)
        val destination = cups.filter { it < current }
            .maxOrNull()
            ?: cups.maxOrNull()
            ?: throw RuntimeException("No destination")
        cups.addAll(cups.indexOf(destination) + 1 , pickedUp)

        cups = cups.takeRound(max(0, cups.indexOf(current) - currentIndex)).toMutableList()

        currentIndex += 1
        currentIndex %= cups.size

        println("pick up: ${pickedUp.joinToString(" ")}")
        println("destination: $destination")
    }

    fun result() = cups.takeRound(cups.indexOf(1))

    private fun List<Int>.takeRound(skip: Int, n: Int = size): List<Int> {
        val last = drop(skip).take(n)
        val first = take(n - last.size)
        return last + first
    }

}

fun main() {
    val lines = lines("23CrabCups")
    val crabCups = CrabCups(lines.first().map { it.toString().toInt() }.toMutableList())
    repeat((1..100).count()) {
        println("-- move ${it + 1} --")
        crabCups.move()
        println()
    }
    println(crabCups.result().joinToString(""))
}
