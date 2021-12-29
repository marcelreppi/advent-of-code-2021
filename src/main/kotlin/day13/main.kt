package day13

import java.io.File

data class Dot(var x: Int, var y: Int) {
    operator fun get(axis: String): Int {
        return if (axis == "x") x else y
    }

    operator fun set(axis: String, value: Int) {
        if (axis == "x") {
            x = value
        } else {
            y = value
        }
    }
}

data class Instruction(val axis: String, val position: Int)

fun printDots(dots: MutableMap<String, Dot>, width: Int, height: Int) {
    for (y in 0..height) {
        for (x in 0..width) {
            val symbol = if (dots.containsKey("${x},${y}")) '#' else ' '
            print(symbol)
        }
        println()
    }
}

fun main() {
    val input = File("./src/main/kotlin/day13/input.txt").readLines()

    var width = 0
    var height = 0

    // { "x,y": Dot }
    var dots = mutableMapOf<String, Dot>()
    val instructions = mutableListOf<Instruction>()

    for (line in input) {
        if (line.isEmpty()) continue

        if (line.startsWith("fold")) {
            val instructionString = line.split(" ").last()
            val (axis, position) = instructionString.split("=")
            instructions.add(Instruction(axis, position.toInt()))
        } else {
            val (x, y) = line.split(",")
            val newDot = Dot(x.toInt(), y.toInt())
            dots[line] = newDot
            width = width.coerceAtLeast(newDot.x)
            height = height.coerceAtLeast(newDot.y)
        }
    }

    for (instruction in instructions) {
        val newDots = mutableMapOf<String, Dot>()
        for (dot in dots.values) {
            val newDot = dot.copy()

            val axisSize = if (instruction.axis == "x") width else height
            val (axis) = instruction
            if (dot[axis] > instruction.position) {
                val distanceToFoldingLine = dot[axis] - instruction.position
                newDot[axis] = dot[axis] - (2 * distanceToFoldingLine)
            }

            // Normalize
            if (instruction.position < axisSize / 2) {
                newDot[axis] += (axisSize - (2 * instruction.position))
            }

            newDots["${newDot.x},${newDot.y}"] = newDot
        }

        dots = newDots
        if (instruction.axis == "x") {
            width = if (instruction.position >= width / 2) {
                instruction.position
            } else {
                width - instruction.position
            }
        }
        if (instruction.axis == "y") {
            height = if (instruction.position >= height / 2) {
                instruction.position
            } else {
                height - instruction.position
            }
        }

    }

    println(dots.size)
    printDots(dots, width, height)
}
