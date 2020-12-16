package me.kcybulski

import me.kcybulski.utils.lines

class TicketTranslation(
    private val rules: List<Rule>,
    private val ticket: Ticket,
    private val nearbyTickets: List<Ticket>
) {

    fun getInvalid() = nearbyTickets.flatMap { it.getInvalid(rules) }

    class Rule(val name: String, private val ranges: Set<Range>) {
        fun contains(n: Int) = ranges.any { it.contains(n) }

        companion object {

            private val regex = "^(.+): ([0-9]+)-([0-9]+) or ([0-9]+)-([0-9]+)\$".toRegex()

            fun from(line: String) = regex
                .matchEntire(line)
                ?.groupValues
                ?.let { Rule(it[1], setOf(Range(it[2].toInt(), it[3].toInt()), Range(it[4].toInt(), it[5].toInt()))) }
        }
    }

    class Range(private val from: Int, private val to: Int) {
        fun contains(n: Int) = n in from..to
    }

    class Ticket(private val values: List<Int>) {

        fun getInvalid(rules: List<Rule>) = values
            .filter { v -> rules.all { !it.contains(v) } }

        companion object {

            fun from(line: String) = Ticket(line.split(",").map { it.toInt() })
        }
    }

}

fun main() {
    val lines = lines("16TicketTranslation")

    val rules = lines
        .takeWhile { it != "your ticket" }
        .mapNotNull { TicketTranslation.Rule.from(it) }

    val ticket = TicketTranslation.Ticket.from(lines.drop(rules.size + 1).first())
    val nearbyTickets = lines.drop(rules.size + 3).map { TicketTranslation.Ticket.from(it) }

    val translation = TicketTranslation(rules, ticket, nearbyTickets)
    println(translation.getInvalid().sum())
}
