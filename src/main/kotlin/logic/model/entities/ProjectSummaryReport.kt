package org.baghdad.logic.model.entities

data class ProjectSummaryReport(
    val projectName: String,
    val totalTasks: Int,
    val tasksPerState: Map<String, Int>,
    val tasksPerUser: Map<String, Int>
)
