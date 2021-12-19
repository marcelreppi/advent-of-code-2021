package day05

import java.io.File

data class Coordinate(val x: Int, val y: Int) {
    companion object {
        fun fromCommaString(commaString: String): Coordinate {
            val coordinateValues = commaString.split(",").map { it.toInt() }
            return Coordinate(coordinateValues.first(), coordinateValues.last())
        }
    }
}

data class Line(val start: Coordinate, val end: Coordinate)

fun parseLine(line: String): Line {
    val stringCoordinates = line.split(" -> ")
    val startCoordinate = Coordinate.fromCommaString(stringCoordinates.first())
    val endCoordinate = Coordinate.fromCommaString(stringCoordinates.last())
    return Line(startCoordinate, endCoordinate)
}

fun isVertical(line: Line): Boolean {
    return line.start.x == line.end.x
}

fun isHorizontal(line: Line): Boolean {
    return line.start.y == line.end.y
}

fun main() {
    val input = File("./src/main/kotlin/day05/input.txt").readLines()

    var overlapCounter = 0

    // { Column4: Row2: [Coordinate1, Coordinate2] } }
    val map: MutableMap<Int, MutableMap<Int, MutableList<Coordinate>>> = mutableMapOf()

    fun addToMap(coordinate: Coordinate) {
        val column = map[coordinate.x]
        if (column != null) {
            val row = column[coordinate.y]
            if (row != null) {
                row.add(coordinate)

                if (row.size == 2) {
                    overlapCounter++
                }
            } else {
                column[coordinate.y] = mutableListOf(coordinate)
            }
        } else {
            val row = mutableListOf(coordinate)
            map[coordinate.x] = mutableMapOf(Pair(coordinate.y, row))
        }
    }

    for (lineString in input) {
        val line = parseLine(lineString)

        when {
            isVertical(line) -> {
                val fromY = line.start.y.coerceAtMost(line.end.y)
                val toY = line.start.y.coerceAtLeast(line.end.y)
                for (y in fromY..toY) {
                    addToMap(Coordinate(line.start.x, y))
                }
            }
            isHorizontal(line) -> {
                val fromX = line.start.x.coerceAtMost(line.end.x)
                val toX = line.start.x.coerceAtLeast(line.end.x)
                for (x in fromX..toX) {
                    addToMap(Coordinate(x, line.start.y))
                }
            }
            else -> {
                val xCoordinates = if (line.start.x < line.end.x) {
                    (line.start.x..line.end.x).toList()
                } else {
                    (line.end.x..line.start.x).toList().reversed()
                }
                val yCoordinates = if (line.start.y < line.end.y) {
                    (line.start.y..line.end.y).toList()
                } else {
                    (line.end.y..line.start.y).toList().reversed()
                }

                val diagonalCoordinates = (xCoordinates zip yCoordinates).map { Coordinate(it.first, it.second) }
                for (coordinate in diagonalCoordinates) {
                    addToMap(coordinate)
                }
            }
        }
    }

    println(overlapCounter)
}