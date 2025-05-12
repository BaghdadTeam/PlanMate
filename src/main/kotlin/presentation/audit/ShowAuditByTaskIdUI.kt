package org.baghdad.presentation.audit

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.model.exceptions.NoTaskFoundException
import org.baghdad.logic.model.exceptions.UnSupportedTimeStampFormatException
import org.baghdad.logic.usecase.audit.GetAuditByTaskIdUseCase
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.utils.formatTimestamp
import java.util.UUID

class ShowAuditByTaskIdUI(
    private val getAuditByTaskIdUseCase: GetAuditByTaskIdUseCase,
    private val viewer: Viewer
) {
    fun execute(taskId: UUID) {
        runBlocking {
            try {
                val (auditsList , usersList) = getAuditByTaskIdUseCase(taskId)
                auditsList.zip(usersList).forEachIndexed { index, pair ->
                    val (auditEntity, userEntity) = pair
                    viewer.logMessage(
                        "${index + 1}: " +
                                "${userEntity.type} " +
                                "${userEntity.name} " +
                                "${auditEntity.description} " +
                                "at ${formatTimestamp(auditEntity.timestamp)}"
                    )
                }
            } catch (_: UnSupportedTimeStampFormatException) {
                viewer.logError("Invalid timestamp format")
            } catch (_: NoTaskFoundException) {
                viewer.logError("No audit found for task with ID: $taskId")
            } catch (e: Exception) {
                print(e)
                viewer.logError("Something went wrong")
            }
        }
    }
}