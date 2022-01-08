package day20

import java.io.File

typealias Image = List<List<Char>>

const val darkPixel = '.'
const val lightPixel = '#'

fun parseImage(imageString: String): Image {
    return imageString.split(System.lineSeparator()).map { row -> row.toCharArray().toList() }
}

fun padImage(grid: Image, padChar: Char = darkPixel): Image {
    val paddingTopAndBottom = List(grid.first().size) { padChar }
    val paddingLeftAndRight = listOf(padChar)
    val newGrid = mutableListOf(paddingTopAndBottom, paddingTopAndBottom)
    newGrid.addAll(1, grid)
    return newGrid.map { paddingLeftAndRight + it + paddingLeftAndRight }
}

fun getOutputPixelIndex(grid: Image, x: Int, y: Int, voidChar: Char): Int {
    val relevantPixelRows = listOf(
        listOf(Pair(x - 1, y - 1), Pair(x, y - 1), Pair(x + 1, y - 1)),
        listOf(Pair(x - 1, y), Pair(x, y), Pair(x + 1, y)),
        listOf(Pair(x - 1, y + 1), Pair(x, y + 1), Pair(x + 1, y + 1)),
    )
    return relevantPixelRows.joinToString("") { pixelRow ->
        pixelRow.joinToString("") { (px, py) ->
            if (px in grid.first().indices && py in grid.indices) {
                if (grid[py][px] == darkPixel) "0" else "1"
            } else {
                if (voidChar == darkPixel) "0" else "1"
            }
        }
    }.toInt(2)
}

fun enhanceImage(algorithm: String, inputImage: Image, repetitions: Int): Image {
    val hasFlippingVoidChar = algorithm.first() == lightPixel && algorithm.last() == darkPixel
    var outputImage = inputImage
    repeat(repetitions) { index ->
        val voidChar = if (hasFlippingVoidChar) {
            if (index % 2 == 0) darkPixel else lightPixel
        } else darkPixel

        outputImage = padImage(outputImage, voidChar).let { paddedImage ->
            paddedImage.mapIndexed { y, row ->
                List(row.size) { x ->
                    val outputPixelIndex = getOutputPixelIndex(paddedImage, x, y, voidChar)
                    algorithm[outputPixelIndex]
                }
            }
        }
    }
    return outputImage
}

fun countLitPixels(image: Image): Int {
    return image.sumOf { row -> row.count { it == lightPixel } }
}

fun main() {
    val input = File("./src/main/kotlin/day20/input.txt").readLines()

    val algorithm = input.first()

    val inputImage = parseImage(input.drop(2).joinToString(System.lineSeparator()))

    val outputImage2 = enhanceImage(algorithm, inputImage, 2)
    val litPixels2 = countLitPixels(outputImage2)
    println(litPixels2)

    val outputImage50 = enhanceImage(algorithm, inputImage, 50)
    val litPixels50 = countLitPixels(outputImage50)
    println(litPixels50)
}
