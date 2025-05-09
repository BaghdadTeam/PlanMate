import org.baghdad.logic.usecase.ViewServiceUseCase
import org.baghdad.presentation.projectStates.ProjectStatesUI
import org.baghdad.presentation.swimlane.RenderSwimlaneUI
import org.baghdad.presentation.task.TaskManagementGatherUI
import java.util.UUID

class SwimlaneUI(
    private val renderSwimlaneUI: RenderSwimlaneUI,
    private val projectStatesUI: ProjectStatesUI,
    private val taskUI: TaskManagementGatherUI,
//    private val auditUI: AuditUI,
    private val inputProvider: () -> String? = { readLine() },
    private val viewServiceUseCase: ViewServiceUseCase
) {
    suspend fun invoke(projectId: UUID) {
        renderSwimlaneUI.invoke(projectId)
        val project = viewServiceUseCase.swimlane(projectId)
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
            when (inputProvider()?.toIntOrNull()) {
                1 -> {
                    println("Navigating to Project States Screen...")
                    projectStatesUI.invoke(projectId)
                }

                2 -> {
                    println("Navigating to Tasks Screen...")
                    taskUI.execute(projectId, project.keys.toList(), project.values.flatten())
                }

//                3 -> {
//                    println("Navigating to Audit Log Screen...")
//                    auditUI.invoke(projectId)
//                }

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
    }
}
