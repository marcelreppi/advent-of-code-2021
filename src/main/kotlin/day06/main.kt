package day06

import java.io.File

tailrec fun simulateDays(state: List<Int>, days: Int): List<Int> {
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
fun simulateByDays(initialState: List<Int>, days: Long): Long {
    var fishCountsPerDay = MutableList(9) { 0L }
    for (f in initialState) {
        fishCountsPerDay[f] += 1L
    }

    for (i in 1..days) {
        val nextFishCountsPerDay = MutableList(9) { 0L }
        for ((day, count) in fishCountsPerDay.withIndex()) {
            if (day == 0) {
                nextFishCountsPerDay[6] += count
                nextFishCountsPerDay[8] += count
            } else {
                nextFishCountsPerDay[day - 1] += count
            }
        }
        fishCountsPerDay = nextFishCountsPerDay
    }

    return fishCountsPerDay.sum()
}

fun main() {
    val input = File("./src/main/kotlin/day06/input.txt").readLines()

    val initialState = input.first().split(",").map { it.toInt() }
    val finalState = simulateDays(initialState, 80)
    val totalFishes = simulateByDays(initialState, 256)
    println(finalState.size)
    println(totalFishes)
}