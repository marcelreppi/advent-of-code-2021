package day19

import Point3D
import pairs
import java.io.File

fun parseInput(input: String): List<Set<Point3D>> {
    return input.split(System.lineSeparator().repeat(2)).map { singleScanner ->
        singleScanner.lines().drop(1).map { Point3D.from(it) }.toSet()
    }

}

class Transform(val scanner: Point3D, val beacons: Set<Point3D>)

fun findTransformIfIntersects(left: Set<Point3D>, right: Set<Point3D>): Transform? =
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

fun main() {
    val input = File("./src/main/kotlin/day19/input.txt").readText()

    val scanners: List<Set<Point3D>> = parseInput(input)

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

    println(baseSector.size)

    val largestManhattanDistance = foundScanners.pairs().maxOf { (s1, s2) -> s1 distanceTo s2 }
    println(largestManhattanDistance)

}