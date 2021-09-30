package me.kcybulski

import me.kcybulski.utils.lines

class ShuttleSearch(departAt: Int,
                    private val buses: List<Pair<Int, Int>>) {

    fun chinesee(): Int {
        val prod = buses.map { it.second }.reduce { a, b -> a * b }
        val N = buses.map { prod / it.second }
        val M = buses.mapIndexed { index, pair -> findM(N[index], pair.second) }

        buses.forEach {
            print("(t + ${it.first}) mod ${it.second} = 0; ")
        }

        return M.mapIndexed { i, m -> - buses[i].first * m * N[i] }.reduce { a, b -> a * b } % prod
    }

    fun findM(a: Int, b: Int): Int = (a + 1) / b
}

fun main() {
    val lines = lines("13ShuttleSearch")
    val departAt = lines.first().toInt()
    val buses = lines[1].split(",")
        .mapIndexed { index, s -> if(s == "x") null else index to s.toInt() }
        .filterNotNull()

    val shuttleSearch = ShuttleSearch(departAt, buses)
    println(shuttleSearch.chinesee())
}
