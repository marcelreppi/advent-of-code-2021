class Day24(input: List<String>) {
    private val magicParameters: List<Parameters> = parseMagicParameters(input)

    private data class Parameters(val a: Int, val b: Int, val c: Int)

    private fun parseMagicParameters(input: List<String>): List<Parameters> = input.chunked(18).map {
        Parameters(
            it[4].substringAfterLast(" ").toInt(),
            it[5].substringAfterLast(" ").toInt(),
            it[15].substringAfterLast(" ").toInt()
        )
    }

    private fun magicFunction(parameters: Parameters, z: Long, w: Long): Long =
        if (z % 26 + parameters.b != w) ((z / parameters.a) * 26) + w + parameters.c
        else z / parameters.a

    private fun solve(): Map<Long, Pair<Long, Long>> {
        var zValues = mutableMapOf(0L to (0L to 0L))
        magicParameters.forEach { parameters ->
            val zValuesThisRound = mutableMapOf<Long, Pair<Long, Long>>()
            zValues.forEach { (z, minMax) ->
                (1..9).forEach { digit ->
                    val newValueForZ = magicFunction(parameters, z, digit.toLong())
                    if (parameters.a == 1 || (parameters.a == 26 && newValueForZ < z)) {
                        zValuesThisRound[newValueForZ] = minOf(
                            zValuesThisRound[newValueForZ]?.first ?: Long.MAX_VALUE, minMax.first * 10 + digit
                        ) to maxOf(
                            zValuesThisRound[newValueForZ]?.second ?: Long.MIN_VALUE, minMax.second * 10 + digit
                        )
                    }
                }
            }
            zValues = zValuesThisRound
        }
        return zValues
    }

    fun solvePart1(): Long {
        return solve()[0]!!.second
    }

    fun solvePart2(): Long {
        return solve()[0]!!.first
    }
}

fun main() {
//    val exampleInput = Resources.resourceAsListOfString("day24_example.txt")
//    val exampleSolutionPart1 = Day24(exampleInput).solvePart1()
//    println(exampleSolutionPart1)
//    val exampleSolutionPart2 = Day24(exampleInput).solvePart2()
//    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day24.txt")
    val solutionPart1 = Day24(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day24(input).solvePart2()
    println(solutionPart2)
}