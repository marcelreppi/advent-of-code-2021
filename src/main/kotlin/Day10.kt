class Day10(private val input: List<String>) {
    private val openBrackets = listOf('(', '[', '{', '<')
    private val closingBrackets = listOf(')', ']', '}', '>')
    private val openToClosingMap = (openBrackets zip closingBrackets).toMap()
    private val corruptPoints = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )
    private val completePoints = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )

    private fun isCorrupted(line: String): Char? {
        val openBracketStack = mutableListOf<Char>()
        for (c in line) {
            if (openBrackets.contains(c)) {
                openBracketStack.add(c)
            } else {
                val lastOpeningBracket = openBracketStack.removeLast()
                val expectedClosingBracket = openToClosingMap[lastOpeningBracket]
                if (c != expectedClosingBracket) {
                    return c
                }
            }
        }

        return null
    }

    private fun findCompletingCharacters(line: String): List<Char> {
        val openBracketStack = mutableListOf<Char>()
        for (c in line) {
            if (openBrackets.contains(c)) {
                openBracketStack.add(c)
            } else {
                val lastOpeningBracket = openBracketStack.removeLast()
                val expectedClosingBracket = openToClosingMap[lastOpeningBracket]
                if (c != expectedClosingBracket) {
                    println("That should not happen")
                }
            }
        }

        return openBracketStack.reversed().mapNotNull { openToClosingMap[it] }
    }

    fun solvePart1(): Int {
        val syntaxErrorScore = input
            .mapNotNull { line -> corruptPoints[isCorrupted(line)] }
            .sum()
        return syntaxErrorScore
    }

    fun solvePart2(): Long {
        return input
            .filter { isCorrupted(it) == null }
            .map { line ->
                val completingCharacters = findCompletingCharacters(line)
                completingCharacters
                    .mapNotNull { char -> completePoints[char]?.toLong() }
                    .reduce { acc, p -> (acc * 5) + p }
            }
            .sorted()
            .median()
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day10_example.txt")
    val exampleSolutionPart1 = Day10(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day10(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day10.txt")
    val solutionPart1 = Day10(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day10(input).solvePart2()
    println(solutionPart2)
}