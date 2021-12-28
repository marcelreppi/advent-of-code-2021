package day09

import java.io.File

data class Point(val x: Int, val y: Int, val height: Int)

fun getAdjacentFields(point: Point): List<Pair<Int, Int>> {
    val (x, y) = point
    return listOf(
        Pair(x, y - 1),
        Pair(x + 1, y),
        Pair(x, y + 1),
        Pair(x - 1, y)
    )
}

fun fieldOutOfBounds(heightMap: List<List<Point>>, x: Int, y: Int): Boolean {
    return x < 0 || x > heightMap.first().size - 1 || y < 0 || y > heightMap.size - 1
}

fun isLowPoint(heightMap: List<List<Point>>, point: Point): Boolean {
    val adjacentFields = getAdjacentFields(point)
    var lowCounter = 0
    for (field in adjacentFields) {
        val (fx, fy) = field

        if (fieldOutOfBounds(heightMap, fx, fy)) {
            lowCounter++
            continue
        }

        val adjacentPoint = heightMap[fy][fx]
        if (point.height < adjacentPoint.height) {
            lowCounter++
        }
    }

    return lowCounter == 4
}

fun findBasin(heightMap: List<List<Point>>, lowPoint: Point): List<Point> {
    val basin = mutableListOf(lowPoint)
    val fieldsToCheck = getAdjacentFields(lowPoint).toMutableList()

    while (fieldsToCheck.isNotEmpty()) {
        val fieldToCheck = fieldsToCheck.removeFirst()
        val (x, y) = fieldToCheck
        if (fieldOutOfBounds(heightMap, x, y)) continue

        val point = heightMap[y][x]
        if (basin.contains(point)) continue
        if (point.height == 9) continue

        basin.add(point)
        fieldsToCheck.addAll(getAdjacentFields(point))
    }

    return basin
}

fun main() {
    val input = File("./src/main/kotlin/day09/input.txt").readLines()

    val heightMap = mutableListOf<List<Point>>()

    for ((y, line) in input.withIndex()) {
        heightMap.add(line.toList().mapIndexed { x, height -> Point(x, y, height.digitToInt()) })
    }

    val lowPoints = mutableListOf<Point>()
    val basins = mutableListOf<List<Point>>()
    for (row in heightMap) {
        for (point in row) {
            if (isLowPoint(heightMap, point)) {
                lowPoints.add(point)
                val basin = findBasin(heightMap, point)
                basins.add(basin)
            }
        }
    }

    val riskLevels = lowPoints.map { it.height + 1 }
    println(riskLevels.sum())

    val threeLargestBasins = basins.map { it.size }.sortedDescending().subList(0, 3)
    println(threeLargestBasins.reduce { acc, size -> acc * size })
}
