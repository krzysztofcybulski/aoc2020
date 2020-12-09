package me.kcybulski

import java.util.regex.Matcher

interface PasswordPolicy {

    fun isValid(password: String): Boolean
}

data class PasswordPolicy1(
    val min: Int,
    val max: Int,
    val letter: Char
) : PasswordPolicy {

    override fun isValid(password: String): Boolean = password.count { it == letter } in min..max
}

data class PasswordPolicy2(
    val position1: Int,
    val position2: Int,
    val letter: Char
) : PasswordPolicy {

    override fun isValid(password: String): Boolean =
        (password[position1 - 1] == letter).xor(password[position2 - 1] == letter)
}

fun main() {
    val pattern = "^(\\d+)-(\\d+) ([a-z]): ([a-z]+)\$".toPattern()

    val passwords = lines("02PasswordPhilosophy")
        .map { pattern.matcher(it) }

    println(countValidPasswords(passwords) { a, b, c -> PasswordPolicy1(a, b, c)})
    println(countValidPasswords(passwords) { a, b, c -> PasswordPolicy2(a, b, c)})
}

fun countValidPasswords(passwords: List<Matcher>, policy: (Int, Int, Char) -> PasswordPolicy): Int =
    passwords.count {
        it.matches()
        policy(
            it.group(1).toInt(),
            it.group(2).toInt(),
            it.group(3).elementAt(0)
        ).isValid(it.group(4))
    }
