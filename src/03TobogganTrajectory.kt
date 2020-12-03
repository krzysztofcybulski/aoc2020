class TobogganTrajectory {
}

class Mountain(private val pattern: List<List<Boolean>>) {

    fun height() = pattern.size

    fun isTreeOn(down: Int, right: Int) = pattern[down][right % pattern[0].size]

}

fun main(args: Array<String>) {
    val lines = object {}.javaClass.getResource("03TobogganTrajectory.txt")
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }

    val mountain = Mountain(lines.map { it.map { it == '#' } })

    println((0 until mountain.height()).count { mountain.isTreeOn(it, it * 3) })
}
