package me.kcybulski

import me.kcybulski.utils.lines

class ConwayCubes(private val cubes: Set<Cube>) {

    fun next() = ConwayCubes(
        cubes
            .flatMap { it.neighbours() }.toSet()
            .filter { shouldLive(it) }.toSet()
    )

    fun size() = cubes.size

    private fun shouldLive(cube: Cube) = cube
        .neighbours()
        .filter { it in cubes }
        .size
        .let { (cube in cubes && it in 2..3) || (cube !in cubes && it == 3) }

    data class Cube(val x: Int, val y: Int, val z: Int, val w: Int) {

        fun neighbours(): Set<Cube> = (-1..1).flatMap { x ->
            (-1..1).flatMap { y ->
                (-1..1).flatMap { z ->
                    (-1..1).map { w ->
                        Cube(
                            x + this.x,
                            y + this.y,
                            z + this.z,
                            w + this.w
                        )
                    }
                }
            }
        }.toSet() - this

    }

}

fun main() {
    val lines = lines("17ConwayCubes")

    val cubes = ConwayCubes(lines.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            c.takeIf { it == '#' }?.let { ConwayCubes.Cube(x, y, 0, 0) }
        }
    }.toSet())

    val finalCubes = (1..6).fold(cubes) { a, _ -> a.next()}
    println(finalCubes.size())
}
