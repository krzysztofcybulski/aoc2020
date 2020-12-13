package me.kcybulski

import me.kcybulski.utils.lines

class ShuttleSearch(departAt: Int, buses: List<Int>) {

    val times: List<Pair<Int, Int>> = buses
        .map { it to departAt + it - (departAt % it) }
        .sortedBy { it.second }

    fun earliestBus(): Int = times.first().first

    fun arriveAt(): Int = times.first().second
}

fun main() {
    val lines = lines("13ShuttleSearch")
    val departAt = lines.first().toInt()
    val buses = lines[1].split(",")
        .filterNot { it == "x" }
        .map { it.toInt() }
    val shuttleSearch = ShuttleSearch(departAt, buses)
    println(shuttleSearch.earliestBus())
    println(shuttleSearch.arriveAt())
    println(shuttleSearch.earliestBus() * (shuttleSearch.arriveAt() - departAt))
}
