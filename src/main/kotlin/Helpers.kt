import kotlin.math.abs
import kotlin.math.sign

data class Point2D(val x: Int, val y: Int) {
    companion object {
        fun from(csvInput: String): Point2D {
            val (x, y) = csvInput.split(",").map { it.toInt() }
            return Point2D(x, y)
        }
    }

    infix fun lineTo(other: Point2D): List<Point2D> {
        val xDelta = (other.x - x).sign
        val yDelta = (other.y - y).sign

        val steps = maxOf(abs((x - other.x)), abs((y - other.y)))

        return (1..steps).scan(this) { prev, _ -> Point2D(prev.x + xDelta, prev.y + yDelta) }
    }

    infix fun <T> isWithin(grid: List<List<T>>): Boolean {
        return y in grid.indices && x in grid.first().indices
    }

    fun getAdjacentPoints(includeDiagonal: Boolean = false): List<Point2D> {
        val horizontalAndVerticalNeighbors = listOf(
            Point2D(x, y - 1), // Top
            Point2D(x + 1, y), // Right
            Point2D(x, y + 1), // Bottom
            Point2D(x - 1, y)  // Left
        )
        if (!includeDiagonal) return horizontalAndVerticalNeighbors

        val diagonalNeighbors = listOf(
            Point2D(x - 1, y - 1), // Top Left
            Point2D(x + 1, y - 1), // Top Right
            Point2D(x - 1, y + 1), // Bottom Left
            Point2D(x + 1, y + 1), // Bottom Right
        )
        return horizontalAndVerticalNeighbors + diagonalNeighbors
    }
}

data class Point3D(val x: Int, val y: Int, val z: Int) {
    companion object {
        fun from(csvInput: String): Point3D {
            return csvInput.split(",").let {
                Point3D(it[0].toInt(), it[1].toInt(), it[2].toInt())
            }
        }
    }

    operator fun plus(other: Point3D): Point3D {
        return Point3D(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: Point3D): Point3D {
        return Point3D(x - other.x, y - other.y, z - other.z)
    }

    infix fun distanceTo(other: Point3D): Int {
        return abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    }

    fun face(facing: Int): Point3D = when (facing) {
        // Using right-hand rule
        // Start with this position
        //   y (index finger pointing up)
        //   |__x (thumb pointing right)
        //  /
        // z (middle finger pointing towards you)
        0 -> this                   // Initial position
        1 -> Point3D(z, y, -x)      // Rotate +90° around y so that z (middle finger) now points to the left (-x)
        2 -> Point3D(-x, y, -z)     // Rotate +180° around y so that z (middle finger) now points away from you (-z)
        3 -> Point3D(-z, y, x)      // Rotate -90° around y so that z (middle finger) now points to the right (x)
        4 -> Point3D(x, -z, y)      // Rotate +90° around x so that z (middle finger) now points upwards (y)
        5 -> Point3D(x, z, -y)      // Rotate -90° around x so that z (middle finger) now points downwards (-y)
        else -> error("Invalid facing")
    }

    fun rotate(rotating: Int): Point3D = when (rotating) {
        0 -> this                   // Quadrant I
        1 -> Point3D(-y, x, z)      // Quadrant II
        2 -> Point3D(-x, -y, z)     // Quadrant III
        3 -> Point3D(y, -x, z)      // Quadrant IV
        else -> error("Invalid rotation")
    }
}

fun <T> printGrid(grid: List<List<T>>, transformer: (T) -> String) {
    grid.forEach { row -> println(row.joinToString("", transform = transformer)) }
}

fun getAdjacentPoints(
    x: Int,
    y: Int,
    includeDiagonal: Boolean = false
): List<Point2D> {
    return Point2D(x, y).getAdjacentPoints(includeDiagonal)
}

fun <T> isWithin(x: Int, y: Int, grid: List<List<T>>): Boolean {
    return Point2D(x, y) isWithin grid
}