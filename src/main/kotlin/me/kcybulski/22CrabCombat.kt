package me.kcybulski

import me.kcybulski.utils.lines
import java.lang.RuntimeException
import java.util.*

class CrabCombat(val player1: Deck, val player2: Deck) {

    val games = mutableSetOf<Pair<String, String>>()

    fun play(): Deck {
        if (games.contains(player1.toString() to player2.toString())) {
            return player1
        }
        games.add(player1.toString() to player2.toString())

        return if(!player1.hasCards()) {
            player2
        } else if(!player2.hasCards()) {
            player1
        } else {
            val round = playRound()
            play()
        }
    }

    fun playRound(): Deck {
        val card1 = player1.get()
        val card2 = player2.get()

        if(player1.cardsLeft() >= card1 && player2.cardsLeft() >= card2) {
            val p1 = Deck(LinkedList(player1.cards.take(card1)))
            val p2 = Deck(LinkedList(player2.cards.take(card2)))
            return when(CrabCombat(p1, p2).play()) {
                p1 -> player1.also { it.add(card1, card2) }
                p2 -> player2.also { it.add(card2, card1) }
                else -> throw RuntimeException("No such player")
            }
        }

        return when {
            card1 > card2 -> player1.also { it.add(card1, card2) }
            card2 > card1 -> player2.also { it.add(card2, card1) }
            else -> throw RuntimeException("Draw")
        }
    }

    data class Deck(val cards: Queue<Int>) {

        fun hasCards(): Boolean = cards.isNotEmpty()
        fun get(): Int = cards.poll()
        fun add(vararg cards: Int) {
            cards.forEach { this.cards.offer(it) }
        }
        fun cardsLeft() = cards.size

        fun points(): Long = cards.reversed()
            .zip(1..cards.size)
            .map { it.first * it.second.toLong() }
            .sum()

        override fun hashCode() = cards.toIntArray().hashCode()

        override fun toString() = cards.joinToString(",")

    }

}

fun main() {
    val lines = lines("22CrabCombat")

    val player1 = lines.drop(1).takeWhile { it != "Player 2:" }.map { it.toInt() }
    val player2 = lines.drop(2).drop(player1.size).map { it.toInt() }

    val game = CrabCombat(CrabCombat.Deck(LinkedList(player1)), CrabCombat.Deck(LinkedList(player2)))
    println(game.play().points())
}
