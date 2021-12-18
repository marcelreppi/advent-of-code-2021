package day04

import java.io.File

data class Field(val value: Int, val x: Int, val y: Int, var marked: Boolean = false)
data class Board(val fields: MutableList<List<Field>> = mutableListOf(), var hasWon: Boolean = false)

fun markField(board: Board, value: Int): Field? {
    for (row in board.fields) {
        for (field in row) {
            if (!field.marked && field.value == value) {
                field.marked = true
                return field
            }
        }
    }
    return null
}

fun isWinningRowOrColumn(rowOrColumn: List<Field>): Boolean {
    return rowOrColumn.all { it.marked }
}

fun isWinningBoard(board: Board, rowIndex: Int, columnIndex: Int): Boolean {
    val isWinningRow = isWinningRowOrColumn(board.fields[rowIndex])
    val isWinningColumn = isWinningRowOrColumn(board.fields.map { row -> row[columnIndex] })
    return isWinningRow || isWinningColumn
}

fun calculateScore(board: Board, lastNumber: Int): Int {
    var sum = 0
    for (row in board.fields) {
        for (field in row) {
            if (!field.marked) sum += field.value
        }
    }
    return sum * lastNumber
}

fun main(args: Array<String>) {
    val input = File("./src/main/kotlin/day04/input.txt").readLines()

    val drawnNumbers = input.first().split(",").map { it.toInt() }
    println(drawnNumbers)

    val boards: MutableList<Board> = mutableListOf()

    var currentBoard: Board = Board()
    for (line in input.subList(2, input.size)) {
        if (line.isEmpty()) {
            boards.add(currentBoard)
            currentBoard = Board()
        } else {
            currentBoard.fields.add(line.split(" ")
                .filter { it.isNotEmpty() }
                .mapIndexed { index, n -> Field(n.toInt(), index, currentBoard.fields.size) })
        }
    }

    val winningBoards: MutableList<Pair<Board, Int>> = mutableListOf()
    for (number in drawnNumbers) {
        for (board in boards) {
            if (board.hasWon) continue

            val field = markField(board, number)
            if (field != null) {
                val isWinningBoard = isWinningBoard(board, field.y, field.x)
                if (isWinningBoard) {
                    board.hasWon = true
                    winningBoards.add(Pair(board, number))
                }
            }
        }
    }

    val firstWinningBoard = winningBoards.first().first
    val lastNumberOfFirstWinningBoard = winningBoards.first().second
    val firstWinnerScore = calculateScore(firstWinningBoard, lastNumberOfFirstWinningBoard)
    println(firstWinnerScore)

    val lastWinningBoard = winningBoards.last().first
    val lastNumberOfLastWinningBoard = winningBoards.last().second
    val lastWinnerScore = calculateScore(lastWinningBoard, lastNumberOfLastWinningBoard)
    println(lastWinnerScore)


}
