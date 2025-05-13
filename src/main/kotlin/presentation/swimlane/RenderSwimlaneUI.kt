package org.baghdad.presentation.swimlane
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.usecase.ViewServiceUseCase

class RenderSwimlaneUI(
) {

    operator fun invoke(swimlaneData: Map<TaskStateEntity, List<TaskEntity>>): String {
        return try {
            if (swimlaneData.isEmpty()) {
                "Error: Failed to fetch data"
            } else {
                val states = swimlaneData.keys.toList()
                val tasksByState = swimlaneData.entries.associate { (state, tasks) ->
                    state.name to tasks.map { it.title }
                }

                val maxRows = swimlaneData.values.maxOfOrNull { it.size } ?: 0

                val headerRow = buildHeaderRow(states)
                val separator = buildSeparator(states)
                val taskRows = buildTaskRows(states, tasksByState, maxRows)

                "$separator\n$headerRow\n$separator\n$taskRows\n$separator"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    private fun buildHeaderRow(states: List<TaskStateEntity>): String {
        return states.joinToString(" | ") { it.name.padEnd(10) }
    }

    private fun buildSeparator(states: List<TaskStateEntity>): String {
        return "+${"-".repeat(10)}+".repeat(states.size)
    }

    private fun buildTaskRows(
        states: List<TaskStateEntity>,
        tasksByState: Map<String, List<String>>,
        maxRows: Int
    ): String {
        return (0 until maxRows).joinToString("\n") { row ->
           states.joinToString(" | ") { state ->
                tasksByState[state.name]?.getOrNull(row)?.padEnd(10) ?: "".padEnd(10)
           }
       }
    }
}