package org.baghdad.presentation.projectStates

import org.baghdad.logic.usecase.projectstates.GetAllStatesPerProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class GetAllStatesPerProjectUI(
    private val useCase: GetAllStatesPerProjectUseCase,
    private val viewer: Viewer,
    private val reader: Reader
) {

    fun execute() {
        val projectId = promptForProjectId() ?: return

        try {
            val states = useCase.invoke(UUID.fromString(projectId))
            if (states.isEmpty()) {
                viewer.logMessage("No states found for project ID: $projectId")
            } else {
                viewer.logMessage("States for project ID: $projectId")
                states.forEachIndexed { index, state ->
                    viewer.logMessage("${index + 1}. ${state.name}")
                }
            }
        } catch (e: Exception) {
            viewer.logError("Failed to retrieve states: ${e.message}")
        }
    }

    private fun promptForProjectId(): String? {
        while (true) {
            viewer.logMessage("Enter the project ID to view its states:")
            val input = reader.readInput()
            if (!input.isNullOrBlank()) return input
            viewer.logError("Project ID cannot be blank. Please try again.")
        }
    }
}