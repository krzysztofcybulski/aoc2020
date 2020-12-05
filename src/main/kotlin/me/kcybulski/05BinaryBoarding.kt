package me.kcybulski

class Plane(private val takenSeats: Collection<Seat>) {

    fun findEmptySeats() = (takenSeats.minOf { it.id }..takenSeats.maxOf { it.id }) - takenSeats.map { it.id }

}

class Seat(row: Int, column: Int) {

    val id = row * 8 + column

    companion object {

        fun from(description: String): Seat {
            val row = binary(description.substring(0, 7).map { it == 'B' }, 128)
            val column = binary(description.substring(7).map { it == 'R' }, 8)
            return Seat(row, column)
        }

        private fun binary(rights: List<Boolean>, max: Int, min: Int = 0): Int = when (rights.firstOrNull()) {
            true -> binary(rights.drop(1), max = max, min = (max - min) / 2 + min)
            false -> binary(rights.drop(1), max = (max - min) / 2 + min, min = min)
            null -> min
        }

    }

}

fun main() {
    val plane = Plane(lines("05BinaryBoarding").map { Seat.from(it) })
    println(plane.findEmptySeats())
}
