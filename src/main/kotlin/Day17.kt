import kotlin.math.floor
import kotlin.math.sqrt

class Day17(input: String) {
    private data class TargetArea(val x: IntRange, val y: IntRange)

    private val targetArea = parseInput(input)

    private fun parseInput(input: String): TargetArea {
        val (xString, yString) = input.substringAfter("target area: ").split(", ")
        val (xMinTarget, xMaxTarget) = xString.substring(2).split("..").map { it.toInt() }
        val (yMinTarget, yMaxTarget) = yString.substring(2).split("..").map { it.toInt() }
        return TargetArea(
            xMinTarget..xMaxTarget,
            yMinTarget..yMaxTarget
        )
    }

    fun solvePart1(): Int {
        // Since the highest point will be reached using a launching vector such that
        // we reach y = 0 at n-1 and y = ymin at n, with ymin being the deepest row
        // of the target area (the 3rd number in the input) and n being the step n°,
        // then the highest point is the sum from 1 to (-ymin - 1), or in other term:

        val yMinTarget = targetArea.y.first
        val highestPoint = (-yMinTarget * (-yMinTarget - 1)) / 2
        return highestPoint
    }

    // Brute-force within search bounds
    fun solvePart2(): Int {
        val xMin = floor(-(1 / 2) + sqrt(((1 / 4) + (targetArea.x.first.toDouble() * 2)))).toInt()
        val xMax = targetArea.x.last
        val yMin = targetArea.y.first
        val yMax = -targetArea.y.first

        val velocities = mutableListOf<Pair<Int, Int>>()
        for (vx0 in xMin..xMax) {
            for (vy0 in yMin..yMax) {
                var x = 0
                var y = 0
                var vx = vx0
                var vy = vy0

                while (x <= targetArea.x.last && y >= targetArea.y.first) {
                    if (x >= targetArea.x.first && y <= targetArea.y.last) {
                        velocities.add(Pair(vx0, vy0))
                        break
                    }

                    x += vx
                    y += vy

                    if (vx > 0) vx--
                    vy--
                }
            }
        }
        return velocities.size
    }
}

fun main() {
    val exampleInput = Resources.resourceAsText("day17_example.txt")
    val exampleSolutionPart1 = Day17(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day17(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsText("day17.txt")
    val solutionPart1 = Day17(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day17(input).solvePart2()
    println(solutionPart2)
}