package org.baghdad.presentation.audit

import org.baghdad.logic.model.exceptions.audit.NoProjectFoundException
import org.baghdad.logic.model.exceptions.audit.NoTaskFoundException
import org.baghdad.logic.model.exceptions.audit.UnSupportedTimeStampFormatException
import org.baghdad.logic.usecase.audit.AddAuditUseCase
import org.baghdad.logic.usecase.audit.GetAuditByProjectIdUseCase
import org.baghdad.logic.usecase.audit.GetAuditByTaskIdUseCase
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class ShowAuditByTaskIdUI(
    private val getAuditByTaskIdUseCase : GetAuditByTaskIdUseCase,
    private val viewer: Viewer
) {
    fun execute(taskId: UUID) {
        try {
            val auditEntitiesByTask = getAuditByTaskIdUseCase(taskId)
            auditEntitiesByTask.forEachIndexed { index, auditEntity ->
                viewer.logMessage("${index + 1} :" +
                    " ${auditEntity.user.type} " +
                    " ${auditEntity.user.name} " +
                    " ${auditEntity.action} " +
                    "at ${auditEntity.timestamp}")
            }
        } catch (_: UnSupportedTimeStampFormatException) {
            viewer.logError("Invalid timestamp format")
        } catch (_: NoTaskFoundException) {
            viewer.logError("No audit found for task with ID: $taskId")
        } catch (_: Exception) {
            viewer.logError("Something went wrong")
        }
    }
}