class Day13(input: List<String>) {
    private val dots: Set<Point2D> = parseDots(input)
    private val instructions: List<Point2D> = parseInstructions(input)

    private fun parseDots(input: List<String>): Set<Point2D> {
        return input.takeWhile { it.isNotEmpty() }
            .map { it.split(",") }
            .map { Point2D(it.first().toInt(), it.last().toInt()) }
            .toSet()
    }

    private fun parseInstructions(input: List<String>): List<Point2D> {
        return input.takeLastWhile { it.isNotEmpty() }
            .map { it.split("=") }
            .map {
                if (it.first().endsWith("y")) {
                    Point2D(0, it.last().toInt())
                } else {
                    Point2D(it.last().toInt(), 0)
                }
            }
    }

    private fun Set<Point2D>.print() {
        for (y in 0..this.maxOf { it.y }) {
            for (x in 0..this.maxOf { it.x }) {
                val symbol = if (this.contains(Point2D(x, y))) "#" else " "
                print(symbol)
            }
            println()
        }
    }

    private fun getPositionAfterFold(position: Int, fold: Int): Int {
        return if (position < fold) position else (fold * 2) - position
    }


    private fun Set<Point2D>.foldAt(instruction: Point2D): Set<Point2D> {
        return if (instruction.x != 0) {
            this.map { it.copy(x = getPositionAfterFold(it.x, instruction.x)) }.toSet()
        } else {
            this.map { it.copy(y = getPositionAfterFold(it.y, instruction.y)) }.toSet()
        }
    }


    fun solvePart1(): Int {
        return dots.foldAt(instructions.first()).size
    }

    fun solvePart2() {
        val finalDots = instructions.fold(dots) { dots, instruction -> dots.foldAt(instruction) }
        finalDots.print()
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day13_example.txt")
    val exampleSolutionPart1 = Day13(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day13(exampleInput).solvePart2()
//    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day13.txt")
    val solutionPart1 = Day13(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day13(input).solvePart2()
//    println(solutionPart2)
}