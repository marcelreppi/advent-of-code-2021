typealias EdgeToEdgeMap = Map<String, List<String>>
typealias Path = List<String>

class Day12(input: List<String>) {
    private val edgeToEdgeMapping = parseInput(input)

    private fun parseInput(input: List<String>): EdgeToEdgeMap {
        return input
            .flatMap {
                val (from, to) = it.split("-")
                listOf(from to to, to to from)
            }
            .groupBy({ it.first }, { it.second })
    }

    private fun part1VisitRule(cave: String, path: List<String>): Boolean =
        cave.isUpperCase() || cave !in path

    private fun part2VisitRule(cave: String, path: List<String>): Boolean =
        when {
            cave.isUpperCase() -> true
            cave == "start" -> false
            cave !in path -> true
            else -> path
                .filterNot { it.isUpperCase() }
                .groupBy { it }
                .none { it.value.size == 2 }
        }

    private fun String.isUpperCase(): Boolean = all { it.isUpperCase() }

    private fun findPaths(
        allowedToVisit: (String, Path) -> Boolean,
        currentPath: Path = listOf("start")
    ): List<Path> {
        val currentCave = currentPath.last()

        if (currentCave == "end") return listOf(currentPath)


        val connections = edgeToEdgeMapping.getValue(currentCave)
        return connections
            .filter { nextCave -> allowedToVisit(nextCave, currentPath) }
            .flatMap { nextCave -> findPaths(allowedToVisit, currentPath + nextCave) }
    }


    fun solvePart1(): Int {
        return findPaths(::part1VisitRule).size
    }

    fun solvePart2(): Int {
        return findPaths(::part2VisitRule).size
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day12_example.txt")
    val exampleSolutionPart1 = Day12(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day12(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day12.txt")
    val solutionPart1 = Day12(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day12(input).solvePart2()
    println(solutionPart2)
}