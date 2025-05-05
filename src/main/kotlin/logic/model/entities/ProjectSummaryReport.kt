package org.baghdad.logic.model.entities

import java.util.UUID

data class ProjectSummaryReport(
    val projectName: String,
    val totalTasks: Int,
    val tasksPerState: Map<UUID, Int>,
    val tasksPerUser: Map<UUID, Int>
)
