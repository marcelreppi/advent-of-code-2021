package day10

import java.io.File

val openBrackets = listOf('(', '[', '{', '<')
val closingBrackets = listOf(')', ']', '}', '>')

val openToClosingMap = (openBrackets zip closingBrackets).toMap()

fun isCorrupted(line: String): Char? {
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

fun findCompletingCharacters(line: String): List<Char> {
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

fun main() {
    val input = File("./src/main/kotlin/day10/input.txt").readLines()

    val corruptPoints = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )
    val completePoints = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )

    val corruptCharacters = mutableListOf<Char>()
    val incompleteLineScores = mutableListOf<Long>()
    for (line in input) {
        val corruptCharacter = isCorrupted(line)
        if (corruptCharacter != null) {
            corruptCharacters.add(corruptCharacter)
        } else {
            val completingCharacters = findCompletingCharacters(line)
            if (completingCharacters.isNotEmpty()) {
                val score = completingCharacters
                    .mapNotNull { completePoints[it]?.toLong() }
                    .reduce { acc, p -> (acc * 5) + p  }
                incompleteLineScores.add(score)
            }
        }
    }

    val syntaxErrorScore = corruptCharacters.mapNotNull { corruptPoints[it] }.sum()
    println(syntaxErrorScore)

    val sortedScores = incompleteLineScores.sorted()
    val medianScore = if (sortedScores.size % 2 == 0) {
        ((sortedScores[sortedScores.size / 2] + sortedScores[sortedScores.size / 2 - 1]) / 2)
    } else {
        (sortedScores[sortedScores.size / 2])
    }
    println(medianScore)
}
