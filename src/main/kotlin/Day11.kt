typealias OctopusCave = Map<Point2D, Int>

class Day11(input: List<String>) {
    private val startingCave: OctopusCave = input.flatMapIndexed { y, row ->
        row.mapIndexed { x, energy -> Point2D(x, y) to energy.digitToInt() }
    }.toMap()

    private fun OctopusCave.steps(): Sequence<Int> = sequence {
        val cave = this@steps.toMutableMap()

        while (true) {
            cave.forEach { (point, energy) -> cave[point] = energy + 1 }
            do {
                val flashersThisRound = cave.filterValues { it > 9 }.keys
                flashersThisRound.forEach { cave[it] = 0 }

                flashersThisRound
                    .flatMap { it.getAdjacentPoints(true) }
                    .filter { it in cave && cave[it] != 0 }
                    .forEach { cave[it] = cave.getValue(it) + 1 }
            } while (flashersThisRound.isNotEmpty())

            yield(cave.count { it.value == 0 })
        }
    }

    fun solvePart1(): Int {
        return startingCave.steps().take(100).sum()
    }

    fun solvePart2(): Int {
        return startingCave.steps().indexOfFirst { it == startingCave.size } + 1
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day11_example.txt")
    val exampleSolutionPart1 = Day11(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day11(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day11.txt")
    val solutionPart1 = Day11(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day11(input).solvePart2()
    println(solutionPart2)
}