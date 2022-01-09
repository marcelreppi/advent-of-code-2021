class Day03(private val input: List<String>) {
    private fun determineGammaRate(report: List<String>): String {
        val sumPerBit = MutableList(report.first().length) { 0 }

        for (entry in report) {
            entry.forEachIndexed { index, n ->
                sumPerBit[index] = sumPerBit[index] + n.digitToInt()
            }
        }

        return sumPerBit.map { if (it > report.size / 2) 1 else 0 }.joinToString("")
    }

    private fun determineEpsilonRate(gammaRate: String): String {
        return gammaRate.map { if (it.digitToInt() == 1) 0 else 1 }.joinToString("")
    }

    private tailrec fun filterRatingsForCommonBit(report: List<String>, mostCommon: Boolean, bitIndex: Int = 0): String {
        if (report.size == 1) return report.first()

        val (entriesWith1AtIndex, entriesWith0AtIndex) = report.partition { entry ->
            entry[bitIndex].toString() == "1"
        }

        val nextReport = if (mostCommon) {
            if (entriesWith1AtIndex.size >= report.size / 2) entriesWith1AtIndex else entriesWith0AtIndex
        } else {
            if (entriesWith0AtIndex.size <= report.size / 2) entriesWith0AtIndex else entriesWith1AtIndex
        }

        return filterRatingsForCommonBit(nextReport, mostCommon, bitIndex + 1)
    }

    fun solvePart1(): Int {
        val gammaRate = determineGammaRate(input)
//        println(gammaRate)
        val epsilonRate = determineEpsilonRate(gammaRate)
//        println(epsilonRate)
        val powerConsumption = gammaRate.toInt(2) * epsilonRate.toInt(2)
        return powerConsumption
    }

    fun solvePart2(): Int {
        val oxygenGeneratorRating = filterRatingsForCommonBit(input, true)
//        println(oxygenGeneratorRating)
        val co2ScrubberRating = filterRatingsForCommonBit(input, false)
//        println(co2ScrubberRating)
        val lifeSupportRating = oxygenGeneratorRating.toInt(2) * co2ScrubberRating.toInt(2)
        return lifeSupportRating
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day03_example.txt")
    val exampleSolutionPart1 = Day03(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day03(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day03.txt")
    val solutionPart1 = Day03(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day03(input).solvePart2()
    println(solutionPart2)
}