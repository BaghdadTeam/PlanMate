package logic.usecase.audit

import helpers.audit.AuditTestData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.exceptions.EmptyActionInAuditEntityException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.usecase.audit.AddAuditUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddAuditUseCaseTest {
    private lateinit var auditRepository: AuditRepository
    private lateinit var addAuditUseCase: AddAuditUseCase
    private val sessionManager = mockk<SessionManager>(relaxed = true)

    @BeforeEach
    fun setup() {
        auditRepository = mockk(relaxed = true)
        addAuditUseCase = AddAuditUseCase(auditRepository,sessionManager)
        coEvery { sessionManager.isAuthenticated() } returns true

    }
    @Test
    fun `Should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows<Exception> {
            addAuditUseCase.invoke(AuditTestData.createAuditHelper())
        }

    }

    @Test
    fun `should throw no exception when add audit`() = runTest {
        // Given
        val auditLogEntity = AuditTestData.createAuditHelper()


        // when
        addAuditUseCase.invoke(auditLogEntity)

    }

    @Test
    fun `should throw EmptyActionInAuditEntityException when add audit with empty description`() =
        runTest {
            // Given
            val auditLogEntity = AuditTestData.createAuditHelper(description = "")


            // when & Then
            assertThrows<EmptyActionInAuditEntityException> { addAuditUseCase.invoke(auditLogEntity) }

        }

    @Test
    fun `should throw EmptyActionInAuditEntityException when add audit with blank description`() =
        runTest {
            // Given
            val auditLogEntity = AuditTestData.createAuditHelper(description = " ")


            // when & Then
            assertThrows<EmptyActionInAuditEntityException> { addAuditUseCase.invoke(auditLogEntity) }

        }
}