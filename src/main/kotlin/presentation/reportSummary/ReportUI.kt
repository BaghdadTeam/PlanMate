package org.baghdad.presentation.reportSummary
import org.baghdad.logic.usecase.report.ReportUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class ReportUI(
    private val reportUseCase: ReportUseCase,
    private val viewer: Viewer,
    private val reader: Reader
) {

    fun viewReportCommand() {
        val reports = reportUseCase.generateProjectSummary()

        if (reports.isEmpty()) {
            viewer.logError("⚠️ No projects found.")
            return
        }

        println("\n📁 List of Projects:")
        reports.forEachIndexed { index, report ->
            viewer.logMessage("${index + 1}. ${report.projectName}")
        }

        print("\n🔢 Enter the number of the project to view its report: ")
        val input = reader.readInput()?.toIntOrNull()

        if (input == null || input !in 1..reports.size) {
            viewer.logError("❌ Invalid selection.")
            return
        }

        val selectedReport = reports[input - 1]

        viewer.logMessage("\n📊 Project Report: ${selectedReport.projectName}")
        viewer.logMessage("🧮 Total Tasks: ${selectedReport.totalTasks}")

        println("\n📍 Tasks by State:")
        if (selectedReport.tasksPerState.isEmpty()) {
            viewer.logError("  No data available.")
        } else {
            selectedReport.tasksPerState.forEach { (state, count) ->
                viewer.logMessage("  - $state: $count task(s)")
            }
        }

        println("\n👤 Tasks by User:")
        if (selectedReport.tasksPerUser.isEmpty()) {
            viewer.logError("  No data available.")
        } else {
            selectedReport.tasksPerUser.forEach { (user, count) ->
                viewer.logMessage("  - $user: $count task(s)")
            }
        }
    }
}
