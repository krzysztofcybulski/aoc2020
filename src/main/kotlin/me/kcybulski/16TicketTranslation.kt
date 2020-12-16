package me.kcybulski

import me.kcybulski.utils.lines

data class TicketTranslation(
    private val rules: List<Rule>,
    private val ticket: Ticket,
    private val nearbyTickets: List<Ticket>
) {

    fun withoutInvalid() = copy(nearbyTickets = nearbyTickets.filterNot { it.isInvalid(rules) })

    fun getInvalidMultiply() = nearbyTickets
        .flatMap { it.getInvalidValues(rules) }
        .reduce { a, b -> a + b }

    fun ordered() = copy(rules = rules
        .indices
        .asSequence()
        .map { index -> nearbyTickets.map { it.values[index] } }
        .mapIndexed { index, list -> Column(rules.filter { rule -> list.all { rule.contains(it) } }, index) }
        .sortedBy { it.rules.size }
        .fold(emptyList<Column>()) { acc, newColumn -> acc + (newColumn.copy(rules = newColumn.rules - acc.flatMap { it.rules })) }
        .sortedBy { it.index }
        .toList()
        .flatMap { it.rules }
    )

    fun getDeparture() = rules
        .zip(ticket.values)
        .filter { (rule, _) -> rule.name.startsWith("departure") }
        .map { (_, ticketValue) -> ticketValue.toLong() }
        .reduce { a, b -> a * b }

    data class Column(val rules: List<Rule>, val index: Int)

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

    class Ticket(val values: List<Int>) {

        fun isInvalid(rules: List<Rule>) = values.any { v -> rules.none { it.contains(v) } }

        fun getInvalidValues(rules: List<Rule>) = values.filter { v -> rules.none { it.contains(v) } }

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

    println(translation.getInvalidMultiply())

    translation
        .withoutInvalid()
        .ordered()
        .getDeparture()
        .run { println(this) }
}
