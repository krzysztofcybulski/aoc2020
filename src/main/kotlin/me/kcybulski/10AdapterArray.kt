package me.kcybulski

import me.kcybulski.utils.lines

data class Difference(val jolts: Int)

fun main() {
    val lines = lines("10AdapterArray")
        .map { it.toInt() }
        .sorted()

    val adapters = listOf(0) + lines + (lines.maxOrNull() ?: 0 + 3)

    val diffs = differences(adapters, 0)
    println(diffs.count { it.jolts == 3 } * diffs.count { it.jolts == 1 })
    println(ways(adapters))
}

fun differences(input: List<Int>, compare: Int, output: List<Difference> = emptyList()): List<Difference> =
    if (input.isEmpty()) {
        output
    } else {
        val first = input.first()
        differences(input.drop(1), first, output + Difference(first - compare))
    }

fun ways(input: List<Int>): Long =
    input.drop(1)
        .fold(mapOf(0 to 1L)) { acc, adapter ->
            acc + (adapter to ((adapter - 3) until adapter).map { acc[it] ?: 0L }.sum())
        }[input.last()] ?: 0L
