package org.baghdad.logic.usecase.report

import org.baghdad.logic.model.entities.ProjectSummaryReport
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.StateRepository
import org.baghdad.logic.repositories.TaskRepository


class ReportUseCase(
    private val reportService: ReportService
) {
    fun generateProjectSummary(): List<ProjectSummaryReport> {
        return reportService.summary()
    }
}