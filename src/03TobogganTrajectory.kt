class TobogganTrajectory {
}

class Mountain(private val pattern: List<List<Boolean>>) {

    fun treesOnSlope(slope: Int, speed: Int = 1) = (0 until height / speed)
        .count { isTreeOn(it * speed, it * slope) }

    private val height = pattern.size

    private fun isTreeOn(down: Int, right: Int) = pattern[down][right % pattern[0].size]

}

fun main() {
    val lines = object {}.javaClass.getResource("03TobogganTrajectory.txt")
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }

    val mountain = Mountain(lines.map { it.map { it == '#' } })

    val result = listOf(
        mountain.treesOnSlope(1),
        mountain.treesOnSlope(3),
        mountain.treesOnSlope(5),
        mountain.treesOnSlope(7),
        mountain.treesOnSlope(1, 2)
    ).reduce { acc, i ->  acc * i }

    println(result)
}
