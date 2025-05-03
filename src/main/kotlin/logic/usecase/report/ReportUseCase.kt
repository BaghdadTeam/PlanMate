package org.baghdad.logic.usecase.report

import org.baghdad.logic.model.entities.ProjectSummaryReport


class ReportUseCase(
    private val reportService: ReportService
) {
    fun generateProjectSummary(): List<ProjectSummaryReport> {
        return reportService.summary()
    }
}