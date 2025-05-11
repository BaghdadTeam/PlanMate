package org.baghdad.presentation.audit

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.NoProjectFoundException
import org.baghdad.logic.model.exceptions.UnSupportedTimeStampFormatException
import org.baghdad.logic.usecase.audit.GetAuditByProjectIdUseCase
import org.baghdad.logic.usecase.user.GetUserByUserIdUseCase
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class ShowAuditByProjectIdUI(
    private val getAuditByProjectIdUseCase: GetAuditByProjectIdUseCase,
    private val getUserByIdUseCase: GetUserByUserIdUseCase,
    private val viewer: Viewer
) {
    fun execute(projectId: UUID) {

        runBlocking {
            try {
                var user : UserEntity
                val auditEntitiesByProject = getAuditByProjectIdUseCase(projectId)
                auditEntitiesByProject.forEachIndexed { index, auditEntity ->
                    user = getUserByIdUseCase(auditEntity.userId)
                    viewer.logMessage(
                        "${index + 1} :" +
                                " ${user.type} " +
                                " ${user.name} " +
                                " ${auditEntity.action} " +
                                "at ${auditEntity.timestamp}"
                    )
                }
            } catch (_: UnSupportedTimeStampFormatException) {
                viewer.logError("Invalid timestamp format")
            } catch (_: NoProjectFoundException) {
                viewer.logError("No audit found for project with ID: $projectId")
            } catch (e: Exception) {
                println(e)
                viewer.logError("Something went wrong")
            }
        }
    }
}