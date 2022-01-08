package day24

import java.io.File


class Parameters(val a: Int, val b: Int, val c: Int)

fun parseMagicParameters(input: List<String>): List<Parameters> = input.chunked(18).map {
    Parameters(
        it[4].substringAfterLast(" ").toInt(),
        it[5].substringAfterLast(" ").toInt(),
        it[15].substringAfterLast(" ").toInt()
    )
}

fun magicFunction(parameters: Parameters, z: Long, w: Long): Long =
    if (z % 26 + parameters.b != w) ((z / parameters.a) * 26) + w + parameters.c
    else z / parameters.a

fun main() {
    val input = File("./src/main/kotlin/day24/input.txt").readLines()


    val magicParameters: List<Parameters> = parseMagicParameters(input)

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

    println(zValues[0]?.first)
    println(zValues[0]?.second)

}
