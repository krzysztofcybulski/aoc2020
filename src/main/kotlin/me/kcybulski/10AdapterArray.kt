package me.kcybulski

import me.kcybulski.utils.lines

data class Difference(val jolts: Int)

fun main(args: Array<String>) {
    val lines = lines("10AdapterArray")
        .map { it.toInt() }
        .sorted()

    val diffs = differences(lines, 0)
    println((diffs.count { it.jolts == 3 } + 1)* diffs.count { it.jolts == 1 })
}

fun differences(input: List<Int>, compare: Int, output: List<Difference> = emptyList()): List<Difference> =
    if(input.isEmpty()) {
        output
    } else {
        val first = input.first()
        differences(input.drop(1), first, output + Difference(first - compare))
    }
