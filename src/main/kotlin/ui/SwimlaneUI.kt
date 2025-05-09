package org.baghdad.ui

import java.util.Scanner
import java.util.UUID

class SwimlaneUI(
    private val renderSwimlaneUI: RenderSwimlaneUI,
    private val projectStatesUI: ProjectStatesUI,
    private val taskUI: TaskUI,
    private val auditUI: AuditUI
) {
    fun invoke(projectId: UUID) {
        var shouldContinue = true
        val scanner = Scanner(System.`in`)
        renderSwimlaneUI.invoke(projectId) // Call to render the Swimlane with dynamic data

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
            when (scanner.nextLine().toIntOrNull()) {
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
        scanner.close()
    }
}