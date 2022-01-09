class Day14(input: List<String>) {
    private val initialPolymer: String = input.first()
    private val rules: Map<String, Char> = parseRules(input)

    private fun parseRules(input: List<String>): Map<String, Char> {
        return input.drop(2).associate { line ->
            val (pattern, insertion) = line.split(" -> ")
            pattern to insertion.toCharArray().first()
        }
    }

    private fun solveNaive(): Int {
        var polymer = initialPolymer
        var letterCount = polymer.groupingBy { it }.eachCount()

        for (r in 1..10) {
            val insertions = mutableListOf<Pair<Int, Char>>()

            for (i in 0..polymer.length - 2) {
                val pair = "${polymer[i]}${polymer[i + 1]}"
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
        return mostCommonLetterQuantity - leastCommonLetterQuantity
    }

    // Idea: Keep track of total count of letter combinations and process all occurrences of each combination at once
    private fun solveSmart(): Long {
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
        return mostCommonLetterQuantity - leastCommonLetterQuantity
    }

    fun solvePart1(): Int {
        return solveNaive()
    }


    fun solvePart2(): Long {
        return solveSmart()
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day14_example.txt")
    val exampleSolutionPart1 = Day14(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day14(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day14.txt")
    val solutionPart1 = Day14(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day14(input).solvePart2()
    println(solutionPart2)
}