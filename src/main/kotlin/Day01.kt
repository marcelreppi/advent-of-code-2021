package day01

import Resources

class Day01(private val input: List<Int>) {
    private fun countDepthIncreases(): Int {
//        var depthIncreases = 0
//        var previousDepth = depths.first()
//        depths.subList(1, depths.size).forEach { depth ->
//            if (depth > previousDepth) {
//                depthIncreases++
//            }
//            previousDepth = depth
//        }
//        return depthIncreases

        return input.zipWithNext().count { (d1, d2) -> d2 > d1 }
    }

    private fun countDepthSumIncreases(): Int {
//        var depthSumIncreases = 0
//        var previousDepthSum = input.subList(0, 3).sum()
//        for (i in 1..input.size - 3) {
//            val currentDepthSum = input.subList(i, i + 3).sum()
//            if (currentDepthSum > previousDepthSum) {
//                depthSumIncreases++
//            }
//            previousDepthSum = currentDepthSum
//        }
//        return depthSumIncreases

        return input
            .windowed(3)
            .zipWithNext()
            .count { (depthWindow1, depthWindow2) -> depthWindow2.sum() > depthWindow1.sum() }
    }

    fun solvePart1(): Int {
        return countDepthIncreases()
    }

    fun solvePart2(): Int {
        return countDepthSumIncreases()
    }
}


fun main() {
    val exampleInput = Resources.resourceAsListOfInt("day01_example.txt")
    val exampleSolutionPart1 = Day01(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day01(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfInt("day01.txt")
    val solutionPart1 = Day01(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day01(input).solvePart2()
    println(solutionPart2)
}