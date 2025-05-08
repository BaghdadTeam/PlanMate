package org.baghdad.logic.usecase.report

import org.baghdad.logic.model.entities.ProjectSummaryReport


class ReportUseCase(
    private val reportService: ReportService
) {
    suspend fun generateProjectSummary(): List<ProjectSummaryReport> {
        return reportService.summary()
    }
}