package day07

import java.io.File
import kotlin.math.abs

fun part1(positions: List<Int>): Int {
    // Could have just been the distance to the median due to optimality property
    // https://en.wikipedia.org/wiki/Median#Optimality_property
//    val sortedPositions = positions.sorted()
//    val median = if (sortedPositions.size % 2 == 0) {
//        ((sortedPositions[sortedPositions.size / 2] + sortedPositions[sortedPositions.size / 2 - 1]) / 2)
//    } else {
//        (sortedPositions[sortedPositions.size / 2])
//    }
//    return positions.sumOf { abs(it - median) }

    val totalCrabs = positions.size
    val crabsPerPosition = positions.groupingBy { it }.eachCount()
    val rightBoundary = crabsPerPosition.keys.maxOrNull() ?: 0

    var crabsLeft = crabsPerPosition[0] ?: 0
    var crabsRight = totalCrabs - crabsLeft - (crabsPerPosition[1] ?: 0)
    var leastFuel = Float.POSITIVE_INFINITY.toInt()
    var lastFuel = positions.sum()

    for (position in 1..rightBoundary) {
        val crabsAtCurrentPosition = crabsPerPosition[position] ?: 0
        val currentFuel = lastFuel - crabsAtCurrentPosition + crabsLeft - crabsRight
        if (currentFuel < leastFuel) {
            leastFuel = currentFuel
        }

        crabsLeft += crabsAtCurrentPosition

        val crabsAtNextPosition = crabsPerPosition[position + 1] ?: 0
        crabsRight -= crabsAtNextPosition

        lastFuel = currentFuel
    }

    return leastFuel
}

fun part2(positions: List<Int>): Int {
    // Could have just been the cost function for each distance to the mean
//    val mean = positions.sum() / positions.size
//    fun cost(distance: Int) = (distance * (distance + 1)) / 2
//    return positions.sumOf { cost(abs(it - mean)) }

    val crabsPerPosition = positions.groupingBy { it }.eachCount()
    val leftBoundary = crabsPerPosition.keys.minOrNull() ?: 0
    val rightBoundary = crabsPerPosition.keys.maxOrNull() ?: 0
    var leastFuel = Float.POSITIVE_INFINITY.toInt()

    for (position in leftBoundary..rightBoundary) {
        var leftFuel = 0
        var rightFuel = 0

        crabsPerPosition.forEach { (p, count) ->
            val distance = abs(p - position)
            // (1..distance).sum() = (distance * (distance + 1) / 2)
            val fuel = (distance * (distance + 1) / 2) * count
            if (p < position) {
                leftFuel += fuel
            }
            if (p > position) {
                rightFuel += fuel
            }
        }

        val currentFuel = leftFuel + rightFuel
        if (currentFuel < leastFuel) {
            leastFuel = currentFuel
        }
    }

    return leastFuel
}

fun main() {
    val input = File("./src/main/kotlin/day07/input.txt").readLines()

    val positions = input.first().split(",").map { it.toInt() }

    val fuel1 = part1(positions)
    println(fuel1)

    val fuel2 = part2(positions)
    println(fuel2)
}