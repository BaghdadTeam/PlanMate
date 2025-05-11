package presentation.audit

import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.exceptions.NoTaskFoundException
import org.baghdad.logic.model.exceptions.UnSupportedTimeStampFormatException
import org.baghdad.logic.usecase.audit.GetAuditByTaskIdUseCase
import org.baghdad.presentation.audit.ShowAuditByTaskIdUI
import org.baghdad.presentation.output.Viewer
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test

class ShowAuditByTaskIdUITest {
    private lateinit var showAuditByTaskIdUI: ShowAuditByTaskIdUI
    private lateinit var getAuditByTaskIdUseCase: GetAuditByTaskIdUseCase
    private lateinit var viewer: Viewer


    @BeforeEach
    fun setup() {
        viewer = mockk(relaxed = true)
        getAuditByTaskIdUseCase = mockk(relaxed = true)
        showAuditByTaskIdUI = ShowAuditByTaskIdUI(getAuditByTaskIdUseCase, viewer)
    }


    @Test
    fun `should handle general exception when getAuditByTaskIdUseCase throws general exception`() {
        // Given
        val taskID = UUID.randomUUID()

        // when
        coEvery { getAuditByTaskIdUseCase.invoke(taskID) } throws Exception()

        // then
        showAuditByTaskIdUI.execute(taskID)
        verify { viewer.logError("Something went wrong") }
    }

    @Test
    fun `should handle NoTaskFoundException when getAuditByTaskIdUseCase throws NoTaskFoundException`() {
        // Given
        val taskID = UUID.randomUUID()

        // when
        coEvery { getAuditByTaskIdUseCase.invoke(taskID) } throws
                NoTaskFoundException("No audit found for task with ID: $taskID")

        // then
        showAuditByTaskIdUI.execute(taskID)
        verify { viewer.logError("No audit found for task with ID: $taskID") }

    }

    @Test
    fun `should handle UnSupportedTimeStampFormatException when getAuditByTaskIdUseCase throws UnSupportedTimeStampFormatException`() {
        // Given
        val taskID = UUID.randomUUID()

        // when
        coEvery { getAuditByTaskIdUseCase.invoke(taskID) } throws UnSupportedTimeStampFormatException(
            "Invalid timestamp format"
        )

        // then
        showAuditByTaskIdUI.execute(taskID)
        verify { viewer.logError("Invalid timestamp format") }

    }

    @Test
    fun `should show audits by Task ID when getAuditByTaskIdUseCase returns audits`(){
        // Given
        val haider = createUserHelper()
        val taskID = UUID.randomUUID()
        val auditEntities = listOf(
            AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            projectId = taskID,
            action = "Create Task Aboud",
            user = haider ,
        )
        )
        // When
        coEvery { getAuditByTaskIdUseCase.invoke(taskID) } returns auditEntities

        // Then
        showAuditByTaskIdUI.execute(taskID)

        verify { viewer.logMessage("1 :" +
                " ${auditEntities[0].user.type} " +
                " ${auditEntities[0].user.name} " +
                " ${auditEntities[0].action} " +
                "at ${auditEntities[0].timestamp}") }

    }
}