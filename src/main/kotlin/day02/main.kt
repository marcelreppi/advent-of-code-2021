package day02

import java.io.File

enum class Direction {
    UP,
    DOWN,
    FORWARD;
}

data class Instruction(val direction: Direction, val value: Int)
data class Position(val x: Int, val y: Int, val aim: Int)

fun parseInstruction(instruction: String): Instruction {
    val value = instruction.last().digitToInt()
    val direction = instruction.substring(0, instruction.length - 2)
    return Instruction(Direction.valueOf(direction.uppercase()), value)
}

tailrec fun trackPosition(currentPosition: Position, instructions: List<Instruction>, trackAim: Boolean): Position {
    if (instructions.isEmpty()) return currentPosition

    val instruction = instructions.first()

    val nextPosition: Position
    if (trackAim) {
        val nextAim = when (instruction.direction) {
            Direction.UP -> currentPosition.aim - instruction.value
            Direction.DOWN -> currentPosition.aim + instruction.value
            else -> currentPosition.aim
        }
        nextPosition = when (instruction.direction) {
            Direction.FORWARD -> Position(
                currentPosition.x + instruction.value,
                currentPosition.y + (nextAim * instruction.value),
                nextAim
            )
            else -> Position(currentPosition.x, currentPosition.y, nextAim)
        }
    } else {
        nextPosition = when (instruction.direction) {
            Direction.UP -> Position(currentPosition.x, currentPosition.y - instruction.value, 0)
            Direction.DOWN -> Position(currentPosition.x, currentPosition.y + instruction.value, 0)
            Direction.FORWARD -> Position(currentPosition.x + instruction.value, currentPosition.y, 0)
        }
    }

    return trackPosition(nextPosition, instructions.subList(1, instructions.size), trackAim)
}

fun main(args: Array<String>) {
    val instructions = File("./src/main/kotlin/day02/input.txt").readLines().map { parseInstruction(it) }
    val startingPosition = Position(0, 0, 0)
    val endPosition = trackPosition(startingPosition, instructions, false)
    println(endPosition.x * endPosition.y)
    val endPositionWithAim = trackPosition(startingPosition, instructions, true)
    println(endPositionWithAim.x * endPositionWithAim.y)
}