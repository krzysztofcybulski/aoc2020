package me.kcybulski

import me.kcybulski.utils.lines
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

class RainRisk() {
}

data class WayPoint(val degrees: Double, val distance: Int) {

    fun north() = (distance * sin(degrees)).roundToInt()
    fun east() = (distance * cos(degrees)).roundToInt()

    companion object {

        fun from(x: Int, y: Int) = WayPoint(
            atan2(y.toDouble(), x.toDouble()),
            sqrt(x.toDouble().pow(2.0) + y.toDouble().pow(2.0)).roundToInt()
        )
    }
}

data class FerryPosition(val north: Int, val east: Int, val wayPoint: WayPoint) {

    fun result() = north.absoluteValue + east.absoluteValue
}

sealed class Command {

    abstract fun next(position: FerryPosition): FerryPosition

}

class Move(private val direction: Direction, private val move: Int) : Command() {
    override fun next(position: FerryPosition): FerryPosition = position.copy(
        wayPoint = when (direction) {
            Direction.NORTH -> WayPoint.from(y = position.wayPoint.north() + move, x = position.wayPoint.east())
            Direction.EAST -> WayPoint.from(y = position.wayPoint.north(), x = position.wayPoint.east() + move)
            Direction.SOUTH -> WayPoint.from(y = position.wayPoint.north() - move, x = position.wayPoint.east())
            Direction.WEST -> WayPoint.from(y = position.wayPoint.north(), x = position.wayPoint.east() - move)
        }
    )
}

class Rotate(private val rotation: Rotation, private val degrees: Int) : Command() {
    override fun next(position: FerryPosition): FerryPosition = position.copy(
        wayPoint = position.wayPoint.copy(
            position.wayPoint.degrees + ((if (rotation == Rotation.RIGHT) -degrees else degrees) * PI / 180)
        )
    )
}

class Forward(private val move: Int) : Command() {
    override fun next(position: FerryPosition): FerryPosition {
        return position.copy(north = position.north + (position.wayPoint.north() * move), east = position.east + (position.wayPoint.east() * move))
    }
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

    val wayPoint = WayPoint.from(10, 1)
    val startPosition = FerryPosition(0, 0, wayPoint)
    val endPosition = lines.fold(startPosition) { acc, pos -> pos.next(acc) }

    println(endPosition.result())
}
