package org.baghdad.presentation.audit

import org.baghdad.presentation.output.Viewer
import java.util.UUID

class AuditMenuUI(
    private val showByProjectId: ShowAuditByProjectIdUI,
    private val showByTaskId: ShowAuditByTaskIdUI,
    private val viewer: Viewer
) {
    operator fun invoke() {
        while (true) {
            viewer.logMessage(
                """
                |=== Audit Menu ===
                |1. Show Audit by Project ID
                |2. Show Audit by Task ID
                |0. Exit
                |Enter your choice:
                """.trimMargin()
            )

            when (readLine()?.trim()) {
                "1" -> {
                    viewer.logMessage("Enter Project ID:")
                    val input = readLine()
                    try {
                        val projectId = UUID.fromString(input)
                        showByProjectId.execute(projectId)
                    } catch (_: Exception) {
                        viewer.logError("Invalid Project ID format.")
                    }
                }

                "2" -> {
                    viewer.logMessage("Enter Task ID:")
                    val input = readLine()
                    try {
                        val taskId = UUID.fromString(input)
                        showByTaskId.execute(taskId)
                    } catch (_: Exception) {
                        viewer.logError("Invalid Task ID format.")
                    }
                }

                "0" -> {
                    viewer.logMessage("Exiting audit menu.")
                    break
                }

                else -> viewer.logError("Invalid choice, please try again.")
            }
        }
    }
}