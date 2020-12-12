package me.kcybulski

import me.kcybulski.utils.lines
import java.lang.RuntimeException
import kotlin.math.absoluteValue

class RainRisk() {
}

data class FerryPosition(val direction: Direction, val north: Int, val east: Int) {

    fun result() = north.absoluteValue + east.absoluteValue
}

sealed class Command {

    abstract fun next(position: FerryPosition): FerryPosition

}

class Move(private val direction: Direction, private val move: Int) : Command() {
    override fun next(position: FerryPosition): FerryPosition = when (direction) {
        Direction.NORTH -> position.copy(north = position.north + move)
        Direction.EAST -> position.copy(east = position.east + move)
        Direction.SOUTH -> position.copy(north = position.north - move)
        Direction.WEST -> position.copy(east = position.east - move)
    }
}

class Rotate(private val rotation: Rotation, private val degrees: Int) : Command() {
    override fun next(position: FerryPosition): FerryPosition {
        val rotateTimes = degrees / 90
        val dirs = if (rotation == Rotation.RIGHT) Direction.values() else Direction.values().reversedArray()
        return position.copy(direction = dirs[(rotateTimes + dirs.indexOf(position.direction)) % 4])
    }
}

class Forward(private val move: Int): Command() {
    override fun next(position: FerryPosition): FerryPosition = Move(position.direction, move).next(position)
}

enum class Direction {

    NORTH, EAST, SOUTH, WEST

}

enum class Rotation {

    LEFT, RIGHT

}

fun main() {
    val lines = lines("12RainRisk")
        .map {
            when (it.first()) {
                'N' -> Move(Direction.NORTH, it.substring(1).toInt())
                'E' -> Move(Direction.EAST, it.substring(1).toInt())
                'S' -> Move(Direction.SOUTH, it.substring(1).toInt())
                'W' -> Move(Direction.WEST, it.substring(1).toInt())
                'R' -> Rotate(Rotation.RIGHT, it.substring(1).toInt())
                'L' -> Rotate(Rotation.LEFT, it.substring(1).toInt())
                'F' -> Forward(it.substring(1).toInt())
                else -> throw IllegalStateException("Not known $it")
            }
        }

    val startPosition = FerryPosition(Direction.EAST, 0, 0)
    val endPosition = lines.fold(startPosition) { acc, pos -> pos.next(acc) }

    println(endPosition.result())
}
