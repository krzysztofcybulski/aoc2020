package me.kcybulski

import me.kcybulski.utils.lines

class DockingData {

    private val memory: MutableMap<Int, Long> = mutableMapOf()

    fun run(lines: List<String>) {
        if (lines.isEmpty()) return
        lines.asSequence()
            .drop(1)
            .takeWhile { it.startsWith("mem") }
            .map { regex.matchEntire(it)!! }
            .map { Command(it.groupValues[1].toInt(), it.groupValues[2].toLong()) }
            .map { it.copy(value = withMask(it.value, lines.first())) }.toList()
            .forEach { memory[it.address] = it.value }
        run(lines.drop(1).dropWhile { it.startsWith("mem") })
    }

    fun sum() = memory.values.sum()

    private fun withMask(value: Long, mask: String): Long = value
        .toString(2)
        .padStart(36, '0')
        .reversed()
        .zip(mask.reversed())
        .map {
            when (it.second) {
                'X' -> it.first
                else -> it.second
            }
        }
        .reversed()
        .joinToString("")
        .toLong(2)

    data class Command(val address: Int, val value: Long)

    companion object {

        val regex = "^mem\\[([0-9]+)\\]\\s+=\\s+([0-9]+)\$".toRegex()
    }
}

fun main() {
    val lines = lines("14DockingData")
    val dockingData = DockingData()
    dockingData.run(lines)
    println(dockingData.sum())
}
