package org.baghdad.presentation.swimlane

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.usecase.ViewServiceUseCase
import java.util.*

class RenderSwimlaneUI(
    private val viewServiceUseCase: ViewServiceUseCase
) {

    operator fun invoke(projectId: UUID): String {
        val swimlaneData = runBlocking { viewServiceUseCase.swimlane(projectId) }
         if(swimlaneData.isNotEmpty()) {

            val states = swimlaneData.keys.toList()

            val tasksByState = swimlaneData.entries.associate { (state, tasks) ->
                state.name to tasks.map { it.title }
            }

            val maxRows = swimlaneData.values.maxOfOrNull { it.size } ?: 0

            val headerRow = states.joinToString(" | ")
            { it.name.padEnd(10) }
            val separator = "+${"-".repeat(10)}+".repeat(states.size)

            val taskRows = (0 until maxRows).joinToString("\n") { row ->
                states.joinToString(" | ") { state ->
                    tasksByState[state.name]
                        ?.getOrNull(row)
                        ?.padEnd(10)
                        ?: "".padEnd(10)
                }
            }

            return """
                $separator
                | $headerRow |
                $separator
                $taskRows
                $separator
            """.trimIndent()

        } else {
            throw Exception("Error fetching swimlane data")
        }
    }
}