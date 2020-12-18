package me.kcybulski

import me.kcybulski.utils.lines
import java.lang.RuntimeException

class OperationOrder(val line: String) {

    fun calculate(expresion: String = line): Long {
        if (isSimple(expresion)) {
            val splited = expresion
                .split(" ")
            return splited
                .drop(1)
                .windowed(2, 2)
                .map { (op, n) -> op.first() to n.toLong() }
                .fold(splited.first().toLong()) { acc, x -> if (x.first == '*') acc * x.second else acc + x.second }
        } else {
            val find = simpleOperation.find(expresion)!!.value
            return calculate(expresion.replaceFirst(find, calculate(find.substring(1, find.length - 1)).toString()))
        }
    }

    private fun isSimple(expresion: String): Boolean = '(' !in expresion

    interface Calculable {
        fun calculate(x: Int): Int
    }

    data class Expression(val operator: Char, val operations: List<Calculable>): Calculable {

        override fun calculate(x: Int) = operations.fold(x) { acc, op -> op.calculate(acc) }
    }

    data class Operation(val operator: Char, val number: Int): Calculable {
        override fun calculate(x: Int) = when(operator) {
            '+' -> x + number
            '*' -> x * number
            else -> throw RuntimeException("No such operator")
        }
    }

    companion object {
        val simpleOperation = "\\([0-9]+( [*+] [0-9]+)+\\)".toRegex()
    }
}

fun main() {
    val lines = lines("18OperationOrder")
    lines
        .map { OperationOrder(it).calculate() }
        .onEach { println(it) }
        .sum()
        .let { println(it) }

}
