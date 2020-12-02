class PasswordPhilosophy() {
}

data class PasswordPolicy(
    val min: Int,
    val max: Int,
    val letter: Char
) {

    fun isValid(password: String): Boolean = password.count { it == letter } in min..max

}

fun main(args: Array<String>) {
    val lines = object {}.javaClass.getResource("02PasswordPhilosophy.txt")
        .readText()
        .split("\n")
        .filter { it.isNotBlank() }

    val pattern = "^(\\d+)-(\\d+) ([a-z]): ([a-z]+)\$".toPattern()

    val validPasswords = lines
        .map { pattern.matcher(it) }
        .count {
            it.matches()
            PasswordPolicy(
                it.group(1).toInt(),
                it.group(2).toInt(),
                it.group(3).elementAt(0)
            ).isValid(it.group(4))
        }

    println(validPasswords)
}
