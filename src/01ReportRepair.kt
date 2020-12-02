class ReportRepair(expenses: Collection<Int>) {

    private val expenses = HashSet(expenses)

    fun productOf2EntriesThatSumTo(sum: Int): Int = productOf2EntriesThatSumTo(sum, expenses)
        ?: throw IllegalStateException("No such expenses")

    fun productOf3EntriesThatSumTo(sum: Int): Int =
        expenses
            .find { productOf2EntriesThatSumTo(sum - it, expenses - it) != null }
            ?.let { it * productOf2EntriesThatSumTo(sum - it, expenses - it)!! }
            ?: throw IllegalStateException("No such expenses")

    private fun productOf2EntriesThatSumTo(sum: Int, list: Set<Int>): Int? = list
        .find { list.contains(sum - it) }
        ?.let { it * (sum - it) }

}

fun main(args: Array<String>) {
    println(ReportRepair(args.map { it.toInt() }).productOf2EntriesThatSumTo(2020))
    println(ReportRepair(args.map { it.toInt() }).productOf3EntriesThatSumTo(2020))
}
