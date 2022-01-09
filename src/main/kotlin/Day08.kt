class Day08(input: List<String>) {
    private val inputOutputDigits = input.map { line ->
        line.split(" | ").let {
            Pair(it.first().split(" "), it.last().split(" "))
        }
    }

    private fun countUniqueDigits(outputDigits: List<String>): Int {
        return outputDigits.count { listOf(2, 3, 4, 7).contains(it.length) }
    }

    private fun decodeOutputDigit(inputSignals: List<String>, outputDigits: List<String>): Int {
        val onePattern = inputSignals.find { it.length == 2 }
        val commonWithOne = { digitString: String -> digitString.count { onePattern?.contains(it) ?: false } }

        val fourPattern = inputSignals.find { it.length == 4 }
        val commonWithFour = { digitString: String -> digitString.count { fourPattern?.contains(it) ?: false } }

        var outputDigit = ""
        for (digitString in outputDigits) {
            val identifiers = listOf(digitString.length, commonWithOne(digitString), commonWithFour(digitString))
            outputDigit += when (identifiers) {
                listOf(2, 2, 2) -> "1"
                listOf(5, 1, 2) -> "2"
                listOf(5, 2, 3) -> "3"
                listOf(4, 2, 4) -> "4"
                listOf(5, 1, 3) -> "5"
                listOf(6, 1, 3) -> "6"
                listOf(3, 2, 2) -> "7"
                listOf(7, 2, 4) -> "8"
                listOf(6, 2, 4) -> "9"
                listOf(6, 2, 3) -> "0"
                else -> ""
            }
        }

        return outputDigit.toInt()
    }

    fun solvePart1(): Int {
        return inputOutputDigits.sumOf { countUniqueDigits(it.second) }
    }

    fun solvePart2(): Int {
        return inputOutputDigits.sumOf { decodeOutputDigit(it.first, it.second) }
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day08_example.txt")
    val exampleSolutionPart1 = Day08(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day08(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day08.txt")
    val solutionPart1 = Day08(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day08(input).solvePart2()
    println(solutionPart2)
}