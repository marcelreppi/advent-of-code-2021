import java.util.*
import kotlin.math.abs

class Day23(input: List<String>) {
    private val initialState = parseInput(input)

    private fun parseSlots(input: String): List<Char> {
        return Regex("[A-Z]").findAll(input)
            .toList()
            .map { it.value.toCharArray().first() }
    }

    private fun parseInput(input: List<String>): State {
        val hallway = Regex("(\\.)+").find(input[1])!!.value
        val roomSlot1 = parseSlots(input[3])
        val roomSlot2 = parseSlots(input[2])

        val rooms = (roomSlot1 zip roomSlot2)
            .mapIndexed { i, slots ->
                Room(slots.toList(), (i + 65).toChar(), 2)
            }
        return State(hallway, rooms)
    }

    private data class Room(val slots: List<Char>, val destinationFor: Char, val targetSize: Int) {
        val roomIndex = destinationFor.code - 65
        val hallwayIndex = roomIndex * 2 + 2
        fun isTargetState(): Boolean = slots.size == targetSize && slots.all { it == destinationFor }
        fun isPartiallyTargetState(): Boolean = !isEmpty() && slots.all { it == destinationFor }
        fun isEmpty(): Boolean = slots.isEmpty()
        fun containsUnwantedAmphipod(): Boolean = slots.any { it != destinationFor }

        fun enter(amphipod: Char): Room {
            if (amphipod != destinationFor) throw Error("Not allowed")
            val nextSlots = slots.toMutableList().apply { add(amphipod) }
            return Room(nextSlots, destinationFor, targetSize)
        }

        fun leave(): Room {
            val nextSlots = slots.toMutableList().apply { removeLast() }
            return Room(nextSlots, destinationFor, targetSize)
        }
    }

    private data class State(val hallway: String, val rooms: List<Room>, val totalEnergy: Int = 0) {
        private val stepEnergy = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)

        fun isTargetState(): Boolean {
            val hallwayIsEmpty = hallway.all { it == '.' }
            val roomsFilledCorrectly = rooms.all { it.isTargetState() }
            return hallwayIsEmpty && roomsFilledCorrectly
        }

        fun getRoomToHallwayTransitionStates(): List<State> {
            return rooms
                .filter { !it.isTargetState() && !it.isPartiallyTargetState() && !it.isEmpty() }
                .flatMap { room ->
                    val amphipod = room.slots.last()
                    hallway.indices
                        .filter { index ->
                            val notInFrontOfRoom = index !in rooms.map { it.hallwayIndex }

                            val fromHallwayIndex = minOf(index, room.hallwayIndex)
                            val toHallwayIndex = maxOf(index, room.hallwayIndex)
                            val nothingBlockingTheWay = (fromHallwayIndex..toHallwayIndex)
                                .all { hallway[it] == '.' }

                            // Possible improvement: Recognize and filter deadlocks?

                            notInFrontOfRoom && nothingBlockingTheWay
                        }
                        .map { index ->
                            val nextHallway = hallway.toCharArray().apply { set(index, amphipod) }.joinToString("")
                            val nextRooms = rooms.toMutableList().map {
                                if (it == room) it.leave() else it.copy()
                            }
                            val hallwaySteps = abs(index - room.hallwayIndex)
                            val roomSteps = room.targetSize + 1 - room.slots.size
                            val nextEnergy = totalEnergy + (hallwaySteps + roomSteps) * stepEnergy[amphipod]!!
                            State(nextHallway, nextRooms, nextEnergy)
                        }
                }
        }

        fun getHallwayToRoomTransitionStates(): List<State> {
            return hallway.indices
                .filter { hallway[it] != '.' }
                .flatMap { index ->
                    val amphipod = hallway[index]
                    rooms
                        .filter { room ->
                            val roomNotDone = !room.isTargetState()
                            val allowedToEnter = !room.containsUnwantedAmphipod() && room.destinationFor == amphipod

                            val fromHallwayIndex = minOf(index, room.hallwayIndex)
                            val toHallwayIndex = maxOf(index, room.hallwayIndex)
                            val nothingBlockingTheWay = (fromHallwayIndex..toHallwayIndex)
                                .all { hallway[it] == if (it == index) amphipod else '.' }

                            roomNotDone && allowedToEnter && nothingBlockingTheWay
                        }
                        .map { room ->
                            val nextHallway = hallway.toCharArray().apply { set(index, '.') }.joinToString("")
                            val nextRooms = rooms.toMutableList().map {
                                if (it == room) it.enter(amphipod) else it.copy()
                            }
                            val hallwaySteps = abs(index - room.hallwayIndex)
                            val roomSteps = room.targetSize - room.slots.size
                            val nextEnergy = totalEnergy + (hallwaySteps + roomSteps) * stepEnergy[amphipod]!!
                            State(nextHallway, nextRooms, nextEnergy)
                        }
                }
        }
    }

    private fun solve(initialState: State): Int {
        val energyComparator: Comparator<State> = compareBy { state -> state.totalEnergy }
        val statesToExplore: PriorityQueue<State> = PriorityQueue(energyComparator)
        statesToExplore.add(initialState)

        val minEnergyPerState = mutableMapOf(initialState to initialState.totalEnergy)

        while (statesToExplore.isNotEmpty()) {
            val state = statesToExplore.remove()

            if (state.isTargetState()) return state.totalEnergy

            val roomToHallwayTransitions = state.getRoomToHallwayTransitionStates()
            val hallwayToRoomTransitions = state.getHallwayToRoomTransitionStates()
            val nextStates = (roomToHallwayTransitions + hallwayToRoomTransitions).filter {
                it.totalEnergy < (minEnergyPerState[it] ?: Int.MAX_VALUE)
            }
            nextStates.forEach { minEnergyPerState[it] = it.totalEnergy }

            statesToExplore.addAll(nextStates)
        }

        return -1
    }


    fun solvePart1(): Int {
        return solve(initialState)

    }

    fun solvePart2(): Int {
        val extraRoomSlot1 = parseSlots("#D#B#A#C#")
        val extraRoomSlot2 = parseSlots("#D#C#B#A#")
        val slotPairs = (extraRoomSlot1 zip extraRoomSlot2)
        val newRooms = initialState.rooms.toMutableList().map { room ->
            val newSlots = room.slots.toMutableList().apply {
                addAll(1, slotPairs[room.roomIndex].toList())
            }
            room.copy(slots = newSlots, targetSize = newSlots.size)
        }
        val newInitialState = initialState.copy(rooms = newRooms)

        return solve(newInitialState)
    }
}

fun main() {
    val exampleInput = Resources.resourceAsListOfString("day23_example.txt")
    val exampleSolutionPart1 = Day23(exampleInput).solvePart1()
    println(exampleSolutionPart1)
    val exampleSolutionPart2 = Day23(exampleInput).solvePart2()
    println(exampleSolutionPart2)

    val input = Resources.resourceAsListOfString("day23.txt")
    val solutionPart1 = Day23(input).solvePart1()
    println(solutionPart1)
    val solutionPart2 = Day23(input).solvePart2()
    println(solutionPart2)
}