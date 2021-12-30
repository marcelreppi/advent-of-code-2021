package day14

import java.io.File

// Very naive solution
fun part1(initialPolymer: String, rules: Map<String, Char>) {
    var polymer = initialPolymer
    var letterCount = polymer.groupingBy { it }.eachCount()

    for (r in 1..10) {
        val insertions = mutableListOf<Pair<Int, Char>>()

        for (i in 0..polymer.length - 2) {
            val pair = "${polymer[i]}${polymer[i+1]}"
            val insertedLetter = rules[pair]
            if (insertedLetter != null) {
                insertions.add(Pair(i + 1, insertedLetter))
            }
        }

        val nextPolymer = polymer.toMutableList()
        val totalInsertions = insertions.size
        while (insertions.isNotEmpty()) {
            val (index, letter) = insertions.removeFirst()
            val indexShift = (totalInsertions - 1) - insertions.size
            nextPolymer.add(index + indexShift, letter)
        }

        letterCount = nextPolymer.groupingBy { it }.eachCount()
        polymer = nextPolymer.joinToString("")
    }

    val mostCommonLetterQuantity = letterCount.values.maxOrNull() ?: 0
    val leastCommonLetterQuantity = letterCount.values.minOrNull() ?: 0
    println(mostCommonLetterQuantity)
    println(leastCommonLetterQuantity)
    println(mostCommonLetterQuantity - leastCommonLetterQuantity)
}

// Idea: Keep track of total count of letter combinations and process all occurrences of each combination at once
fun part2(initialPolymer: String, rules: Map<String, Char>) {
    var combinationsCounter = mutableMapOf<String, Long>()

    for (i in 0..initialPolymer.length - 2) {
        val combination = "${initialPolymer[i]}${initialPolymer[i + 1]}"
        val combinationCount = combinationsCounter[combination]
        if (combinationCount == null) {
            combinationsCounter[combination] = 1
        } else {
            combinationsCounter[combination] = combinationCount + 1
        }
    }

    var totalLetterCount = mutableMapOf<Char, Long>()
    for (r in 1..40) {
        val nextCombinationsCounter = mutableMapOf<String, Long>()
        val nextTotalLetterCount = mutableMapOf<Char, Long>(initialPolymer.last() to 1)
        for ((combination, count) in combinationsCounter) {
            val insertedLetter = rules[combination]
            if (insertedLetter != null) {
                val newCombination1 = combination[0] + insertedLetter.toString()
                val newCombination2 = insertedLetter.toString() + combination[1]

                for (newCombination in listOf(newCombination1, newCombination2)) {
                    // Count new combinations
                    val newCombinationCount = nextCombinationsCounter[newCombination]
                    if (newCombinationCount == null) {
                        nextCombinationsCounter[newCombination] = count
                    } else {
                        nextCombinationsCounter[newCombination] = newCombinationCount + count
                    }

                    // Count letters
                    // Count only first letter because second one is the first of another combination
                    // Exception: Last combination but last letter will always be the last letter of the polymer.
                    // That's why nextTotalLetterCount is initialized with the last letter of the polymer = 1
                    val firstLetter = newCombination[0]
                    val letterCount = nextTotalLetterCount[firstLetter]
                    if (letterCount == null) {
                        nextTotalLetterCount[firstLetter] = count
                    } else {
                        nextTotalLetterCount[firstLetter] = letterCount + count
                    }
                }
            } else {
                nextCombinationsCounter[combination] = count
            }
        }
        combinationsCounter = nextCombinationsCounter
        totalLetterCount = nextTotalLetterCount
    }

    val mostCommonLetterQuantity = totalLetterCount.values.maxOrNull() ?: 0
    val leastCommonLetterQuantity = totalLetterCount.values.minOrNull() ?: 0
    println(mostCommonLetterQuantity)
    println(leastCommonLetterQuantity)
    println(mostCommonLetterQuantity - leastCommonLetterQuantity)
}

fun main() {
    val input = File("./src/main/kotlin/day14/input.txt").readLines()

    val rules = mutableMapOf<String, Char>()

    for (line in input.subList(2, input.size)) {
        val (pattern, insertion) = line.split(" -> ")
        rules[pattern] = insertion.toCharArray().first()
    }

    val polymer = input.first()

//    part1(polymer, rules)
    part2(polymer, rules)

}
