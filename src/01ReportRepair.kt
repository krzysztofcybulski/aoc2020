class ReportRepair(expenses: Collection<Int>) {

    val expenses = HashSet(expenses)

    fun result(index: Int = 0): Int = expenses
        .find { expenses.contains(2020 - it) }
        ?.let { it * (2020 - it) }
        ?: throw IllegalStateException("No such expenses")

}

fun main(args: Array<String>) {
    println(ReportRepair(args.map { it.toInt() }).result())
}