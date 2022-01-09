class Day02(private val input: List<String>) {
    private enum class Direction {
        UP,
        DOWN,
        FORWARD;
    }

    private data class Instruction(val direction: Direction, val value: Int)
    private data class Position(val x: Int, val y: Int, val aim: Int)

    private fun parseInstruction(instruction: String): Instruction {
        val value = instruction.last().digitToInt()
        val direction = instruction.substring(0, instruction.length - 2)
        return Instruction(Direction.valueOf(direction.uppercase()), value)
    }

    private tailrec fun trackPosition(currentPosition: Position, instructions: List<Instruction>, trackAim: Boolean): Position {
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

    fun solvePart1(): Int {
        val instructions = input.map { parseInstruction(it) }
        val startingPosition = Position(0, 0, 0)
        val endPosition = trackPosition(startingPosition, instructions, false)
        return endPosition.x * endPosition.y
    }

    fun solvePart2(): Int {
        val instructions = input.map { parseInstruction(it) }
        val startingPosition = Position(0, 0, 0)
        val endPositionWithAim = trackPosition(startingPosition, instructions, true)
        return endPositionWithAim.x * endPositionWithAim.y
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day02_example.txt")
    val exampleSolutionPart1 = Day02(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day02(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day02.txt")
    val solutionPart1 = Day02(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day02(input).solvePart2()
    println(solutionPart2)
}