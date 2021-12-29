package day12

import java.io.File

data class Path(val caves: MutableList<String>, var doubleSmallCave: String? = null)

fun main() {
    val input = File("./src/main/kotlin/day12/input.txt").readLines()

    val vertices = mutableMapOf<String, MutableList<String>>()

    for (line in input) {
        val (from, to) = line.split("-")

        val existingConnectionsFrom = vertices[from]
        if (existingConnectionsFrom != null) {
            existingConnectionsFrom.add(to)
        } else {
            vertices[from] = mutableListOf(to)
        }
        val existingConnectionsTo = vertices[to]
        if (existingConnectionsTo != null) {
            existingConnectionsTo.add(from)
        } else {
            vertices[to] = mutableListOf(from)
        }
    }

    val paths = mutableListOf<Path>()

    val startingPath = Path(mutableListOf("start"))

    fun doStep(currentPath: Path) {
        val currentCave = currentPath.caves.last()
        if (currentCave == "end") return
        val connections = vertices[currentCave] ?: throw Error("Wtf")

        for (nextCave in connections) {
            when (nextCave) {
                "start" -> continue
                "end" -> {
                    val finalPath = Path(currentPath.caves.toMutableList(), currentPath.doubleSmallCave)
                    finalPath.caves.add(nextCave)
                    paths.add(finalPath)
                }
                else -> {
                    val nextPath = Path(currentPath.caves.toMutableList(), currentPath.doubleSmallCave)

                    val isSmallCave = nextCave.all { it.isLowerCase() }
                    if (isSmallCave) {
                        // Part 1
//                        val hasBeenVisited = currentPath.caves.contains(nextCave)
//                        if (hasBeenVisited) continue

                        // Part 2
                        val hasDoubleCave = currentPath.doubleSmallCave != null
                        val hasBeenVisited = currentPath.caves.contains(nextCave)
                        if (hasDoubleCave) {
                            if (hasBeenVisited) continue
                        } else {
                            if (hasBeenVisited) {
                                nextPath.doubleSmallCave = nextCave
                            }
                        }
                    }

                    nextPath.caves.add(nextCave)

                    doStep(nextPath)
                }
            }
        }
    }

    doStep(startingPath)

    println(paths.size)
}
