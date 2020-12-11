package me.kcybulski

import me.kcybulski.utils.lines

sealed class FerrySeat

object EmptySeat : FerrySeat()
object TakenSeat : FerrySeat()

data class Position(val row: Int, val column: Int)

typealias Ferry = MutableMap<Position, FerrySeat>

fun main() {
    val seats: Ferry = mutableMapOf()
    val lines = lines("11SeatingSystem")
        .forEachIndexed { row, sr ->
            sr.forEachIndexed { column, s ->
                if (s == 'L') {
                    seats[Position(row, column)] = EmptySeat
                }
            }
        }
    val finalFerry = next(seats)
    println(finalFerry.values.count { it is TakenSeat })
}

fun next(ferry: Ferry): Ferry {
    val newFerry: Ferry = mutableMapOf()
    ferry.forEach { (pos, seat) ->
        val occupied = getOccupiedNextTo(pos, ferry)
        newFerry[pos] = when {
            occupied == 0 -> TakenSeat
            occupied > 3 -> EmptySeat
            else -> seat
        }
    }
    return if(newFerry == ferry) {
        newFerry
    } else {
        next(newFerry)
    }
}

fun getOccupiedNextTo(position: Position, ferry: Ferry) =
    isOccupied(position.copy(row = position.row - 1, column = position.column - 1), ferry) +
            isOccupied(position.copy(row = position.row - 1), ferry) +
            isOccupied(position.copy(row = position.row - 1, column = position.column + 1), ferry) +
            isOccupied(position.copy(row = position.row, column = position.column - 1), ferry) +
            isOccupied(position.copy(row = position.row, column = position.column + 1), ferry) +
            isOccupied(position.copy(row = position.row + 1, column = position.column - 1), ferry) +
            isOccupied(position.copy(row = position.row + 1), ferry) +
            isOccupied(position.copy(row = position.row + 1, column = position.column + 1), ferry)

fun isOccupied(position: Position, ferry: Ferry): Int = if (ferry[position] is TakenSeat) 1 else 0

fun print(ferry: Ferry) {
    val columns = ferry.keys.maxByOrNull { it.column }?.column ?: 1
    val rows = ferry.keys.maxByOrNull { it.row }?.row ?: 1
    (0..columns).forEach { column ->
        (0..rows).forEach { row -> print(Position(row, column), ferry) }
        println()
    }
}

fun print(position: Position, ferry: Ferry) {
    val seat = ferry[position]
    print(when(seat) {
        TakenSeat -> "#"
        EmptySeat -> "L"
        else -> "."
    })
}
