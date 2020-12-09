package me.kcybulski

import me.kcybulski.utils.lines

fun main() {
    val nums = lines("09EncodingError").map { it.toLong() }
    val sum = 1212510616L

    var index = 0
    var length = 1

    while (true) {
        val s = nums.subList(index, index + length).sum()
        when {
            s > sum -> {
                index++
                length = 1
            }
            s < sum -> length++
            else -> {
                val res = nums.subList(index, index + length)
                println((res.minOrNull() ?: 0) + (res.maxOrNull() ?: 0))
                return
            }
        }
    }
}
