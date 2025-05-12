package org.baghdad.presentation.audit

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.NoProjectFoundException
import org.baghdad.logic.model.exceptions.UnSupportedTimeStampFormatException
import org.baghdad.logic.usecase.audit.GetAuditByProjectIdUseCase
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class ShowAuditByProjectIdUI(
    private val getAuditByProjectIdUseCase: GetAuditByProjectIdUseCase,
    private val viewer: Viewer
) {
    fun execute(projectId: UUID) {

        runBlocking {
            try {
                val (auditsList , usersList) = getAuditByProjectIdUseCase(projectId)
                auditsList.zip(usersList).forEachIndexed { index, pair ->
                    val (auditEntity, userEntity) = pair
                    viewer.logMessage(
                           "${index + 1} :" +
                                " ${userEntity.type} " +
                                " ${userEntity.name} " +
                                " ${auditEntity.description} " +
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