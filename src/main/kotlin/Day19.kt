class Day19(input: String) {
    private val scanners: List<Set<Point3D>> = parseInput(input)

    private data class Transform(val scanner: Point3D, val beacons: Set<Point3D>)
    private data class Solution(val scanners: Set<Point3D>, val beacons: Set<Point3D>)

    private fun parseInput(input: String): List<Set<Point3D>> {
        return input
            .split(System.lineSeparator().repeat(2))
            .map { singleScanner ->
                singleScanner
                    .lines()
                    .drop(1)
                    .map { Point3D.from(it) }
                    .toSet()
            }
    }

    private fun findTransformIfIntersects(left: Set<Point3D>, right: Set<Point3D>): Transform? =
        (0 until 6).firstNotNullOfOrNull { face ->
            (0 until 4).firstNotNullOfOrNull { rotation ->
                val rightReoriented = right.map { it.face(face).rotate(rotation) }.toSet()
                left.firstNotNullOfOrNull { b1 ->
                    rightReoriented.firstNotNullOfOrNull { b2 ->
                        val difference = b1 - b2
                        val moved = rightReoriented.map { it + difference }.toSet()
                        if (moved.intersect(left).size >= 12) {
                            Transform(difference, moved)
                        } else null
                    }
                }
            }
        }

    private fun solve(): Solution {
        val baseSector = scanners.first().toMutableSet()
        val foundScanners = mutableSetOf(Point3D(0, 0, 0))
        val unmappedSectors = ArrayDeque<Set<Point3D>>().apply { addAll(scanners.drop(1)) }
        while (unmappedSectors.isNotEmpty()) {
            val thisSector = unmappedSectors.removeFirst()
            when (val transform = findTransformIfIntersects(baseSector, thisSector)) {
                null -> unmappedSectors.add(thisSector)
                else -> {
                    baseSector.addAll(transform.beacons)
                    foundScanners.add(transform.scanner)
                }
            }
        }
        return Solution(foundScanners, baseSector)
    }

    fun solvePart1(): Int {
        return solve().beacons.size
    }

    fun solvePart2(): Int {
        return solve().scanners.pairs().maxOf { it.first distanceTo it.second }
    }
}

fun main() {
    val exampleInput = Resources.resourceAsText("day19_example.txt")
    val exampleSolutionPart1 = Day19(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day19(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsText("day19.txt")
    val solutionPart1 = Day19(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day19(input).solvePart2()
    println(solutionPart2)
}