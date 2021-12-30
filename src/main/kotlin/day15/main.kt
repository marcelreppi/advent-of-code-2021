package day15

import java.io.File
import java.util.*

data class Position(val x: Int, val y: Int, var value: Int)

class PathComparator {
    companion object : Comparator<List<Position>> {
        override fun compare(a: List<Position>, b: List<Position>): Int {
            val riskA = a.sumOf(Position::value)
            val riskB = b.sumOf(Position::value)
            return riskA - riskB
        }
    }
}

fun findLowestRiskDijkstra(grid: MutableList<List<Position>>): Int {
    fun getAdjacentPositions(position: Position): List<Position> {
        val (x, y) = position
        val adjacentCoordinates = listOf(
            Pair(x, y - 1),
            Pair(x + 1, y),
            Pair(x, y + 1),
            Pair(x - 1, y)
        )
        val adjacentPositions = mutableListOf<Position>()
        for ((ax, ay) in adjacentCoordinates) {
            if (ax >= 0 && ay >= 0 && ax <= grid.first().size - 1 && ay <= grid.size - 1) {
                adjacentPositions.add(grid[ay][ax])
            }
        }
        return adjacentPositions
    }

    val startingPosition = grid[0][0]
    val destinationPosition = grid[grid.size - 1][grid.first().size - 1]

    val startingPath = listOf(startingPosition)
    val pathsToCheck = PriorityQueue(PathComparator)
    pathsToCheck.add(startingPath)

    val visitedNodes = mutableSetOf<Position>()
    val minRiskPerPosition = mutableMapOf<Position, Int>()

    while (pathsToCheck.isNotEmpty()) {
        val path = pathsToCheck.remove()

        val currentPosition = path.last()
        val pathRisk = path.sumOf(Position::value)

        if (currentPosition == destinationPosition) {
            return pathRisk - startingPosition.value
        }

        if (visitedNodes.contains(currentPosition)) {
            continue
        }

        visitedNodes.add(currentPosition)

        val adjacentPositions = getAdjacentPositions(currentPosition)
        for (adjacentPosition in adjacentPositions) {
            val alreadyInPath = path.contains(adjacentPosition)
            val alreadyVisited = visitedNodes.contains(adjacentPosition)
            val nextRisk = pathRisk + adjacentPosition.value
            val notRiskier = nextRisk < (minRiskPerPosition[adjacentPosition] ?: Int.MAX_VALUE)
            if (!alreadyInPath && !alreadyVisited && notRiskier) {
                minRiskPerPosition[adjacentPosition] = nextRisk
                val nextPath = path.toMutableList()
                nextPath.add(adjacentPosition)
                pathsToCheck.add(nextPath)
            }
        }
    }

    return Int.MAX_VALUE
}

fun main() {
    val input = File("./src/main/kotlin/day15/input.txt").readLines()

    val grid = mutableListOf<List<Position>>()
    for ((y, line) in input.withIndex()) {
        val row = line.mapIndexed { x, c ->  Position(x, y, c.digitToInt()) }
        grid.add(row)
    }

    val lowestRiskSmall = findLowestRiskDijkstra(grid)
    println(lowestRiskSmall)

    val largeGrid = mutableListOf<List<Position>>()
    for (row in grid) {
        val newRow = MutableList(row.size * 5) { Position(-1, -1, -1) }
        for ((index, position) in row.withIndex()) {
            for (i in 0..4) {
                val newX = index + i * row.size
                var newValue = position.value + i
                if (newValue > 9) newValue %= 9
                newRow[newX] = Position(newX, position.y, newValue)
            }
        }
        largeGrid.add(newRow)
    }

    val newRows: MutableList<List<Position>> = mutableListOf()
    for (i in 1..4) {
        for (row in largeGrid.subList(0, grid.size)) {
            val newRow = row.map {
                val newY = it.y + i * grid.size
                var newValue = it.value + i
                if (newValue > 9) newValue %= 9
                Position(it.x, newY, newValue)
            }
            newRows.add(newRow)
        }
    }
    largeGrid.addAll(newRows)

    val lowestRiskLarge = findLowestRiskDijkstra(largeGrid)
    println(lowestRiskLarge)
}
