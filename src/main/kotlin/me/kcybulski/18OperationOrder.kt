package me.kcybulski

import me.kcybulski.utils.lines
import java.lang.Character.isDigit
import java.util.*
import kotlin.collections.ArrayList


class OperationOrder(val line: String) {

    fun calculate(): Long {
        val stack = Stack<Long>()
        convertToRPN()
            .forEach {
                when (it) {
                    "+" -> stack.push(stack.pop() + stack.pop())
                    "*" -> stack.push(stack.pop() * stack.pop())
                    else -> stack.push(it.toLong())
                }
            }
        return stack.pop()
    }

    private fun convertToRPN(): ArrayList<String> {
        val result = ArrayList<String>()
        val opStack: Stack<String> = Stack()
        line.split(" ").forEach { token ->
            when {
                isDigit(token.first()) -> result.add(token)
                token == "(" -> opStack.push(token)
                token == ")" -> {
                    while (opStack.peek() != "(") {
                        result.add(opStack.pop())
                    }
                    opStack.pop()
                }
                else -> {
                    while (!opStack.isEmpty() && getPriority(opStack.peek()) >= getPriority(token)) {
                        result.add(opStack.pop())
                    }
                    opStack.push(token)
                }
            }
        }
        while (!opStack.isEmpty()) {
            result.add(opStack.pop())
        }
        return result
    }

    private fun getPriority(op: String): Int = when (op) {
        "(" -> 0
        "+" -> 2
        else -> 1
    }
}

fun main() {
    val lines = lines("18OperationOrder")
    lines
        .map { it.replace("(", "( ").replace(")", " )") }
        .map { OperationOrder(it).calculate() }
        .onEach { println(it) }
        .sum()
        .let { println(it) }

}
