package me.kcybulski

import me.kcybulski.utils.lines

class GroupAnswers(private val personAnswers: List<PersonAnswers>) {

    fun questionsOr() = personAnswers
        .map { it.answers }
        .reduce { acc, a -> acc + a }

    fun questionsAnd() = personAnswers
        .map { it.answers }
        .reduce { acc, a -> acc.intersect(a) }

    companion object {
        fun from(string: String) = GroupAnswers(string.split("\n").map { PersonAnswers.from(it) })
    }
}

class PersonAnswers(val answers: Set<Char>) {

    companion object {
        fun from(string: String) = PersonAnswers(string.toSet())
    }
}

fun main() {
    val groups: List<GroupAnswers> = lines("06CustomCustoms", "\n\n").map { GroupAnswers.from(it) }
    println(groups.map { it.questionsOr().size }.sum())
    println(groups.map { it.questionsAnd().size }.sum())
}
