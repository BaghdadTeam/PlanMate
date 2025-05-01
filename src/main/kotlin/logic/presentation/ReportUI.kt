package logic.presentation
import org.baghdad.logic.usecase.report.ReportUseCase

class ReportUI(
    private val reportUseCase: ReportUseCase
) {

    fun viewReportCommand() {
        val reports = reportUseCase.generateProjectSummary()

        if (reports.isEmpty()) {
            println("⚠️ No projects found.")
            return
        }

        println("\n📁 List of Projects:")
        reports.forEachIndexed { index, report ->
            println("${index + 1}. ${report.projectName}")
        }

        print("\n🔢 Enter the number of the project to view its report: ")
        val input = readLine()?.toIntOrNull()

        if (input == null || input !in 1..reports.size) {
            println("❌ Invalid selection.")
            return
        }

        val selectedReport = reports[input - 1]

        println("\n📊 Project Report: ${selectedReport.projectName}")
        println("🧮 Total Tasks: ${selectedReport.totalTasks}")

        println("\n📍 Tasks by State:")
        if (selectedReport.tasksPerState.isEmpty()) {
            println("  No data available.")
        } else {
            selectedReport.tasksPerState.forEach { (state, count) ->
                println("  - $state: $count task(s)")
            }
        }

        println("\n👤 Tasks by User:")
        if (selectedReport.tasksPerUser.isEmpty()) {
            println("  No data available.")
        } else {
            selectedReport.tasksPerUser.forEach { (user, count) ->
                println("  - $user: $count task(s)")
            }
        }
    }
}
