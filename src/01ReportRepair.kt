class ReportRepair(expenses: Collection<Int>) {

    val expenses = HashSet(expenses)

    fun productOf2EntriesThatSumTo(sum: Int): Int = expenses
        .find { expenses.contains(sum - it) }
        ?.let { it * (sum - it) }
        ?: throw IllegalStateException("No such expenses")

}

fun main(args: Array<String>) {
    println(ReportRepair(args.map { it.toInt() }).productOf2EntriesThatSumTo(2020))
}