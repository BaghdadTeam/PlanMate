package presentation.audit

import helpers.audit.AuditTestData
import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.exceptions.NoAuditForTaskException
import org.baghdad.logic.model.exceptions.UnSupportedTimeStampFormatException
import org.baghdad.logic.usecase.audit.GetAuditByTaskIdUseCase
import org.baghdad.presentation.audit.ShowAuditByTaskIdUI
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.utils.formatTimestamp
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
        showAuditByTaskIdUI = ShowAuditByTaskIdUI(getAuditByTaskIdUseCase, viewer )
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
                NoAuditForTaskException("No audit found for task with ID: $taskID")

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
        val auditEntities = listOf(AuditTestData.createAuditHelper())
        val user = createUserHelper(id = auditEntities[0].userId)

        // When
        coEvery { getAuditByTaskIdUseCase.invoke(auditEntities[0].entityUnderAuditId) } returns Pair((auditEntities), listOf(user))

        // Then
        showAuditByTaskIdUI.execute(auditEntities[0].entityUnderAuditId)

        verify { viewer.logMessage("1: " +
                "${user.type} " +
                "${user.name} " +
                "${auditEntities[0].description} " +
                "at ${formatTimestamp(auditEntities[0].timestamp)}") }
    }
}