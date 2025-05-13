package org.baghdad.logic.usecase.report

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.ProjectSummaryReport
import org.baghdad.logic.model.exceptions.UnauthorizedException


class ReportUseCase(
    private val reportService: ReportService,
    private val sessionManager: SessionManager,
) {
    suspend fun generateProjectSummary(): List<ProjectSummaryReport> {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException()
        return reportService.summary()
    }
}