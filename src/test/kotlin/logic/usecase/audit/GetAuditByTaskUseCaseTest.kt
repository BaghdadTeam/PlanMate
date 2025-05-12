package logic.usecase.audit

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.usecase.audit.GetAuditByTaskIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class GetAuditByTaskUseCaseTest {
    lateinit var  auditRepository : AuditRepository
    lateinit var  getAuditByTaskUseCase: GetAuditByTaskIdUseCase
    private val sessionManager: SessionManager = mockk()


    @BeforeEach
    fun setup(){
        auditRepository = mockk(relaxed = true)
        getAuditByTaskUseCase = GetAuditByTaskIdUseCase(auditRepository,sessionManager,)
        coEvery { sessionManager.isAuthenticated() } returns true

    }
    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows <UnauthorizedException> { getAuditByTaskUseCase.invoke(UUID.randomUUID()) }
        }

    @Test
    fun `Should throw no exception when get audit by task id`() = runTest {
        // Given
        val projectID = UUID.randomUUID()
        // when & then
        getAuditByTaskUseCase.invoke(projectID)

    }
}