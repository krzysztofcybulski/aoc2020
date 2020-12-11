package me.kcybulski

import me.kcybulski.utils.lines

sealed class FerrySeat

object EmptySeat : FerrySeat()
object TakenSeat : FerrySeat()

data class Position(val row: Int, val column: Int)

data class Ferry(
    val seats: MutableMap<Position, FerrySeat>
) {

    val columns = seats.keys.maxByOrNull { it.column }?.column ?: 1
    val rows = seats.keys.maxByOrNull { it.row }?.row ?: 1

    fun taken() = seats.values.count { it is TakenSeat }

}

fun main() {
    val seats: MutableMap<Position, FerrySeat> = mutableMapOf()
    val lines = lines("11SeatingSystem")
        .forEachIndexed { row, sr ->
            sr.forEachIndexed { column, s ->
                if (s == 'L') {
                    seats[Position(row, column)] = EmptySeat
                }
            }
        }
    val ferry = Ferry(seats)
    val finalFerry = next(ferry)
    print(finalFerry)
    println(finalFerry.taken())
}

fun next(ferry: Ferry): Ferry {
    val newSeats: MutableMap<Position, FerrySeat> = mutableMapOf()
    ferry.seats.forEach { (pos, seat) ->
        val occupied = getOccupiedNextTo(pos, ferry)
        newSeats[pos] = when {
            occupied == 0 -> TakenSeat
            occupied > 4 -> EmptySeat
            else -> seat
        }
    }
    val newFerry = Ferry(newSeats)
    return if(newFerry == ferry) {
        newFerry
    } else {
        next(newFerry)
    }
}

fun getOccupiedNextTo(position: Position, ferry: Ferry) =
    isOccupied(position, ferry) { it.copy(row = it.row - 1, column = it.column - 1) } +
    isOccupied(position, ferry) { it.copy(row = it.row - 1) } +
    isOccupied(position, ferry) { it.copy(row = it.row - 1, column = it.column + 1) } +
    isOccupied(position, ferry) { it.copy(column = it.column - 1) } +
    isOccupied(position, ferry) { it.copy(column = it.column + 1) } +
    isOccupied(position, ferry) { it.copy(row = it.row + 1, column = it.column - 1) } +
    isOccupied(position, ferry) { it.copy(row = it.row + 1) } +
    isOccupied(position, ferry) { it.copy(row = it.row + 1, column = it.column + 1) }

fun isOccupied(pos: Position, ferry: Ferry, change: (Position) -> Position): Int {
    val nextPos = change(pos)
    if(nextPos.column < 0 || nextPos.row < 0 || nextPos.column > ferry.columns || nextPos.row > ferry.rows) {
        return 0
    }
    if(ferry.seats[nextPos] is TakenSeat) {
        return 1
    }
    if(ferry.seats[nextPos] is EmptySeat) {
        return 0
    }
    return isOccupied(nextPos, ferry, change)
}

fun print(ferry: Ferry) {
    (0..ferry.columns).forEach { column ->
        (0..ferry.rows).forEach { row -> print(Position(row, column), ferry) }
        println()
    }
}

fun print(position: Position, ferry: Ferry) {
    val seat = ferry.seats[position]
    print(when(seat) {
        TakenSeat -> "#"
        EmptySeat -> "L"
        else -> "."
    })
}
