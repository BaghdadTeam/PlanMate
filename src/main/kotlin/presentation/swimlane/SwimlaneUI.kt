package org.baghdad.presentation.swimlane

import org.baghdad.logic.usecase.ViewServiceUseCase
import org.baghdad.presentation.audit.ShowAuditByProjectIdUI
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.projectStates.ProjectStatesUI
import org.baghdad.presentation.task.TaskManagementGatherUI
import java.util.UUID

class SwimlaneUI(
    private val renderSwimlaneUI: RenderSwimlaneUI,
    private val projectStatesUI: ProjectStatesUI,
    private val taskUI: TaskManagementGatherUI,
    private val auditByProjectIdUI: ShowAuditByProjectIdUI,
    private val viewServiceUseCase: ViewServiceUseCase,
    private val reader: Reader,
    private val viewer: Viewer

) {
    fun invoke(projectId: UUID) {
        try {
//            renderSwimlaneUI(projectId)


            while (true) {
                println(
                    """
                   == Plan Mate ==

                Choose an action:
                1- Manage States (Admin Only)
                2- Manage Tasks
                3- View Audit Log
                0- Back to Previous Screen
            """.trimIndent()
                )

                print("Enter your choice: ")
                when (reader.readInput()?.toIntOrNull()) {
                    1 -> {
                        println("Navigating to Project States Screen...")
                        projectStatesUI.invoke(projectId)
                    }

                    2 -> {
                        println("Navigating to Tasks Screen...")
                        taskUI.execute(projectId)
                    }

                    3 -> {
                        println("Navigating to Audit Log Screen...")
                        auditByProjectIdUI.execute(projectId)
                    }

                    0 -> {
                        println("Returning to the previous screen...")
                        return
                    }

                    null -> {
                        println("Invalid input. Please enter a number.")
                    }

                    else -> {
                        println("Invalid choice. Please try again.")
                    }
                }
                println()
            }
        } catch (exception: Exception) {

        }
    }
}
