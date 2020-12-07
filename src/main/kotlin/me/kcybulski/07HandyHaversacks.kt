package me.kcybulski

data class Bag(val name: String, val innerBags: Collection<Bag>) {

    fun contains(color: String): Boolean = when(name) {
        color -> true
        else -> innerBags.any { it.contains(color) }
    }

}

fun main() {
    val bagsMap: Map<String, List<String>> = lines("07HandyHaversacks")
        .map { parse(it) }
        .associateBy({ it.first }, { it.second })
    val bags = bagsMap.keys.map { createBag(it, bagsMap) }
    println(bags.count { it.contains("shiny gold") })
}

private fun createBag(name: String, bags: Map<String, List<String>>): Bag =
    Bag(name, (bags[name] ?: emptyList()).map { createBag(it, bags) })

private val bagsRegex = "([1-9][0-9]*) ([a-z]+ [a-z]+) bags?[,|.]\\s*".toRegex()

private fun parse(line: String): Pair<String, List<String>> {
    val split = line.split(" bags contain ")
    val bags = bagsRegex.findAll(split[1])
        .map { Pair(it.groupValues[1], it.groupValues[2]) }
        .map { (amount, name) -> name }
        .toList()
    return Pair(split[0], bags)
}

private fun bags(amount: Int, name: String) = (1..amount).map { name }
