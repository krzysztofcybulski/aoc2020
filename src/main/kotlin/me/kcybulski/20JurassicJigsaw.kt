package me.kcybulski

import me.kcybulski.utils.lines
import kotlin.math.sqrt

class JurassicJigsaw(val tiles: MutableList<Tile>) {

    val size = sqrt(tiles.size.toDouble()).toInt()

//    val arrangement: List<MutableList<Tile>> = arrayOf(
//        intArrayOf(3083, 1871, 2063, 2801, 3797, 2521, 2617, 3709, 2789, 2111, 1451, 1021),
//        intArrayOf(3677, 2791, 2129, 2311, 2837, 2617, 2437, 1613, 2437, 2789, 1049, 1093),
//        intArrayOf(1669, 3727, 1289, 2393, 1951, 3559, 2707, 3851, 3041, 2111, 1999, 3463),
//        intArrayOf(1129, 2411, 2267, 2939, 1979, 3767, 3299, 1069, 1483, 1451, 1741, 1567),
//        intArrayOf(2741, 1129, 2609, 2281, 3943, 2731, 3347, 1523, 3929, 1021, 2423, 2221),
//        intArrayOf(2659, 2741, 1723, 2467, 1637, 3167, 2963, 2579, 2441, 1999, 3463, 3019),
//        intArrayOf(3803, 2659, 1409, 1327, 1901, 2003, 2557, 2137, 2647, 1741, 1093, 1283),
//        intArrayOf(2897, 3803, 3719, 3547, 2273, 1487, 3923, 1721, 3691, 3041, 2287, 1579),
//        intArrayOf(3863, 2897, 2351, 1193, 2269, 2203, 3671, 1549, 3271, 1483, 1567, 3529),
//        intArrayOf(3329, 3863, 1217, 3203, 1201, 3137, 3593, 3637, 3163, 1667, 2221, 1361),
//        intArrayOf(2141, 3719, 1031, 2131, 1933, 2971, 1427, 3761, 2459, 3833, 3019, 1553),
//        intArrayOf(3461, 3547, 3643, 1699, 3527, 2953, 3511, 1783, 2753, 2549, 1283, 3881)
//    ).map { row -> row.map { id -> tiles.find { it.id == id }!! }.toMutableList() }

    fun rotate(input: List<List<Tile>>): List<List<Tile>> {
        val arrangement = input.map { it.toMutableList() }
        arrangement.indices.forEach { row ->
            arrangement[row].indices.forEach { column ->
                if (row > 0 && column > 0) {
                    while (!arrangement[row][column].matchesWith(arrangement[row - 1][column])
                        && !arrangement[row][column].matchesWith(arrangement[row][column - 1])
                    ) {
                        arrangement[row][column] = arrangement[row][column].randomRotate()
                    }
                } else if (row > 0) {
                    while (!arrangement[row][column].matchesWith(arrangement[row - 1][column])) {
                        arrangement[row][column] = arrangement[row][column].randomRotate()
                    }
                } else if (column > 0) {
                    while (!arrangement[row][column].matchesWith(arrangement[row][column - 1])) {
                        arrangement[row][column] = arrangement[row][column].randomRotate()
                    }
                }
            }
        }
        return arrangement
    }

    fun arrange(): List<List<Tile>> {
        val arrange: Array<Array<Tile?>> = Array(size) { Array(size) { null } }
        tiles.forEach { tile -> tile.friends.addAll((tiles - tile).filter { it.matchesWith(tile) }) }
        arrange[0][0] = tiles.find { it.friends.size == 2 }
        tiles.forEach { it.friends.remove(arrange[0][0]) }
        arrange.indices.forEach { row ->
            arrange[row].indices.forEach { column ->
                if (arrange[row][column] == null) {
                    val neighbors = getNeighbors(arrange, row, column)
                    val tileToPut = neighbors.flatMap { it.friends }.distinct().first()
                    tiles.forEach { it.friends.remove(tileToPut) }
                    arrange[row][column] = tileToPut
                }
            }
        }
        return arrange.map { it.map { it!! } }
    }

    private fun getNeighbors(arrange: Array<Array<Tile?>>, row: Int, column: Int): List<Tile> {
        val tiles = mutableListOf<Tile?>()
        if (row > 0) tiles.add(arrange[row - 1][column])
        if (column > 0) tiles.add(arrange[row][column - 1])
        if (row < size - 1) tiles.add(arrange[row + 1][column])
        if (column < size - 1) tiles.add(arrange[row][column + 1])
        return tiles.filterNotNull()
    }

    class Tile(val id: Int, val pattern: List<String>, val friends: MutableList<Tile> = mutableListOf()) {

