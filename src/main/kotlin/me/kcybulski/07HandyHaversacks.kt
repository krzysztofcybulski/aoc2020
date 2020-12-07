package me.kcybulski

data class Bag(val name: String, val innerBags: Collection<Pair<Int, Bag>>) {

//    fun contains(color: String): Boolean = when(name) {
//        color -> true
//        else -> innerBags.any { (_, bag) -> bag.contains(color) }
//    }

    fun size(): Int = 1 + (innerBags.sumBy { (amount, bag) -> amount * bag.size() })

}

fun main() {
    val bagsMap: Map<String, List<Pair<Int, String>>> = lines("07HandyHaversacks")
        .map { parse(it) }
        .associateBy({ it.first }, { it.second })
    val bags = bagsMap.keys.map { createBag(it, bagsMap) }
    println(bags.find { it.name == "shiny gold" }?.size())
}

private fun createBag(name: String, bags: Map<String, List<Pair<Int, String>>>): Bag =
    Bag(name, (bags[name] ?: emptyList<Pair<Int, String>>()).map { Pair(it.first, createBag(it.second, bags)) })

private val bagsRegex = "([1-9][0-9]*) ([a-z]+ [a-z]+) bags?[,|.]\\s*".toRegex()

private fun parse(line: String): Pair<String, List<Pair<Int, String>>> {
    val split = line.split(" bags contain ")
    val bags = bagsRegex.findAll(split[1])
        .map { Pair(it.groupValues[1].toInt(), it.groupValues[2]) }
        .toList()
    return Pair(split[0], bags)
}
