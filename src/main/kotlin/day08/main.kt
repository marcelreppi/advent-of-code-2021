package day08

import java.io.File

// Part 1
fun countUniqueDigits(outputDigits: List<String>): Int {
    return outputDigits.count { listOf(2,3,4,7).contains(it.length) }
}

// Part 2
fun decodeOutputDigit(inputSignals: List<String>, outputDigits: List<String>): Int {
    val onePattern = inputSignals.find { it.length == 2 }
    val commonWithOne = { digitString: String -> digitString.count { onePattern?.contains(it) ?: false }}

    val fourPattern = inputSignals.find { it.length == 4 }
    val commonWithFour = { digitString: String -> digitString.count { fourPattern?.contains(it) ?: false }}

    var outputDigit = ""
    for (digitString in outputDigits) {
        val identifiers = listOf(digitString.length, commonWithOne(digitString), commonWithFour(digitString))
        outputDigit += when (identifiers) {
            listOf(2,2,2) -> "1"
            listOf(5,1,2) -> "2"
            listOf(5,2,3) -> "3"
            listOf(4,2,4) -> "4"
            listOf(5,1,3) -> "5"
            listOf(6,1,3) -> "6"
            listOf(3,2,2) -> "7"
            listOf(7,2,4) -> "8"
            listOf(6,2,4) -> "9"
            listOf(6,2,3) -> "0"
            else -> ""
        }
    }

    return outputDigit.toInt()
}

fun main() {
    val input = File("./src/main/kotlin/day08/input.txt").readLines()

    var totalUniqueDigits = 0
    var totalOutputDigitSum = 0
    for (line in input) {
        val (inputSignalString, outputDigitString) = line.split(" | ")
        val inputSignals = inputSignalString.split(" ")
        val outputDigits = outputDigitString.split(" ")
        totalUniqueDigits += countUniqueDigits(outputDigits)
        totalOutputDigitSum += decodeOutputDigit(inputSignals, outputDigits)
    }

    println(totalUniqueDigits)
    println(totalOutputDigitSum)

}