import kotlin.math.abs

class Day07(input: List<String>) {
    private val positions = input.first().split(",").map { it.toInt() }

    fun solvePart1(): Int {
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

    fun solvePart2(): Int {
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
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day07_example.txt")
    val exampleSolutionPart1 = Day07(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day07(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day07.txt")
    val solutionPart1 = Day07(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day07(input).solvePart2()
    println(solutionPart2)
}