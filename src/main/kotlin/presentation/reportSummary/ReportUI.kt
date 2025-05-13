package org.baghdad.presentation.reportSummary

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.usecase.report.ReportUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class ReportUI(
    private val reportUseCase: ReportUseCase,
    private val viewer: Viewer,
    private val reader: Reader
) {

    fun viewReportCommand() {
        try {
            runBlocking {
                val reports = reportUseCase.generateProjectSummary()

                if (reports.isEmpty()) {
                    viewer.logError("⚠️ No projects found.")
                    return@runBlocking
                }

                println("\n📁 List of Projects:")
                reports.forEachIndexed { index, report ->
                    viewer.logMessage("${index + 1}. ${report.projectName}")
                }

                print("\n🔢 Enter the number of the project to view its report: ")
                val input = reader.readInput()?.toIntOrNull()

                if (input == null || input !in 1..reports.size) {
                    viewer.logError("❌ Invalid selection.")
                    return@runBlocking
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
        } catch (_: Exception) {
            viewer.logError("something went wrong")
        }

    }
}




//class ReportUI(
//    private val reportUseCase: ReportUseCase,
//    private val viewer: Viewer,
//    private val reader: Reader
//) {
//
//    fun viewReportCommand() = runBlocking {
//        try {
//            val reports = reportUseCase.generateProjectSummary()
//
//            if (reports.isEmpty()) {
//                viewer.logError("⚠️ No projects found.")
//                return@runBlocking
//            }
//
//            showProjectList(reports)
//            val selectedIndex = getUserSelection(reports.size) ?: return@runBlocking
//            val selectedReport = reports[selectedIndex]
//
//            displayReport(selectedReport)
//
//        } catch (e: Exception) {
//            viewer.logError("❌ Something went wrong: ${e.message}")
//        }
//    }
//
//    private fun showProjectList(reports: List<ProjectReport>) {
//        viewer.logMessage("\n📁 List of Projects:")
//        reports.forEachIndexed { index, report ->
//            viewer.logMessage("${index + 1}. ${report.projectName}")
//        }
//    }
//
//    private fun getUserSelection(max: Int): Int? {
//        viewer.logMessage("\n🔢 Enter the number of the project to view its report:")
//        val input = reader.readInput()?.toIntOrNull()
//
//        return if (input == null || input !in 1..max) {
//            viewer.logError("❌ Invalid selection.")
//            null
//        } else {
//            input - 1
//        }
//    }
//
//    private fun displayReport(report: ProjectReport) {
//        viewer.logMessage("\n📊 Project Report: ${report.projectName}")
//        viewer.logMessage("🧮 Total Tasks: ${report.totalTasks}")
//
//        viewer.logMessage("\n📍 Tasks by State:")
//        if (report.tasksPerState.isEmpty()) {
//            viewer.logError("  No data available.")
//        } else {
//            report.tasksPerState.forEach { (state, count) ->
//                viewer.logMessage("  - $state: $count task(s)")
//            }
//        }
//
//        viewer.logMessage("\n👤 Tasks by User:")
//        if (report.tasksPerUser.isEmpty()) {
//            viewer.logError("  No data available.")
//        } else {
//            report.tasksPerUser.forEach { (user, count) ->
//                viewer.logMessage("  - $user: $count task(s)")
//            }
//        }
//    }
//}
//


//data class ProjectSummary(
//    val projectName: String,
//    val totalTasks: Int,
//    val tasksPerState: Map<String, Int>,
//    val tasksPerUser: Map<String, Int>
//)
//class ReportUI(
//    private val reportUseCase: ReportUseCase,
//    private val viewer: Viewer,
//    private val reader: Reader
//) {
//
//    fun viewReportCommand() = runBlocking {
//        try {
//            val summaries = reportUseCase.generateProjectSummary()
//
//            if (summaries.isEmpty()) {
//                viewer.logError("⚠️ No projects found.")
//                return@runBlocking
//            }
//
//            showProjectList(summaries)
//            val selectedIndex = getUserSelection(summaries.size) ?: return@runBlocking
//            val selected = summaries[selectedIndex]
//
//            displayReport(selected)
//
//        } catch (e: Exception) {
//            viewer.logError("❌ Something went wrong: ${e.message}")
//        }
//    }
//
//    private fun showProjectList(summaries: List<Any>) {
//        viewer.logMessage("\n📁 List of Projects:")
//        summaries.forEachIndexed { index, summary ->
//            val name = extractProjectName(summary)
//            viewer.logMessage("${index + 1}. $name")
//        }
//    }
//
//    private fun getUserSelection(max: Int): Int? {
//        viewer.logMessage("\n🔢 Enter the number of the project to view its report:")
//        val input = reader.readInput()?.toIntOrNull()
//        return if (input == null || input !in 1..max) {
//            viewer.logError("❌ Invalid selection.")
//            null
//        } else {
//            input - 1
//        }
//    }
//
//    private fun displayReport(summary: Any) {
//        val name = extractProjectName(summary)
//        val totalTasks = extractTotalTasks(summary)
//        val tasksByState = extractTasksByState(summary)
//        val tasksByUser = extractTasksByUser(summary)
//
//        viewer.logMessage("\n📊 Project Report: $name")
//        viewer.logMessage("🧮 Total Tasks: $totalTasks")
//
//        viewer.logMessage("\n📍 Tasks by State:")
//        if (tasksByState.isEmpty()) {
//            viewer.logError("  No data available.")
//        } else {
//            tasksByState.forEach { (state, count) ->
//                viewer.logMessage("  - $state: $count task(s)")
//            }
//        }
//
//        viewer.logMessage("\n👤 Tasks by User:")
//        if (tasksByUser.isEmpty()) {
//            viewer.logError("  No data available.")
//        } else {
//            tasksByUser.forEach { (user, count) ->
//                viewer.logMessage("  - $user: $count task(s)")
//            }
//        }
//    }
//
//    // --- Replace the following extractors with actual property accessors depending on your model type ---
//
//    private fun extractProjectName(summary: Any): String {
//        // replace with actual casting and access
//        return (summary as? Map<*, *>)?.get("projectName") as? String ?: "Unnamed"
//    }
//
//    private fun extractTotalTasks(summary: Any): Int {
//        return (summary as? Map<*, *>)?.get("totalTasks") as? Int ?: 0
//    }
//
//    private fun extractTasksByState(summary: Any): Map<String, Int> {
//        return (summary as? Map<*, *>)?.get("tasksPerState") as? Map<String, Int> ?: emptyMap()
//    }
//
//    private fun extractTasksByUser(summary: Any): Map<String, Int> {
//        return (summary as? Map<*, *>)?.get("tasksPerUser") as? Map<String, Int> ?: emptyMap()
//    }
//}
//
