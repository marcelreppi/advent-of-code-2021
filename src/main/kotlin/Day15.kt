import java.util.*

class Day15(input: List<String>) {
    private val grid: List<List<Position>> = parseInput(input)

    private data class Position(val location: Point2D, val risk: Int)

    private fun parseInput(input: List<String>): List<List<Position>> {
        return input.mapIndexed { y, row ->
            row.mapIndexed { x, risk ->
                Position(Point2D(x, y), risk.digitToInt())
            }
        }
    }

    private class PathComparator {
        companion object : Comparator<List<Position>> {
            override fun compare(a: List<Position>, b: List<Position>): Int {
                val riskA = a.sumOf(Position::risk)
                val riskB = b.sumOf(Position::risk)
                return riskA - riskB
            }
        }
    }

    private fun findLowestRiskDijkstra(grid: List<List<Position>>): Int {
        val startingPosition = grid[0][0]
        val destinationPosition = grid[grid.indices.last][grid.first().indices.last]

        val startingPath = listOf(startingPosition)
        val pathsToCheck = PriorityQueue(PathComparator)
        pathsToCheck.add(startingPath)

        val visitedLocations = mutableSetOf<Point2D>()
        val minRiskPerLocation = mutableMapOf<Point2D, Int>()

        while (pathsToCheck.isNotEmpty()) {
            val path = pathsToCheck.remove()

            val currentPosition = path.last()
            val pathRisk = path.sumOf(Position::risk)

            if (currentPosition == destinationPosition) {
                return pathRisk - startingPosition.risk
            }

            if (visitedLocations.contains(currentPosition.location)) {
                continue
            }

            visitedLocations.add(currentPosition.location)

            val adjacentLocations = currentPosition.location.getAdjacentPoints()
            for (adjacentLocation in adjacentLocations.filter { it isWithin grid }) {
                val nextPosition = grid[adjacentLocation.y][adjacentLocation.x]
                val alreadyInPath = path.contains(nextPosition)
                val alreadyVisited = visitedLocations.contains(adjacentLocation)
                val nextRisk = pathRisk + nextPosition.risk
                val notRiskier = nextRisk < (minRiskPerLocation[adjacentLocation] ?: Int.MAX_VALUE)
                if (!alreadyInPath && !alreadyVisited && notRiskier) {
                    minRiskPerLocation[adjacentLocation] = nextRisk
                    val nextPath = path.toMutableList()
                    nextPath.add(nextPosition)
                    pathsToCheck.add(nextPath)
                }
            }
        }

        return Int.MAX_VALUE
    }

    private fun extendGrid(): List<List<Position>> {
        val largeGrid = mutableListOf<List<Position>>()
        for (row in grid) {
            val newRow = MutableList(row.size * 5) { Position(Point2D(-1, -1), -1) }
            for ((index, position) in row.withIndex()) {
                for (i in 0..4) {
                    val newX = index + i * row.size
                    var newValue = position.risk + i
                    if (newValue > 9) newValue %= 9
                    newRow[newX] = Position(Point2D(newX, position.location.y), newValue)
                }
            }
            largeGrid.add(newRow)
        }

        val newRows: MutableList<List<Position>> = mutableListOf()
        for (i in 1..4) {
            for (row in largeGrid.subList(0, grid.size)) {
                val newRow = row.map {
                    val newY = it.location.y + i * grid.size
                    var newValue = it.risk + i
                    if (newValue > 9) newValue %= 9
                    Position(Point2D(it.location.x, newY), newValue)
                }
                newRows.add(newRow)
            }
        }
        largeGrid.addAll(newRows)
        return largeGrid
    }

    fun solvePart1(): Int {
        val lowestRiskSmall = findLowestRiskDijkstra(grid)
        return lowestRiskSmall
    }

    fun solvePart2(): Int {
        val largeGrid = extendGrid()
        val lowestRiskLarge = findLowestRiskDijkstra(largeGrid)
        return lowestRiskLarge
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day15_example.txt")
    val exampleSolutionPart1 = Day15(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day15(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day15.txt")
    val solutionPart1 = Day15(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day15(input).solvePart2()
    println(solutionPart2)
}