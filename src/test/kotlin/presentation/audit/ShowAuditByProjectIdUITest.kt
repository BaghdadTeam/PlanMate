package presentation.audit

import helpers.audit.AuditTestData
import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.exceptions.NoProjectFoundException
import org.baghdad.logic.model.exceptions.UnSupportedTimeStampFormatException
import org.baghdad.logic.usecase.audit.GetAuditByProjectIdUseCase
import org.baghdad.logic.usecase.user.GetUserByUserIdUseCase
import org.baghdad.presentation.audit.ShowAuditByProjectIdUI
import org.baghdad.presentation.output.Viewer
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test

class ShowAuditByProjectIdUITest {
    private lateinit var showAuditByProjectIdUI: ShowAuditByProjectIdUI
    private lateinit var getAuditByProjectIdUseCase: GetAuditByProjectIdUseCase
    private lateinit var getUserByIdUseCase: GetUserByUserIdUseCase
    private lateinit var viewer: Viewer

    @BeforeEach
    fun setup() {
        viewer = mockk(relaxed = true)
        getAuditByProjectIdUseCase = mockk(relaxed = true)
        getUserByIdUseCase = mockk(relaxed = true)
        showAuditByProjectIdUI =
            ShowAuditByProjectIdUI(getAuditByProjectIdUseCase, getUserByIdUseCase, viewer)
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
    fun `should handle UnSupportedTimeStampFormatException when getAuditByProjectIdUseCase throws UnSupportedTimeStampFormatException`() {
        // Given
        val projectID = UUID.randomUUID()

        // when
        coEvery { getAuditByProjectIdUseCase.invoke(projectID) } throws UnSupportedTimeStampFormatException(
            "Invalid timestamp format"
        )

        // then
        showAuditByProjectIdUI.execute(projectID)
        verify { viewer.logError("Invalid timestamp format") }

    }

    @Test
    fun `should show audits by Project ID when getAuditByProjectIdUseCase returns audits`() {
        // Given
        val auditEntities = listOf(AuditTestData.createAuditHelper())
        val user = createUserHelper(id = auditEntities[0].userId)

        // When
        coEvery { getUserByIdUseCase.invoke(auditEntities[0].userId) } returns user
        coEvery { getAuditByProjectIdUseCase.invoke(auditEntities[0].projectId) } returns auditEntities

        // Then
        showAuditByProjectIdUI.execute(auditEntities[0].projectId)

        verify {
            viewer.logMessage(
                "1 :" +
                        " ${user.type} " +
                        " ${user.name} " +
                        " ${auditEntities[0].action} " +
                        "at ${auditEntities[0].timestamp}"
            )
        }

    }

}