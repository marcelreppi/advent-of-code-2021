class Day05(input: List<String>) {
    private val lineCoordinates = input.map { parseLine(it) }

    private fun parseLine(line: String): Pair<Point2D, Point2D> {
        return line.split(" -> ").let {
            val startCoordinate = Point2D.from(it.first())
            val endCoordinate = Point2D.from(it.last())
            Pair(startCoordinate, endCoordinate)
        }
    }

    private fun Pair<Point2D, Point2D>.isHorizontal(): Boolean = this.first.x == this.second.x
    private fun Pair<Point2D, Point2D>.isVertical(): Boolean = this.first.y == this.second.y

    fun solvePart1(): Int {
        return lineCoordinates
            .filter { it.isHorizontal() || it.isVertical() }
            .flatMap { it.first lineTo it.second }
            .groupingBy { it }
            .eachCount()
            .count { it.value > 1 }
    }

    fun solvePart2(): Int {
        return lineCoordinates
            .flatMap { it.first lineTo it.second }
            .groupingBy { it }
            .eachCount()
            .count { it.value > 1 }
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day05_example.txt")
    val exampleSolutionPart1 = Day05(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day05(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day05.txt")
    val solutionPart1 = Day05(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day05(input).solvePart2()
    println(solutionPart2)
}