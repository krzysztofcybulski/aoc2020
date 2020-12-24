package me.kcybulski

import me.kcybulski.utils.lines

class LobbyLayout(private val tiles: List<Tile>) {

    private val blacks = mutableSetOf<Pair<Int, Int>>()

    init {
        tiles.map { it.position }.forEach {
            if(blacks.contains(it)) {
                blacks.remove(it)
            } else {
                blacks.add(it)
            }
        }
    }

    fun howManyBlack() = blacks.size

    class Tile(private val input: String) {

        val position = setPosition()

        private fun setPosition(x: Int = 0, y: Int = 0, position: String = input): Pair<Int, Int> {
            if (position.isEmpty()) {
                return x to y
            }
            val (xc, yc) = POSITIONS[position.take(2)]
                ?: POSITIONS[position.take(1)]
                ?: throw RuntimeException("No move")
            return setPosition(x + xc, y + yc, position.drop(if(xc == 2 || xc == -2) 1 else 2))
        }

        companion object {

            val POSITIONS = mapOf(
                "e" to Pair(2, 0),
                "se" to Pair(1, -1),
                "sw" to Pair(-1, -1),
                "w" to Pair(-2, 0),
                "nw" to Pair(-1, 1),
                "ne" to Pair(1, 1)
            )

        }
    }
}

fun main() {
    val tiles = lines("24LobbyLayout")
        .map { LobbyLayout.Tile(it) }
    val layout = LobbyLayout(tiles)
    println(layout.howManyBlack())
}
