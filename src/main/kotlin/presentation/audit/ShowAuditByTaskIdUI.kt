package org.baghdad.presentation.audit

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.NoTaskFoundException
import org.baghdad.logic.model.exceptions.UnSupportedTimeStampFormatException
import org.baghdad.logic.usecase.audit.GetAuditByTaskIdUseCase
import org.baghdad.logic.usecase.user.GetUserByUserIdUseCase
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class ShowAuditByTaskIdUI(
    private val getAuditByTaskIdUseCase: GetAuditByTaskIdUseCase,
    private val getUserByIdUseCase: GetUserByUserIdUseCase,
    private val viewer: Viewer
) {
    fun execute(taskId: UUID) {
        runBlocking {
            try {
                var user : UserEntity
                val auditEntitiesByTask = getAuditByTaskIdUseCase(taskId)
                auditEntitiesByTask.forEachIndexed { index, auditEntity ->
                    user = getUserByIdUseCase(auditEntity.userId)
                    viewer.logMessage(
                           "${index + 1} :" +
                                " ${user.type} " +
                                " ${user.name} " +
                                " ${auditEntity.description} " +
                                "at ${auditEntity.timestamp}"
                    )
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
}