        val top = pattern.first()
        val bottom = pattern.last()
        val left = pattern.joinToString("", transform = { it.first().toString() })
        val right = pattern.joinToString("", transform = { it.last().toString() })

        val sides =
            setOf(top, bottom, left, right, top.reversed(), bottom.reversed(), left.reversed(), right.reversed())

        fun rotates() = listOf(
            this,
            rotate(),
            rotate().rotate(),
            rotate().rotate().rotate(),
            flipHorizontal(),
            flipVertical(),
            rotate().flipHorizontal(),
            rotate().flipVertical(),
            rotate().rotate().flipHorizontal(),
            rotate().rotate().flipVertical(),
            rotate().rotate().rotate().flipHorizontal(),
            rotate().rotate().rotate().flipVertical()
        )

        fun matchesWith(tile: Tile) = tile.sides.any { it in sides }

        fun match(tile: Tile, hisSide: (Tile) -> String, ourSide: (Tile) -> String): Tile? = rotates()
            .find { it.matchesWithExactly(tile, hisSide, ourSide) }

        fun randomRotate() = rotates().random()

        fun matchesWithExactly(tile: Tile, hisSide: (Tile) -> String, ourSide: (Tile) -> String): Boolean =
            hisSide(tile) == ourSide(this)

        fun rotate() = Tile(pattern = pattern
            .mapIndexed { rowIndex, row ->
                row.mapIndexed { columnIndex, _ -> pattern[columnIndex][rowIndex] }.joinToString("")
            }
            .map { it.reversed() }, id = id, friends = friends
        )

        fun flipVertical() = Tile(
            pattern = pattern
                .mapIndexed { rowIndex, _ -> pattern[pattern.size - rowIndex - 1] }, id = id, friends = friends
        )

        fun flipHorizontal() = Tile(
            pattern = pattern
                .mapIndexed { rowIndex, row ->
                    row.mapIndexed { columnIndex, _ -> pattern[rowIndex][pattern[0].length - columnIndex - 1] }
                        .joinToString("")
                }, id = id, friends = friends
        )

        fun print() {
            println("Pattern ${id}:")
            println(pattern.joinToString("\n"))
        }

        fun join(tile: Tile) = Tile(
            id = id,
            pattern = pattern
                .zip(tile.pattern)
                .map { (a, b) -> a + b },
            friends = friends
        )

        fun noFrames(): Tile = Tile(
            id = id,
            pattern = pattern
                .drop(1)
                .dropLast(1)
                .map { it.drop(1).dropLast(1) },
            friends = friends
        )

        fun allMonsters(): Int = rotates().map { it.monsters() }.sum()

        fun monsters(): Int {
            var counter = 0
            (0 until pattern.size - MONSTER.size + 1).forEach { row ->
                (0 until pattern[0].length - MONSTER[0].length + 3).forEach { column ->
                    pattern
                        .drop(row)
                        .take(MONSTER.size)
                        .map { it.drop(column).take(MONSTER[0].length) }
                        .mapIndexed { col, line -> line.mapIndexed { row, c -> if(MONSTER[col][row] == '#') c == '#' else true }.all { it }}
                        .takeIf { rows -> rows.all { it } }
                        ?.run { counter++ }
                }
            }
            return counter
        }

        companion object {

            val MONSTER = listOf(
                "                  # ",
                "#    ##    ##    ###",
                " #  #  #  #  #  #   "
            )

        }
    }

}

fun main() {
    val tiles: List<JurassicJigsaw.Tile> = lines("20JurassicJigsaw", "\n\n")
        .map { it.split("\n") }
        .map { JurassicJigsaw.Tile(it.first().drop(5).dropLast(1).toInt(), it.drop(1)) }
    val jigsaw = JurassicJigsaw(tiles.toMutableList())
    val arrangement = tryArrange(jigsaw)
    val rotation = jigsaw.rotate(arrangement)

    val pattern: List<List<JurassicJigsaw.Tile>> = rotation.map { row -> row.map { it.noFrames() } }

    val result = pattern.flatMap { row ->
        row.reduce { acc, tile -> acc.join(tile) }.pattern
    }

    val finalTile = JurassicJigsaw.Tile(0, result)

    println(finalTile.allMonsters())

    println(finalTile.pattern.map { row -> row.count { it == '#' } }.sum())
}

fun tryArrange(jigsaw: JurassicJigsaw): List<List<JurassicJigsaw.Tile>> {
    return try {
        jigsaw.arrange()
    } catch (e: Exception) {
        jigsaw.tiles.shuffle()
        tryArrange(jigsaw)
    }
}
