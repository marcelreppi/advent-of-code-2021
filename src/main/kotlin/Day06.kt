class Day06(input: String) {
    private val initialState = input.split(",").map { it.toInt() }

    private tailrec fun simulateDays(state: List<Int>, days: Int): List<Int> {
        if (days == 0) return state

        val newState = state.toMutableList()
        state.forEachIndexed { index, x ->
            if (x == 0) {
                newState.add(8)
                newState[index] = 6
            } else {
                newState[index] -= 1
            }
        }

        return simulateDays(newState, days - 1)
    }

    // All fish that have the same day can be simulated at once
    // Therefore we store counts of fish for each day and advance them all at once
    // All fish of day 0 are reset to day 6 and also the same amount of fish are added to day 8
    private fun simulateByDays(initialState: List<Int>, days: Int): Long {
        var fishCountPerDay = LongArray(9).apply {
            initialState.forEach { this[it] += 1L }
        }
        repeat(days) {
            fishCountPerDay = LongArray(9).apply {
                (0..8).forEach { day ->
                    val count = fishCountPerDay[day]
                    if (day == 0) {
                        this[6] += count
                        this[8] += count
                    } else {
                        this[day - 1] += count
                    }
                }
            }
        }
        return fishCountPerDay.sum()
    }

    fun solvePart1(): Int {
        val finalState = simulateDays(initialState, 80)
        return finalState.size
    }

    fun solvePart2(): Long {
        return simulateByDays(initialState, 256)
    }
}

fun main() {
    val exampleInput = Resources.resourceAsText("day06_example.txt")
    val exampleSolutionPart1 = Day06(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day06(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsText("day06.txt")
    val solutionPart1 = Day06(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day06(input).solvePart2()
    println(solutionPart2)
}