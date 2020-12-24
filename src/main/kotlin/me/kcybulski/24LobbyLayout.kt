package me.kcybulski

import me.kcybulski.LobbyLayout.Tile.Companion.POSITIONS
import me.kcybulski.utils.lines

class LobbyLayout(tiles: List<Tile>) {

    private val blacks = mutableSetOf<Pair<Int, Int>>()

    init {
        tiles.map { it.position }.forEach {
            if (blacks.contains(it)) {
                blacks.remove(it)
            } else {
                blacks.add(it)
            }
        }
    }

    fun flipAll() {
        val tiles = mutableSetOf<Pair<Int, Int>>()
        tiles.addAll(blacks)
        blacks.forEach { tiles.addAll(neighbours(it)) } // Why flatMap doesnt work grrr
        tiles
            .map { pos -> Triple(pos, blacks.contains(pos), neighbours(pos).count { blacks.contains(it) }) }
            .forEach { (position, isBlack, neighbours) ->
                if (isBlack && (neighbours == 0 || neighbours > 2))
                    blacks.remove(position)
                if (!isBlack && neighbours == 2)
                    blacks.add(position)
            }
    }

    fun howManyBlack() = blacks.size

    private fun neighbours(position: Pair<Int, Int>): List<Pair<Int, Int>> = POSITIONS
        .values
        .map { position.first + it.first to position.second + it.second }

    class Tile(private val input: String) {

        val position = setPosition()

        private fun setPosition(x: Int = 0, y: Int = 0, position: String = input): Pair<Int, Int> {
            if (position.isEmpty()) {
                return x to y
            }
            val (xc, yc) = POSITIONS[position.take(2)]
                ?: POSITIONS[position.take(1)]
                ?: throw RuntimeException("No move")
            return setPosition(x + xc, y + yc, position.drop(if (xc == 2 || xc == -2) 1 else 2))
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
    repeat(100) {
        layout.flipAll()
        println("Day ${it + 1}: ${layout.howManyBlack()}")
    }
}
