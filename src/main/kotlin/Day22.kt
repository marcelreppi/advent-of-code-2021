class Day22(input: List<String>) {
    private val cuboids = input.map(Cuboid::from)

    private data class Cuboid(
        val on: Boolean, val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int, val zMin: Int, val zMax: Int
    ) {
        companion object {
            fun from(input: String): Cuboid {
                fun parseRange(rangeString: String): Pair<Int, Int> {
                    val (from, to) = rangeString.drop(2).split("..").map { it.toInt() }
                    return Pair(from, to)
                }

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

    fun solvePart1(): Int {
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
        return cubeStates.values.count { it }
    }

    fun solvePart2(): Long {
        val volumes = mutableListOf<Cuboid>()
        for (cuboid in cuboids) {
            volumes.addAll(volumes.mapNotNull { it.intersect(cuboid) })
            if (cuboid.on) volumes.add(cuboid)
        }
        return volumes.sumOf { it.volume() }
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day22_example.txt")
    val exampleSolutionPart1 = Day22(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day22(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day22.txt")
    val solutionPart1 = Day22(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day22(input).solvePart2()
    println(solutionPart2)
}