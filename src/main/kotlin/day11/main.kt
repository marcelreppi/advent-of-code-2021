package day11

import java.io.File

data class Octopus(val x: Int, val y: Int, var energy: Int, var hasFlashed: Boolean = false)
data class Field(val x: Int, val y: Int)

fun getAdjacentFields(x: Int, y: Int): List<Field> {
    return listOf(
        Field(x, y - 1),
        Field(x + 1, y - 1),
        Field(x + 1, y),
        Field(x + 1, y + 1),
        Field(x, y + 1),
        Field(x - 1, y + 1),
        Field(x - 1, y),
        Field(x - 1, y - 1),
    )
}

fun fieldOutOfBounds(grid: List<List<Octopus>>, x: Int, y: Int): Boolean {
    return x < 0 || x > grid.first().size - 1 || y < 0 || y > grid.size - 1
}

fun main() {
    val input = File("./src/main/kotlin/day11/input.txt").readLines()

    val grid = mutableListOf<List<Octopus>>()
    for ((y, line) in input.withIndex()) {
        grid.add(line.toList().mapIndexed { x, energy -> Octopus(x, y, energy.digitToInt()) })
    }

    var totalFlashes = 0
    var step = 0
    while (true) {
        step++
        val flashingOctopuses = mutableSetOf<Octopus>()
        val fieldsToCheck = mutableListOf<Field>()

        for (row in grid) {
            for (octopus in row) {
                octopus.energy += 1
                if (octopus.energy > 9) {
                    flashingOctopuses.add(octopus)
                    fieldsToCheck.addAll(getAdjacentFields(octopus.x, octopus.y))
                }
            }
        }

        while (fieldsToCheck.isNotEmpty()) {
            val field = fieldsToCheck.removeFirst()

            if (fieldOutOfBounds(grid, field.x, field.y)) continue

            val octopus = grid[field.y][field.x]

            if (octopus.energy <= 9) {
                octopus.energy += 1
            } else {
                continue
            }

            if (octopus.energy > 9) {
                flashingOctopuses.add(octopus)
                fieldsToCheck.addAll(getAdjacentFields(octopus.x, octopus.y))
            }
        }

        for (octopus in flashingOctopuses) {
            octopus.energy = 0
        }

        totalFlashes += flashingOctopuses.size

        if (flashingOctopuses.size == 100) {
            break
        }
    }

    println(totalFlashes)
    println(step)

}
