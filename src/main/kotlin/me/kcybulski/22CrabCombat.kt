package me.kcybulski

import me.kcybulski.utils.lines
import java.util.*

class CrabCombat(val player1: Deck, val player2: Deck) {

    fun play(): Long {
        if(!player1.hasCards()) {
            return player2.points()
        }
        if(!player2.hasCards()) {
            return player1.points()
        }
        val card1 = player1.get()
        val card2 = player2.get()
        if(card1 > card2) {
            player1.add(card1, card2)
            println("Player 1 won")
        } else if(card2 > card1) {
            player2.add(card2, card1)
            println("Player 2 won")
        } else {
            println("A tie")
        }
        return play()
    }

    class Deck(starting: List<Int>) {

        private val cards: Queue<Int> = LinkedList<Int>(starting)

        fun hasCards(): Boolean = cards.isNotEmpty()
        fun get(): Int = cards.poll()
        fun add(vararg cards: Int) {
            cards.forEach { this.cards.add(it) }
        }
        fun points(): Long = cards.reversed()
            .zip(1..cards.size)
            .map { it.first * it.second.toLong() }
            .sum()

    }

}

fun main() {
    val lines = lines("22CrabCombat")

    val player1 = lines.drop(1).takeWhile { it != "Player 2:" }.map { it.toInt() }
    val player2 = lines.drop(2).drop(player1.size).map { it.toInt() }

    val game = CrabCombat(CrabCombat.Deck(player1), CrabCombat.Deck(player2))
    println(game.play())
}
