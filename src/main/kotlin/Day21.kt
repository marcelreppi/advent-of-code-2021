class Day21(input: List<String>) {
    private val p1 = PlayerState(input.first().substringAfterLast(" ").toInt())
    private val p2 = PlayerState(input.last().substringAfterLast(" ").toInt())

    private data class PlayerState(val position: Int, val score: Int = 0) {
        fun advance(steps: Int): PlayerState {
            val nextPosition = ((position + steps - 1) % 10) + 1
            return PlayerState(nextPosition, score + nextPosition)
        }
    }

    private data class GameState(val p1: PlayerState, val p2: PlayerState, val p1Turn: Boolean = true) {
        fun next(steps: Int): GameState {
            val nextP1 = if (p1Turn) p1.advance(steps) else p1
            val nextP2 = if (!p1Turn) p2.advance(steps) else p2
            return GameState(nextP1, nextP2, !p1Turn)
        }

        fun hasWinner(winningScore: Int): Boolean {
            return p1.score >= winningScore || p2.score >= winningScore
        }

        fun minScore(): Int {
            return minOf(p1.score, p2.score)
        }
    }

    private data class WinCounts(val p1Wins: Long = 0, val p2Wins: Long = 0) {
        operator fun plus(other: WinCounts): WinCounts {
            return WinCounts(p1Wins + other.p1Wins, p2Wins + other.p2Wins)
        }

        operator fun times(other: Int): WinCounts {
            return WinCounts(p1Wins * other, p2Wins * other)
        }

        fun max(): Long = maxOf(p1Wins, p2Wins)
    }

    fun solvePart1(): Int {
        var game = GameState(p1, p2)
        val diceThrows = ((1..100) + (1..100) + (1..100)).chunked(3)
        var rounds = 0
        while (!game.hasWinner(1000)) {
            rounds++
            val diceThrow = diceThrows[(rounds - 1) % diceThrows.size]
            game = game.next(diceThrow.sum())
        }

        return game.minScore() * rounds * 3
    }

    fun solvePart2(): Long {
        val diceThrowsToFrequency = mapOf(
            3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1
        )

        val initialGameState = GameState(p1, p2)

        val gameStateMemoization: MutableMap<GameState, WinCounts> = mutableMapOf()

        fun simulateDiceThrow(gameState: GameState): WinCounts {
            if (gameState.hasWinner(21)) {
                return if (gameState.p1.score > gameState.p2.score) {
                    WinCounts(1, 0)
                } else {
                    WinCounts(0, 1)
                }
            }

            if (gameState in gameStateMemoization) {
                return gameStateMemoization.getValue(gameState)
            }

            return diceThrowsToFrequency.map { (diceThrow, frequency) ->
                simulateDiceThrow(gameState.next(diceThrow)) * frequency
            }.reduce { a, b -> a + b }.also { gameStateMemoization[gameState] = it }
        }

        val winCounts = simulateDiceThrow(initialGameState)
        return winCounts.max()
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day21_example.txt")
    val exampleSolutionPart1 = Day21(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day21(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day21.txt")
    val solutionPart1 = Day21(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day21(input).solvePart2()
    println(solutionPart2)
}