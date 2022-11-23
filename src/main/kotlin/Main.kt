import java.io.File
import java.lang.RuntimeException
import java.util.Scanner

data class Transition(
    val startState: Char,
    val endState: Char,
    val value: Char,
) {

    override fun toString(): String
        = "T[$startState $endState $value]"

}

class FiniteAutomata(
    val alphabet: MutableList<Char> = mutableListOf(),
    val states: MutableList<Char> = mutableListOf(),
    val transitions: MutableList<Transition> = mutableListOf(),
    var initialStates: Char = ' ',
    var endStates: Char = ' ',
) {

    companion object {

        fun fromFile(file: String): FiniteAutomata {
            val result = FiniteAutomata()

            var lineNumber = 0
            File(file).forEachLine {
                val splitLine = it.trim().split(" ").map { it.toCharArray()[0] }
                when (lineNumber) {
                    0 -> {
                        result.states.addAll(splitLine)
                    }
                    1 -> {
                        result.alphabet.addAll(splitLine)
                    }
                    2 -> {
                        result.initialStates = splitLine[0]
                    }
                    3 -> {
                        result.endStates = splitLine[0]
                    }
                    else -> {
                        result.transitions.add(
                            Transition(
                                splitLine[0], splitLine[1], splitLine[2]
                            )
                        )
                    }
                }
                lineNumber++
            }

            return result
        }

    }

    fun getNextState(currentState: Char, value: Char): Char
        = transitions.find {
            it.startState == currentState && it.value == value
        }?.endState ?: ' '

    fun isAccepted(variable: String): Boolean {
        var currentState = initialStates
        variable.forEach {
            currentState = getNextState(currentState, it)
        }
        return endStates == currentState
    }
}

fun main(args: Array<String>) {
    val fa = FiniteAutomata.fromFile("src/main/resources/data.in")

    while (true) {
        with(Scanner(System.`in`)) {
            when (nextInt()) {
                1 -> println(fa.states)
                2 -> println(fa.alphabet)
                3 -> println(fa.transitions)
                4 -> println(fa.initialStates)
                5 -> println(fa.endStates)
                6 -> {
                    val str = next()
                    if (fa.isAccepted(str)) {
                        println("Accepted")
                    } else {
                        println("Not accepted")
                    }
                }
            }
        }
    }
}
