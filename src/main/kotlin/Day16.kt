class Day16(input: List<String>) {
    private val hexString = input.first()

    private val binaryString = hexString
        .map {
            val i = Integer.parseInt(it.toString(), 16)
            Integer.toBinaryString(i).padStart(4, '0')
        }.joinToString("")

    private fun performCalculation(typeId: Int, values: List<Long>): Long {
        return when (typeId) {
            0 -> values.sum()
            1 -> values.reduce { acc, l -> acc * l }
            2 -> values.minOrNull() ?: throw Error("wtf")
            3 -> values.maxOrNull() ?: throw Error("wtf")
            5 -> if (values[0] > values[1]) 1 else 0
            6 -> if (values[0] < values[1]) 1 else 0
            7 -> if (values[0] == values[1]) 1 else 0
            else -> throw Error("wtf")
        }
    }

    private var versionSum = 0

    /**
     * @return Pair of length and value
     */
    private fun parseSubpacket(packet: String): Pair<Int, Long?> {
        if (packet.all { it == '0' } || packet.isEmpty()) return Pair(0, null)

        val versionString = packet.substring(0, 3)
        val version = Integer.parseInt(versionString, 2)
        val typeString = packet.substring(3, 6)
        val type = Integer.parseInt(typeString, 2)

        versionSum += version

        if (type == 4) {
            var literalValueString = ""
            var pointer = 6
            while (true) {
                val prefixBit = packet[pointer]
                val nextPointer = pointer + 5
                literalValueString += packet.substring(pointer + 1, nextPointer)
                pointer = nextPointer
                if (prefixBit == '0') break
            }
            val literalValue = literalValueString.toLong(2)
            return Pair(pointer, literalValue)
        } else {
            val lengthTypeId = packet[6]
            if (lengthTypeId == '0') {
                val placeholderBits = 15
                val subpacketsStart = 7 + placeholderBits
                val lengthOfSubpacketsString = packet.substring(7, subpacketsStart)
                val lengthOfSubpackets = Integer.parseInt(lengthOfSubpacketsString, 2)
                val packetLength = subpacketsStart + lengthOfSubpackets

                var nextSubpacketStart = subpacketsStart

                val subpacketValues = mutableListOf<Long?>()
                while (nextSubpacketStart < packetLength) {
                    val (subpacketLength, subpacketValue) = parseSubpacket(
                        packet.substring(
                            nextSubpacketStart,
                            packetLength
                        )
                    )
                    nextSubpacketStart += subpacketLength
                    subpacketValues.add(subpacketValue)
                }
                val subpacketResult = performCalculation(type, subpacketValues.filterNotNull())

                return Pair(packetLength, subpacketResult)
            } else {
                val placeholderBits = 11
                val subpacketsStart = 7 + placeholderBits
                val numberOfSubpacketsString = packet.substring(7, subpacketsStart)
                val numberOfSubpackets = Integer.parseInt(numberOfSubpacketsString, 2)

                var nextSubpacketStart = subpacketsStart
                val subpacketValues = mutableListOf<Long?>()
                for (i in 1..numberOfSubpackets) {
                    val (subpacketLength, subpacketValue) = parseSubpacket(packet.substring(nextSubpacketStart))
                    nextSubpacketStart += subpacketLength
                    subpacketValues.add(subpacketValue)
                }
                val packetValue = performCalculation(type, subpacketValues.filterNotNull())

                return Pair(nextSubpacketStart, packetValue)
            }
        }
    }

    fun solvePart1(): Int {
        parseSubpacket(binaryString)
        return versionSum
    }

    fun solvePart2(): Long {
        val (packetSize, packetValue) = parseSubpacket(binaryString)
        return packetValue!!
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day16_example.txt")
    val exampleSolutionPart1 = Day16(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day16(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day16.txt")
    val solutionPart1 = Day16(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day16(input).solvePart2()
    println(solutionPart2)
}