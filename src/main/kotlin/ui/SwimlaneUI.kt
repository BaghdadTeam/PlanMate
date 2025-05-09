import org.baghdad.ui.RenderSwimlaneUI
import java.util.UUID

class SwimlaneUI(
    private val renderSwimlaneUI: RenderSwimlaneUI,
    private val projectStatesUI: ProjectStatesUI,
    private val taskUI: TaskUI,
    private val auditUI: AuditUI,
    private val inputProvider: () -> String? = { readLine() }
) {
    fun invoke(projectId: UUID) {
        var shouldContinue = true
        renderSwimlaneUI.invoke(projectId)

        while (shouldContinue) {
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
                    taskUI.invoke(projectId)
                }

                3 -> {
                    println("Navigating to Audit Log Screen...")
                    auditUI.invoke(projectId)
                }

                0 -> {
                    println("Returning to the previous screen...")
                    shouldContinue = false
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
