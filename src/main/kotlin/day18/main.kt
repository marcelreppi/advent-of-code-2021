package day18

import java.io.File
import kotlin.math.ceil
import kotlin.math.floor

data class SnailNumber(var value: Int, var depth: Int)
typealias Equation = MutableList<SnailNumber>

fun parseLine(line: String): Equation {
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

fun add(sn1: Equation, sn2: Equation): Equation {
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

fun magnitude(equation: Equation): Int {
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

fun main() {
    val input = File("./src/main/kotlin/day18/input.txt").readLines().map { parseLine(it) }

    val initialEquation = input.first()
    val result = input.subList(1, input.size).fold(initialEquation) { sn1, sn2 -> add(sn1, sn2) }
    val magnitude = magnitude(result)
    println(magnitude)

    var maxMagnitude = 0
    for (i in input.indices) {
        for (j in input.indices) {
            if (i == j) continue
            maxMagnitude = maxMagnitude.coerceAtLeast(magnitude(add(input[i], input[j])))
            maxMagnitude = maxMagnitude.coerceAtLeast(magnitude(add(input[j], input[i])))
        }
    }
    println(maxMagnitude)
}