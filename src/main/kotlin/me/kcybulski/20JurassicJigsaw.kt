package me.kcybulski

import me.kcybulski.utils.lines

class JurassicJigsaw(val tiles: List<Tile>) {

    fun arrangeAndMultiply(): Long {
        val nonMatching = tiles.filter { tile -> (tiles - tile).count { it.matchesWith(tile) } == 2 }
        return nonMatching.fold(1L) { a, b -> a * b.id.toLong()}
    }


    class Tile(val id: Int, val pattern: List<String>) {

        val top = pattern.take(1).first()
        val bottom = pattern.takeLast(1).first()
        val left = pattern.joinToString("", transform = { it.first().toString() })
        val right = pattern.joinToString("", transform = { it.last().toString() })

        val sides = setOf(top, bottom, left, right, top.reversed(), bottom.reversed(), left.reversed(), right.reversed())

        fun matchesWith(tile: Tile) = tile.sides.any { it in sides }
        fun matchesSides(tile: Tile) = tile.sides.count { it in sides }

    }

}

fun main() {
    val tiles = lines("20JurassicJigsaw", "\n\n")
        .map { it.split("\n") }
        .map { JurassicJigsaw.Tile(it.first().drop(5).dropLast(1).toInt(), it.drop(1)) }
    val jigsaw = JurassicJigsaw(tiles)
    println(jigsaw.arrangeAndMultiply())
}
