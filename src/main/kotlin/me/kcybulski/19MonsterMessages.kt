package me.kcybulski

import me.kcybulski.utils.lines

class Pattern(rules: List<String>) {

    private val assRules = rules.associateBy(
        { it.takeWhile { it != ':' }.toInt() },
        { it.dropWhile { it != ':' }.drop(2) }
    )

    private val rules: MutableMap<Int, Rule> = mutableMapOf()
    private val pattern: Regex

    init {
        add(0)
        pattern = this.rules[0]!!.asStringRegex().toRegex()
    }

    fun matches(line: String) = pattern.matches(line)

    private fun add(index: Int): Rule {
        return when (val content = assRules[index]!!) {
            "\"a\"" -> Rule.SimpleRule(index, "a")
            "\"b\"" -> Rule.SimpleRule(index, "b")
            else -> {
                val rule = Rule.LoopRule(index, emptyList())
                val orRules = content
                    .split(" | ")
                    .map { or ->
                        or.split(" ").map { it.toInt() }
                            .map { if(it == index) rule else rules[it] ?: add(it) }
                            .toMutableList()
                    }
                rule.rules = orRules
                rule
            }
        }.also { rules[index] = it }
    }

    sealed class Rule(
        open val index: Int
    ) {

        abstract fun asStringRegex(l: Int = 0): String

        class SimpleRule(
            override val index: Int,
            val match: String
        ) : Rule(index) {
            override fun asStringRegex(l: Int) = match
        }

        class LoopRule(
            override val index: Int,
            var rules: List<List<Rule>>
        ) : Rule(index) {

            override fun asStringRegex(l: Int): String {
                return if (l > 100)
                    ""
                else
                    rules
                        .joinToString("|", "(", ")") {
                            it.joinToString("") { it.asStringRegex(l + 1) }
                        }
            }
        }
    }

}

fun main() {
    val lines = lines("19MonsterMessages")
    val pattern = Pattern(lines.take(134))

    val texts = lines.drop(134)
    println(texts
        .filter { pattern.matches(it) }
        .size)
}
