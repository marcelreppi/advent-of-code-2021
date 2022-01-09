typealias Image = List<List<Char>>

class Day20(input: List<String>) {
    private val darkPixel = '.'
    private val lightPixel = '#'

    private val algorithm = input.first()
    private val inputImage: Image = parseImage(input)

    private fun parseImage(input: List<String>): Image {
        return input.drop(2).map { row -> row.toCharArray().toList() }
    }

    private fun padImage(grid: Image, padChar: Char = darkPixel): Image {
        val paddingTopAndBottom = List(grid.first().size) { padChar }
        val paddingLeftAndRight = listOf(padChar)
        val newGrid = mutableListOf(paddingTopAndBottom, paddingTopAndBottom)
        newGrid.addAll(1, grid)
        return newGrid.map { paddingLeftAndRight + it + paddingLeftAndRight }
    }

    private fun getOutputPixelIndex(grid: Image, x: Int, y: Int, voidChar: Char): Int {
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

    private fun enhanceImage(algorithm: String, inputImage: Image, repetitions: Int): Image {
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

    private fun countLitPixels(image: Image): Int {
        return image.sumOf { row -> row.count { it == lightPixel } }
    }

    fun solvePart1(): Int {
        val outputImage = enhanceImage(algorithm, inputImage, 2)
        return countLitPixels(outputImage)
    }

    fun solvePart2(): Int {
        val outputImage = enhanceImage(algorithm, inputImage, 50)
        return countLitPixels(outputImage)
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day20_example.txt")
    val exampleSolutionPart1 = Day20(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day20(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day20.txt")
    val solutionPart1 = Day20(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day20(input).solvePart2()
    println(solutionPart2)
}