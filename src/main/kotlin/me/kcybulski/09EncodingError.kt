package me.kcybulski

fun main() {
    val lines = lines("09EncodingError").map { it.toLong() }
    val preamble = 25

    lines
        .drop(preamble)
        .mapIndexed { i, x -> isPairOf(x, lines.drop(i).take(preamble)) }
        .indexOf(false)
        .let { println(lines[it + preamble]) }
}

fun isPairOf(x: Long, nums: List<Long>): Boolean = nums
    .map { x - it }
    .find { nums.contains(it) } != null
