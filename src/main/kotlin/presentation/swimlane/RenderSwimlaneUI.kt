package org.baghdad.presentation.swimlane
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.TaskStateEntity

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

                val columnWidths = calculateColumnWidths(states, tasksByState)

                val separator = buildSeparator(columnWidths)
                val headerRow = buildHeaderRow(states, columnWidths)
                val taskRows = buildTaskRows(states, tasksByState, columnWidths, maxRows)

                // Final composed table with full borders
                buildString {
                    appendLine(separator)
                    appendLine(headerRow)
                    appendLine(separator)
                    appendLine(taskRows)
                    appendLine(separator)
                }
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    private fun calculateColumnWidths(
        states: List<TaskStateEntity>,
        tasksByState: Map<String, List<String>>
    ): Map<String, Int> {
        return states.associate { state ->
            val name = state.name
            val maxTaskLength = tasksByState[name]?.maxOfOrNull { it.length } ?: 0
            val width = maxOf(name.length, maxTaskLength, 10) + 2 // padding
            name to width
        }
    }

    private fun buildSeparator(columnWidths: Map<String, Int>): String {
        return columnWidths.values.joinToString("+", prefix = "+", postfix = "+") {
            "-".repeat(it+2)
        }
    }

    private fun buildHeaderRow(states: List<TaskStateEntity>, columnWidths: Map<String, Int>): String {
        return states.joinToString(" | ", prefix = "| ", postfix = " |") { state ->
            state.name.padEnd(columnWidths[state.name] ?: 12)
        }
    }

    private fun buildTaskRows(
        states: List<TaskStateEntity>,
        tasksByState: Map<String, List<String>>,
        columnWidths: Map<String, Int>,
        maxRows: Int
    ): String {
        return (0 until maxRows).joinToString("\n") { row ->
            states.joinToString(" | ", prefix = "| ", postfix = " |") { state ->
                val task = tasksByState[state.name]?.getOrNull(row) ?: ""
                task.padEnd(columnWidths[state.name] ?: 12)
            }
        }
    }
}