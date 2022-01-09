import kotlin.math.ceil
import kotlin.math.floor

class Day18(input: List<String>) {
    private val equations = input.map { parseLine(it) }

    private fun parseLine(line: String): List<SnailNumber> {
        var depth = 0
        val equation = mutableListOf<SnailNumber>()
        for (char in line) {
            when (char) {
                '[' -> depth++
                ']' -> depth--
                ',' -> {}
                else -> equation.add(SnailNumber(char.digitToInt(), depth))
            }
        }
        return equation
    }

    private data class SnailNumber(var value: Int, var depth: Int)

    private fun add(sn1: List<SnailNumber>, sn2: List<SnailNumber>): List<SnailNumber> {
        val sumSn = sn1.map { it.copy() }.toMutableList()
        sumSn.addAll(sn2.map { it.copy() }.toMutableList())
        sumSn.forEach { it.depth += 1 }

        var updated = true
        while (updated) {
            updated = false
            for (i in 0 until sumSn.size) {
                val number = sumSn[i]
                if (number.depth >= 5) {
                    val nextNumber = sumSn[i + 1]
                    if (nextNumber.depth == number.depth) {
                        if (i - 1 >= 0) {
                            val leftNumber = sumSn[i - 1]
                            leftNumber.value += number.value
                        }
                        if (i + 2 < sumSn.size) {
                            val rightNumber = sumSn[i + 2]
                            rightNumber.value += nextNumber.value
                        }
                        sumSn.removeAt(i + 1)
                        sumSn.removeAt(i)
                        sumSn.add(i, SnailNumber(0, number.depth - 1))
                        updated = true
                        break
                    }
                }
            }

            if (!updated) {
                for (i in 0 until sumSn.size) {
                    val number = sumSn[i]
                    if (number.value > 9) {

                        val newSn1 = SnailNumber(
                            floor(((number.value.toDouble() / 2.0))).toInt(), number.depth + 1
                        )
                        val newSn2 = SnailNumber(
                            ceil(((number.value.toDouble() / 2.0))).toInt(), number.depth + 1
                        )
                        sumSn.removeAt(i)
                        sumSn.addAll(i, listOf(newSn1, newSn2))
                        updated = true
                        break
                    }
                }
            }

        }
        return sumSn
    }

    private fun magnitude(equation: List<SnailNumber>): Int {
        val magnitude = equation.toMutableList()
        while (magnitude.size > 1) {
            for (i in 0 until magnitude.size - 1) {
                val number = magnitude[i]
                val nextNumber = magnitude[i + 1]
                if (number.depth == nextNumber.depth) {
                    val newNumber = SnailNumber(number.value * 3 + nextNumber.value * 2, number.depth - 1)
                    magnitude.removeAt(i + 1)
                    magnitude.removeAt(i)
                    magnitude.add(i, newNumber)
                    break
                }
            }
        }
        return magnitude.first().value
    }


    fun solvePart1(): Int {
        val result = equations.reduce { sn1, sn2 -> add(sn1, sn2) }
        return magnitude(result)
    }

    fun solvePart2(): Int {
        var maxMagnitude = 0
        for (i in equations.indices) {
            for (j in equations.indices) {
                if (i == j) continue
                maxMagnitude = maxMagnitude.coerceAtLeast(magnitude(add(equations[i], equations[j])))
                maxMagnitude = maxMagnitude.coerceAtLeast(magnitude(add(equations[j], equations[i])))
            }
        }
        return maxMagnitude
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day18_example.txt")
    val exampleSolutionPart1 = Day18(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day18(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day18.txt")
    val solutionPart1 = Day18(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day18(input).solvePart2()
    println(solutionPart2)
}