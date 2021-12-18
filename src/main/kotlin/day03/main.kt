package day03

import java.io.File

fun determineGammaRate(report: List<String>): String {
    val sumPerBit = MutableList(report.first().length) { 0 }

    for (entry in report) {
        entry.forEachIndexed { index, n ->
            sumPerBit[index] = sumPerBit[index] + n.digitToInt()
        }
    }

    return sumPerBit.map { if (it > report.size / 2) 1 else 0 }.joinToString("")
}

fun determineEpsilonRate(gammaRate: String): String {
    return gammaRate.map { if (it.digitToInt() == 1) 0 else 1 }.joinToString("")
}

tailrec fun filterRatingsForCommonBit(report: List<String>, mostCommon: Boolean, bitIndex: Int = 0): String {
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

fun main(args: Array<String>) {
    val report = File("./src/main/kotlin/day03/input.txt").readLines()

    val gammaRate = determineGammaRate(report)
    println(gammaRate)
    val epsilonRate = determineEpsilonRate(gammaRate)
    println(epsilonRate)
    val powerConsumption = gammaRate.toInt(2) * epsilonRate.toInt(2)
    println(powerConsumption)


    val oxygenGeneratorRating = filterRatingsForCommonBit(report, true)
    println(oxygenGeneratorRating)
    val co2ScrubberRating = filterRatingsForCommonBit(report, false)
    println(co2ScrubberRating)
    val lifeSupportRating = oxygenGeneratorRating.toInt(2) * co2ScrubberRating.toInt(2)
    println(lifeSupportRating)

}
