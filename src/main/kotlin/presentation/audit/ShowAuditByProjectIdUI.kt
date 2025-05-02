package org.baghdad.presentation.audit

import org.baghdad.logic.model.exceptions.audit.NoProjectFoundException
import org.baghdad.logic.model.exceptions.audit.UnSupportedTimeStampFormatException
import org.baghdad.logic.usecase.audit.GetAuditByProjectIdUseCase
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class ShowAuditByProjectIdUI(
    private val getAuditByProjectIdUseCase: GetAuditByProjectIdUseCase,
    private val viewer: Viewer
) {
    fun execute(projectId: UUID) {
        try {
            val auditEntitiesByProject = getAuditByProjectIdUseCase(projectId)
            auditEntitiesByProject.forEachIndexed { index, auditEntity ->
                viewer.logMessage("${index + 1} :" +
                        " ${auditEntity.user.type} " +
                        " ${auditEntity.user.name} " +
                        " ${auditEntity.action} " +
                        "at ${auditEntity.timestamp}")
            }
        } catch (_: UnSupportedTimeStampFormatException) {
            viewer.logError("Invalid timestamp format")
        } catch (_: NoProjectFoundException) {
            viewer.logError("No audit found for project with ID: $projectId")
        } catch (e: Exception) {
            viewer.logError("Something went wrong")
        }
    }
}