package org.baghdad.presentation.audit
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class AuditManagementUI(
    private val showAuditByProjectIdUI: ShowAuditByProjectIdUI,
    private val viewer: Viewer,
    private val reader: Reader
) {
    operator fun invoke(projectId: UUID) {
        while (true) {
            viewer.logMessage("=== Audit UI ===")
            viewer.logMessage("1. Show audit for this project")
            viewer.logMessage("0. Back")
            viewer.logMessage("Enter your choice: ")

            val choice = reader.readInput()?.toIntOrNull()

            when (choice) {
                1 -> showAuditByProjectIdUI.execute(projectId)
                0 -> return
                else -> viewer.logError("Invalid choice. Please try again.")
            }
        }
    }
}
