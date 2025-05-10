package presentation.audit

import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.exceptions.NoProjectFoundException
import org.baghdad.logic.model.exceptions.UnSupportedTimeStampFormatException
import org.baghdad.logic.usecase.audit.GetAuditByProjectIdUseCase
import org.baghdad.presentation.audit.ShowAuditByProjectIdUI
import org.baghdad.presentation.output.Viewer
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test

class ShowAuditByProjectIdUITest {
    private lateinit var showAuditByProjectIdUI: ShowAuditByProjectIdUI
    private lateinit var getAuditByProjectIdUseCase: GetAuditByProjectIdUseCase
    private lateinit var viewer: Viewer

    @BeforeEach
    fun setup() {
        viewer = mockk(relaxed = true)
        getAuditByProjectIdUseCase = mockk(relaxed = true)
        showAuditByProjectIdUI = ShowAuditByProjectIdUI(getAuditByProjectIdUseCase, viewer)
    }

    @Test
    fun `should handle general exception when getAuditByProjectIdUseCase throws general exception`() {
        // Given
        val projectID = UUID.randomUUID()

        // when
        coEvery { getAuditByProjectIdUseCase.invoke(projectID) } throws Exception()

        // then
        showAuditByProjectIdUI.execute(projectID)
        verify { viewer.logError("Something went wrong") }
    }

    @Test
    fun `should handle NoProjectFoundException when getAuditByProjectIdUseCase throws NoProjectFoundException`() {
        // Given
        val projectID = UUID.randomUUID()

        // when
        coEvery { getAuditByProjectIdUseCase.invoke(projectID) } throws NoProjectFoundException("No audit found for project with ID: $projectID")

        // then
        showAuditByProjectIdUI.execute(projectID)
        verify { viewer.logError("No audit found for project with ID: $projectID") }

    }

    @Test
    fun `should handle UnSupportedTimeStampFormatException when getAuditByProjectIdUseCase throws UnSupportedTimeStampFormatException`(){
        // Given
        val projectID = UUID.randomUUID()

        // when
        coEvery { getAuditByProjectIdUseCase.invoke(projectID) } throws UnSupportedTimeStampFormatException("Invalid timestamp format")

        // then
        showAuditByProjectIdUI.execute(projectID)
        verify { viewer.logError("Invalid timestamp format") }

    }

    @Test
    fun `should show audits by Project ID when getAuditByProjectIdUseCase returns audits`(){
        // Given
        val haider = createUserHelper()
        val taskID = UUID.randomUUID()
        val auditEntities = listOf(AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            projectId = taskID,
            action = "Create Project Aboud",
            user = haider ,
        ))
        // When
        coEvery { getAuditByProjectIdUseCase.invoke(taskID) } returns auditEntities

        // Then
        showAuditByProjectIdUI.execute(taskID)

        verify { viewer.logMessage("1 :" +
                " ${auditEntities[0].user.type} " +
                " ${auditEntities[0].user.name} " +
                " ${auditEntities[0].action} " +
                "at ${auditEntities[0].timestamp}") }

    }

}