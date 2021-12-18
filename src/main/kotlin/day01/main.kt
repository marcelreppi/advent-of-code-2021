package day01

import java.io.File

fun countDepthIncreases(depths: List<Int>): Int {
    var depthIncreases = 0
    var previousDepth = depths.first()
    depths.subList(1, depths.size).forEach { depth ->
        if (depth > previousDepth) {
            depthIncreases++
        }
        previousDepth = depth
    }
    return depthIncreases
}

fun countDepthSumIncreases(depths: List<Int>): Int {
    var depthSumIncreases = 0
    var previousDepthSum = depths.subList(0, 3).sum()
    for (i in 1..depths.size - 3) {
        val currentDepthSum = depths.subList(i, i + 3).sum()
        if (currentDepthSum > previousDepthSum) {
            depthSumIncreases++
        }
        previousDepthSum = currentDepthSum
    }
    return depthSumIncreases
}


fun main(args: Array<String>) {
    val depths = File("./src/main/kotlin/day01/input.txt").readLines().map { it.toInt() }
    val depthIncreases = countDepthIncreases(depths)
    println(depthIncreases)
    val depthSumIncreases = countDepthSumIncreases(depths)
    println(depthSumIncreases)
}