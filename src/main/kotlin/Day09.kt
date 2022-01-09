class Day09(input: List<String>) {
    private val heightMap = input.map { row -> row.toCharArray().map { it.digitToInt() } }

    private fun isLowPoint(x: Int, y: Int): Boolean {
        val height = heightMap[y][x]
        val adjacentFields = getAdjacentPoints(x, y)
        return adjacentFields.filter { it isWithin heightMap }.all { field ->
            val adjacentHeight = heightMap[field.y][field.x]
            height < adjacentHeight
        }
    }

    private fun findBasin(x: Int, y: Int): List<Point2D> {
        val basin: MutableList<Point2D> = mutableListOf()
        val pointsToCheck = mutableListOf(Point2D(x, y))
        while (pointsToCheck.isNotEmpty()) {
            val point = pointsToCheck.removeFirst()

            if (
                !(point isWithin heightMap) ||
                heightMap[point.y][point.x] >= 9 ||
                basin.contains(point)
            ) continue

            basin.add(point)

            pointsToCheck.addAll(point.getAdjacentPoints())
        }

        return basin
    }

    fun solvePart1(): Int {
        return heightMap.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, height ->
                if (isLowPoint(x, y)) height + 1 else null
            }
        }.sum()
    }

    fun solvePart2(): Int {
        val lowPoints = heightMap.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, _ ->
                if (isLowPoint(x, y)) Point2D(x, y) else null
            }
        }
        val basins = lowPoints.map { findBasin(it.x, it.y) }
        return basins
            .sortedBy { it.size }
            .takeLast(3)
            .fold(1) { acc, list -> acc * list.size }
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day09_example.txt")
    val exampleSolutionPart1 = Day09(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day09(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day09.txt")
    val solutionPart1 = Day09(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day09(input).solvePart2()
    println(solutionPart2)
}