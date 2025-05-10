package org.baghdad.presentation.projectStates

import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class ProjectStatesUI(
    private val addStateToProjectUI: AddStateToProjectUI,
    private val deleteStateForProjectUI: DeleteStateForProjectUI,
    private val editProjectStateUI: EditProjectStateUI,
    private val getAllStatesPerProjectUI  : GetAllStatesPerProjectUI,
    private val reader: Reader,
    private val viewer: Viewer

) {
    fun invoke(projectId: UUID) {
        viewer.logMessage(" === Project States === ")
        viewer.logMessage("1 - Add State to Project")
        viewer.logMessage("2 - Delete State for Project")
        viewer.logMessage("3 - Edit Project State")
        viewer.logMessage("0 - Back to Previous Screen")

        when (reader.readInput()?.toIntOrNull()) {
            1 -> addStateToProjectUI.execute(projectId)
            2 -> {
                val stateId = getStateIdFromUser(projectId , "delete")
                if (stateId != null) {
                    deleteStateForProjectUI.execute(stateId)
                }
                else {
                    viewer.logError("Invalid choice. Please try again.")
                }
            }
            3 -> {
                val stateId = getStateIdFromUser(projectId , "edit")
                if (stateId != null) {
                    editProjectStateUI.execute(projectId , stateId)
                }else{
                    viewer.logError("Invalid choice. Please try again.")
                }
            }
            0 -> return
            else -> viewer.logError("Invalid choice. Please try again.")

        }
    }
    private fun getStateIdFromUser(projectId: UUID , action : String) : UUID?{
        val statesIds = getAllStatesPerProjectUI.execute(projectId)
        viewer.logMessage("Enter the state number of the state to $action:")
        val choice = reader.readInput()?.toIntOrNull()
        return if (choice != null && choice in 1..statesIds.size) {
            statesIds[choice - 1]
        }else{
            null
        }
    }
}