data class Passport(
    val byr: String?,
    val iyr: String?,
    val eyr: String?,
    val hgt: String?,
    val hcl: String?,
    val ecl: String?,
    val pid: String?
) {

    fun isNull(): Boolean = (byr == null ||
            iyr == null ||
            eyr == null ||
            hgt == null ||
            hcl == null ||
            ecl == null ||
            pid == null)


    fun isValid(): Boolean {
        if ((byr == null ||
                    iyr == null ||
                    eyr == null ||
                    hgt == null ||
                    hcl == null ||
                    ecl == null ||
                    pid == null
                    )
        ) {
            println("1")
            return false
        }


        if (!isNumber(byr, 4)) {
            println("2: ${byr}")
            return false
        }

        val byrI = byr.toInt()
        if (byrI < 1920 || byrI > 2002) {
            println("3: ${byr}")
            return false
        }

        if (!isNumber(iyr, 4)) {
            println("4: ${iyr}")
            return false
        }

        val iyrI = iyr.toInt()
        if (iyrI < 2010 || iyrI > 2020) {
            println("5: ${iyr}")
            return false
        }

        if (!isNumber(eyr, 4)) {
            println("6: ${eyr}")
            return false
        }

        val eyrI = eyr.toInt()
        if (eyrI < 2020 || eyrI > 2030) {
            println("7: ${eyr}")
            return false
        }

        try {
            val hgtI = hgt.substring(0, hgt.length - 2).toInt()
            if(!hgt.contains("cm") && !hgt.contains("in")) {
                return false;
            }
            if (hgt.contains("cm") && (hgtI < 150 || hgtI > 193)) {
                println("8: ${hgt}")
                return false
            } else if (hgt.contains("in") && (hgtI < 59 || hgtI > 76)) {
                println("9: ${hgt}")
                return false
            }
        } catch (e: Exception) {
            println("10: ${hgt}")
            return false
        }

        if (!color.matches(hcl)) {
            println("11: ${hcl}")
            return false;
        }

        if (ecl !in colors) {
            println("12: ${ecl}")
            return false;
        }

        if (!isNumber(pid, 9)) {
            println("13: ${pid}")
            return false
        }

        return true

    }

    fun isNumber(s: String, length: Int): Boolean {
        val x = s.count { it.isDigit() }
        return s.length == length && x == length
    }

    companion object {

        val color = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})\$".toRegex()
        val colors = listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
    }

}

fun main() {
    val lines = object {}.javaClass.getResource("04PassportProcessing.txt")
        .readText()
        .split("\n\n")
        .filter { it.isNotBlank() }

    val result = lines.map {
        Passport(
            getAfter(it, "byr"),
            getAfter(it, "iyr"),
            getAfter(it, "eyr"),
            getAfter(it, "hgt"),
            getAfter(it, "hcl"),
            getAfter(it, "ecl"),
            getAfter(it, "pid")
        )
    }

    result.forEach {
        if (!it.isNull() && !it.isValid()) {
            println("INVALID: $it")
        }
        if(it.isValid()) {
            println("VALID: $it")
        }
    }

    println(result.count(Passport::isValid))
}

private fun getAfter(line: String, key: String): String? {
    val index = line.indexOf(key)
    if (index == -1) return null
    return line.drop(line.indexOf(key) + key.length + 1).takeWhile { it != ' ' && it != '\n' }
}
