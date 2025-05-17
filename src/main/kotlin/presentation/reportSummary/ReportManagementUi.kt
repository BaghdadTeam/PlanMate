package org.baghdad.presentation.reportSummary
import org.baghdad.presentation.audit.ShowAuditByProjectIdUI
import org.baghdad.presentation.audit.ShowAuditByTaskIdUI
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.task.GetTasksByProjectIdUI
import java.util.*

class ReportManagementUi(
    private val reportUI: ReportUI,
    private val viewer: Viewer,
    private val reader: Reader
    ) {
    fun showReportMenu() {
        while (true) {
            viewer.logMessage("\n========= 📊 Report Management =========")
            viewer.logMessage("1. View Project Summary Report")
            viewer.logMessage("0. Return to Main Menu")
            viewer.logMessage("========================================")
            viewer.logMessage("Enter your choice: ")

            when (reader.readInput()?.trim()) {
                "1" -> reportUI.viewReportCommand()
                "0" -> {
                    viewer.logMessage("🔙 Returning to main menu...")
                    break
                }
                else -> viewer.logError("❌ Invalid choice. Please try again.")
            }
        }
    }
}

