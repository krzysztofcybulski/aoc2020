package me.kcybulski

import me.kcybulski.utils.lines
import java.lang.Math.pow
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.pow

class ComboBreaker(val card: Int, val door: Int) {


}

fun main() {
    val lines = lines("25ComboBreaker")
    val card = lines[0].toLong()
    val door = lines[1].toLong()

    var count = 0
    var n = 1L

    while(n != card) {
        n *= 7
        n %= 20201227
        count++
    }

    println(BigInteger.valueOf(door).pow(count).mod(BigInteger.valueOf(20201227)))
}
