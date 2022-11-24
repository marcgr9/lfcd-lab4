import java.io.File
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
    var initialState: Char = ' ',
    var endStates: MutableList<Char> = mutableListOf(),
) {

    companion object {

        fun fromFile(file: String): FiniteAutomata {
            val result = FiniteAutomata()

            var lineNumber = 0
            File(file).forEachLine {
                val splitLine = it.trim().split(" ").map { it.toCharArray()[0] }
                when (lineNumber) {
                    0 -> result.states.addAll(splitLine)
                    1 -> result.alphabet.addAll(splitLine)
                    2 -> result.initialState = splitLine[0]
                    3 -> result.endStates.addAll(splitLine)
                    else -> {
                        result.transitions.add(
                            Transition(
                                splitLine[0], splitLine[2], splitLine[1]
                            )
                        )
                    }
                }
                lineNumber++
            }

            return result
        }

    }

    fun isAccepted(dfa: String): Boolean {
        var currentState = initialState
        dfa.forEach {
            currentState = getNextState(currentState, it)
        }
        return endStates.contains(currentState)
    }

    private fun getNextState(currentState: Char, value: Char): Char
        = transitions.find {
            it.startState == currentState && it.value == value
        }?.endState ?: ' '

}

fun main(args: Array<String>) {
    val fa = FiniteAutomata.fromFile("src/main/resources/data.in")

    println(getMenu())
    while (true) {
        with(Scanner(System.`in`)) {
            when (nextInt()) {
                0 -> println(getMenu())
                1 -> println(fa.states)
                2 -> println(fa.alphabet)
                3 -> println(fa.transitions)
                4 -> println(fa.initialState)
                5 -> println(fa.endStates)
                6 -> {
                    print("DFA: ")
                    val str = next()
                    println(
                        "The DFA is " +
                        (if (fa.isAccepted(str)) " " else "not ") +
                        "accepted."
                    )
                }
            }
        }
    }
}

fun getMenu() =
    """
        0. Menu
        1. Display set of states
        2. Display the alphabet
        3. Display all the transitions
        4. Display the initial state
        5. Display the final states
        6. Verify if a DFA is accepted by the FA
    """.trimIndent()
