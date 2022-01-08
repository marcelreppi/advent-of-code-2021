package day22

import java.io.File

fun parseRange(rangeString: String): Pair<Int, Int> {
    val (from, to) = rangeString.drop(2).split("..").map { it.toInt() }
    return Pair(from, to)
}

private data class Cuboid(
    val on: Boolean, val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int, val zMin: Int, val zMax: Int
) {
    companion object {
        fun from(input: String): Cuboid {
            val on = input.substringBefore(" ") == "on"
            val (xRangeString, yRangeString, zRangeString) = input.substringAfter(" ").split(",")
            val (xMin, xMax) = parseRange(xRangeString)
            val (yMin, yMax) = parseRange(yRangeString)
            val (zMin, zMax) = parseRange(zRangeString)
            return Cuboid(on, xMin, xMax, yMin, yMax, zMin, zMax)

        }
    }

    fun volume(): Long {
        return (xMax - xMin + 1).toLong() * (yMax - yMin + 1).toLong() * (zMax - zMin + 1).toLong() * if (on) 1 else -1
    }

    fun intersect(other: Cuboid): Cuboid? {
        if (xMin <= other.xMax && xMax >= other.xMin) {
            if (yMin <= other.yMax && yMax >= other.yMin) {
                if (zMin <= other.zMax && zMax >= other.zMin) {
                    val xMinInt = maxOf(xMin, other.xMin)
                    val xMaxInt = minOf(xMax, other.xMax)
                    val yMinInt = maxOf(yMin, other.yMin)
                    val yMaxInt = minOf(yMax, other.yMax)
                    val zMinInt = maxOf(zMin, other.zMin)
                    val zMaxInt = minOf(zMax, other.zMax)
                    return Cuboid(!on, xMinInt, xMaxInt, yMinInt, yMaxInt, zMinInt, zMaxInt)
                }
            }
        }
        return null
    }
}

fun main() {
    val input = File("./src/main/kotlin/day22/input.txt").readLines()

    val cuboids = input.map(Cuboid::from)

    val cubeStates: MutableMap<String, Boolean> = mutableMapOf()
    for (cuboid in cuboids) {
        for (x in maxOf(-50, cuboid.xMin)..minOf(50, cuboid.xMax)) {
            for (y in maxOf(-50, cuboid.yMin)..minOf(50, cuboid.yMax)) {
                for (z in maxOf(-50, cuboid.zMin)..minOf(50, cuboid.zMax)) {
                    cubeStates["$x,$y,$z"] = cuboid.on
                }
            }
        }
    }
    val onCubes = cubeStates.values.count { it }
    println(onCubes)

    val volumes = mutableListOf<Cuboid>()
    for (cuboid in cuboids) {
        volumes.addAll(volumes.mapNotNull { it.intersect(cuboid) })
        if (cuboid.on) volumes.add(cuboid)
    }
    val volumeSum = volumes.sumOf { it.volume() }
    println(volumeSum)
}
