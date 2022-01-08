package day25

import java.io.File

data class Move(val char: Char, val fromX: Int, val fromY: Int, val toX: Int, val toY: Int)

fun getNextEastMove(grid: List<List<Char>>, x: Int, y: Int): Move? {
    val rightX = (x + 1) % grid[y].size
    return if (grid[y][rightX] == '.') {
        Move(grid[y][x], x, y, rightX, y)
    } else null
}

fun getNextSouthMove(grid: List<List<Char>>, x: Int, y: Int): Move? {
    val leftX = if ((x - 1) < 0) grid[y].size - 1 else (x - 1) % grid[y].size
    val rightX = (x + 1) % grid[y].size
    val bottomY = (y + 1) % grid.size

    val validSituations = listOf("...", "v..", "..v", "v.v", ".>.", "v>.", ">>.", "..>", "v.>")
    val currentSituation = "${grid[bottomY][leftX]}${grid[bottomY][x]}${grid[bottomY][rightX]}"

    return if (currentSituation in validSituations) {
        Move(grid[y][x], x, y, x, bottomY)
    } else null
}

fun main() {
    val input = File("./src/main/kotlin/day25/input.txt").readLines()

    var grid = input.map { it.toCharArray().toList() }

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

    println(steps)
}
