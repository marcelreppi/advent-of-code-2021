class Day25(input: List<String>) {
    private val initialGrid = input.map { it.toCharArray().toList() }

    private data class Move(val char: Char, val fromX: Int, val fromY: Int, val toX: Int, val toY: Int)

    private fun getNextEastMove(grid: List<List<Char>>, x: Int, y: Int): Move? {
        val rightX = (x + 1) % grid[y].size
        return if (grid[y][rightX] == '.') {
            Move(grid[y][x], x, y, rightX, y)
        } else null
    }

    private fun getNextSouthMove(grid: List<List<Char>>, x: Int, y: Int): Move? {
        val leftX = if ((x - 1) < 0) grid[y].size - 1 else (x - 1) % grid[y].size
        val rightX = (x + 1) % grid[y].size
        val bottomY = (y + 1) % grid.size

        val validSituations = listOf("...", "v..", "..v", "v.v", ".>.", "v>.", ">>.", "..>", "v.>")
        val currentSituation = "${grid[bottomY][leftX]}${grid[bottomY][x]}${grid[bottomY][rightX]}"

        return if (currentSituation in validSituations) {
            Move(grid[y][x], x, y, x, bottomY)
        } else null
    }

    fun solvePart1(): Int {
        var grid = initialGrid.toMutableList().map { it.toMutableList() }
        var steps = 0
        while (true) {
            steps++
            val moves = mutableListOf<Move>()
            for (y in grid.indices) {
                for (x in grid[y].indices) {
                    val move = when (grid[y][x]) {
                        '>' -> getNextEastMove(grid, x, y) ?: continue
                        'v' -> getNextSouthMove(grid, x, y) ?: continue
                        else -> continue
                    }
                    moves.add(move)
                }
            }

            if (moves.size == 0) break

            val nextGrid = grid.toMutableList().map { it.toMutableList() }
            // Sort the moves to ensure moves from > happen before v
            for (move in moves.sortedBy { it.char }) {
                nextGrid[move.fromY][move.fromX] = '.'
                nextGrid[move.toY][move.toX] = move.char
            }
            grid = nextGrid
        }
        return steps
    }

//    fun solvePart2(): Int {
//        return 0
//    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day25_example.txt")
    val exampleSolutionPart1 = Day25(exampleInput).solvePart1()
    println(exampleSolutionPart1)
//    val exampleSolutionPart2 = Day25(exampleInput).solvePart2()
//    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day25.txt")
    val solutionPart1 = Day25(input).solvePart1()
    println(solutionPart1)
//    val solutionPart2 = Day25(input).solvePart2()
//    println(solutionPart2)
}