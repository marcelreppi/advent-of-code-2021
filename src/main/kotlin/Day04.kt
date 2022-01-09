private data class Field(val value: Int, val x: Int, val y: Int, var marked: Boolean = false)
private data class BingoBoard(val fields: List<List<Field>>, var hasWon: Boolean = false)

class Day04(input: List<String>) {
    private val drawnNumbers = input.first().split(",").map { it.toInt() }
    private val boards = parseInput(input)

    private fun parseInput(input: List<String>): Set<BingoBoard> {
        return input
            .asSequence()
            .drop(1)
            .filter { it.isNotEmpty() }
            .chunked(5)
            .map { parseBingoBoard(it) }
            .toSet()
    }

    private fun parseBingoBoard(input: List<String>): BingoBoard {
        return BingoBoard(
            input.mapIndexed { y, row ->
                row.split(" ").filter { it.isNotEmpty() }.mapIndexed { x, n ->
                    Field(n.toInt(), x, y)
                }
            }
        )
    }

    private fun markField(board: BingoBoard, value: Int): Field? {
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

    private fun isWinningRowOrColumn(rowOrColumn: List<Field>): Boolean {
        return rowOrColumn.all { it.marked }
    }

    private fun isWinningBoard(board: BingoBoard, rowIndex: Int, columnIndex: Int): Boolean {
        val isWinningRow = isWinningRowOrColumn(board.fields[rowIndex])
        val isWinningColumn = isWinningRowOrColumn(board.fields.map { row -> row[columnIndex] })
        return isWinningRow || isWinningColumn
    }

    private fun calculateScore(board: BingoBoard, lastNumber: Int): Int {
        var sum = 0
        for (row in board.fields) {
            for (field in row) {
                if (!field.marked) sum += field.value
            }
        }
        return sum * lastNumber
    }

    private fun simulateGame(): Set<Pair<BingoBoard, Int>> {
        val winningBoards: MutableList<Pair<BingoBoard, Int>> = mutableListOf()
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
        return winningBoards.toSet()
    }

    fun solvePart1(): Int {
        val winningBoards = simulateGame()
        val firstWinningBoard = winningBoards.first().first
        val lastNumberOfFirstWinningBoard = winningBoards.first().second
        val firstWinnerScore = calculateScore(firstWinningBoard, lastNumberOfFirstWinningBoard)
        return firstWinnerScore
    }

    fun solvePart2(): Int {
        val winningBoards = simulateGame()
        val lastWinningBoard = winningBoards.last().first
        val lastNumberOfLastWinningBoard = winningBoards.last().second
        val lastWinnerScore = calculateScore(lastWinningBoard, lastNumberOfLastWinningBoard)
        return lastWinnerScore
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day04_example.txt")
    val exampleSolutionPart1 = Day04(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day04(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day04.txt")
    val solutionPart1 = Day04(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day04(input).solvePart2()
    println(solutionPart2)
}