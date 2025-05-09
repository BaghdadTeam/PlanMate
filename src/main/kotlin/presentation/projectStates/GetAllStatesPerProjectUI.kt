package org.baghdad.presentation.projectStates

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.usecase.projectstates.GetAllStatesPerProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class GetAllStatesPerProjectUI(
    private val useCase: GetAllStatesPerProjectUseCase,
    private val viewer: Viewer,
) {

    fun execute(projectId : UUID) : List<UUID> {
        val statesUUIDs = mutableListOf<UUID>()

        try {
            runBlocking {
                val states = useCase.invoke(projectId)
                if (states.isEmpty()) {
                    viewer.logMessage("No states found for this project")
                } else {
                    states.forEachIndexed { index, state ->
                        viewer.logMessage("${index + 1}. ${state.name}")
                        statesUUIDs.add(state.id)
                    }

                }

            }
            return statesUUIDs
        } catch (e: Exception) {
            viewer.logError("Failed to retrieve states: ${e.message}")
            return emptyList()
        }
    }


}