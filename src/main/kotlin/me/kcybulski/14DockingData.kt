package me.kcybulski

import me.kcybulski.utils.lines

class DockingData {

    private val memory: MutableMap<Long, Long> = mutableMapOf()

    fun run(lines: List<String>) {
        if (lines.isEmpty()) return
        lines.asSequence()
            .drop(1)
            .takeWhile { it.startsWith("mem") }
            .map { regex.matchEntire(it)!! }
            .map { Command(it.groupValues[1].toLong(), it.groupValues[2].toLong()) }
            .flatMap { cmd ->
                toAddresses(withMask(cmd.address, lines.first()))
                    .map { cmd.copy(address = it) }
            }
            .forEach { memory[it.address] = it.value }
        run(lines.drop(1).dropWhile { it.startsWith("mem") })
    }

    fun sum() = memory.values.sum()

    private fun toAddresses(value: String): List<Long> {
        return if ('X' !in value)
            listOf(value.toLong(2))
        else
            toAddresses(value.replaceFirst('X', '1')) + toAddresses(value.replaceFirst('X', '0'))
    }

    private fun withMask(value: Long, mask: String): String = value
        .toString(2)
        .padStart(36, '0')
        .reversed()
        .zip(mask.reversed())
        .map {
            when (it.second) {
                '0' -> it.first
                '1' -> '1'
                else -> 'X'
            }
        }
        .reversed()
        .joinToString("")

    data class Command(val address: Long, val value: Long)

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
