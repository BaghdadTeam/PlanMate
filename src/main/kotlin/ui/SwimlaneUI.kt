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
        renderSwimlaneUI(projectId)
        while (shouldContinue) {
            println(
                """
                                   == Plan Mate ==
                                   
                [1- To Do]         [2- In Progress]         [3- Done]

                1- create ui         3- creating logic         2- creating data source

                                    1- to modify or create a state (Only for Admins)
                                    2- to modify or create a task
                                    3- view audit
                                    0- back to previous screen
            """.trimIndent()
            )

            print("Enter your choice: ")
            when (scanner.nextLine().toIntOrNull()) {
                1 -> {
                    println("Navigating to ProjectStatesUI...")
                    projectStatesUI.invoke(projectId)
                }

                2 -> {
                    println("Navigating to TaskUI...")
                    taskUI.invoke(projectId)
                }

                3 -> {
                    println("Navigating to AuditUI...")
                    auditUI.invoke(projectId)
                }

                0 -> {
                    println("Returning to previous screen...")
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