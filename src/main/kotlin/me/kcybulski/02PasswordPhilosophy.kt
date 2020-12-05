package me.kcybulski

data class PasswordPolicy(
    val min: Int,
    val max: Int,
    val letter: Char
) {

    fun isValid(password: String): Boolean = password.count { it == letter } in min..max

}

data class PasswordPolicy2(
    val position1: Int,
    val position2: Int,
    val letter: Char
) {

    fun isValid(password: String): Boolean = (password[position1 - 1] == letter).xor(password[position2 - 1] == letter)

}

fun main() {
    val pattern = "^(\\d+)-(\\d+) ([a-z]): ([a-z]+)\$".toPattern()

    val validPasswords = lines("02PasswordPhilosophy")
        .map { pattern.matcher(it) }
        .count {
            it.matches()
            PasswordPolicy2(
                it.group(1).toInt(),
                it.group(2).toInt(),
                it.group(3).elementAt(0)
            ).isValid(it.group(4))
        }

    println(validPasswords)
}
