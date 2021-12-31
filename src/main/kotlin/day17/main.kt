package day17

import java.io.File
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {
    val input = File("./src/main/kotlin/day17/input.txt").readLines()

    val prefix = "target area: "
    val (xString, yString) = input.first().substring(prefix.length).split(", ")
    val (xMinTarget, xMaxTarget) = xString.substring(2).split("..").map { it.toInt() }
    val (yMinTarget, yMaxTarget) = yString.substring(2).split("..").map { it.toInt() }

    // Part 1
    // Since the highest point will be reached using a launching vector such that
    // we reach y = 0 at n-1 and y = ymin at n, with ymin being the deepest row
    // of the target area (the 3rd number in the input) and n being the step n°,
    // then the highest point is the sum from 1 to (-ymin - 1), or in other term:

    val highestPoint = (-yMinTarget * (-yMinTarget - 1)) / 2
    println(highestPoint)

    // Part 2
    // Brute-force within search bounds
    val xMin = floor(-(1/2) + sqrt(((1 / 4) + (xMinTarget.toDouble() * 2)))).toInt()
    val xMax = xMaxTarget
    val yMin = yMinTarget
    val yMax = -yMinTarget

    val velocities = mutableListOf<Pair<Int, Int>>()
    for (vx0 in xMin..xMax) {
        for (vy0 in yMin..yMax) {
            var x = 0
            var y = 0
            var vx = vx0
            var vy = vy0

            while (x <= xMaxTarget && y >= yMinTarget) {
                if (x >= xMinTarget && y <= yMaxTarget) {
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
    println(velocities.size)

}
