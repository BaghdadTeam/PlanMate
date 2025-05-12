package logic.usecase.audit

import com.google.common.base.CharMatcher.any
import io.kotest.assertions.any
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.exceptions.EmptyActionInAuditEntityException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.usecase.audit.AddAuditUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class AddAuditUseCaseTest {
    private lateinit var  auditRepository : AuditRepository
    private lateinit var  addAuditUseCase : AddAuditUseCase
    private val sessionManager: SessionManager = mockk()

    @BeforeEach
    fun setup(){
        auditRepository = mockk(relaxed = true)
        addAuditUseCase = AddAuditUseCase(auditRepository,sessionManager,)
        coEvery { sessionManager.isAuthenticated() } returns true

    }
    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows <UnauthorizedException> { addAuditUseCase.invoke(auditLogEntity) }
    }

    @Test
    fun `should throw no exception when add audit`() = runTest {
        // Given
        // when
        addAuditUseCase.invoke(auditLogEntity)

    }

    @Test
    fun `should throw EmptyActionInAuditEntityException when add audit with empty action`() = runTest {
        // Given
        val randomUUID = UUID.randomUUID()
        val auditLogEntity = AuditLogEntity(
            projectId = randomUUID,
            action = "",
            userId = randomUUID,
            entityUnderAudit = Entities.Task.name,
        )

        // when & Then
        assertThrows<EmptyActionInAuditEntityException> {addAuditUseCase.invoke(auditLogEntity)}

    }

    @Test
    fun `should throw EmptyActionInAuditEntityException when add audit with blank action`() = runTest {
        // Given
        val randomUUID = UUID.randomUUID()
        val auditLogEntity = AuditLogEntity(
            projectId = randomUUID,
            action = " ",
            userId = randomUUID,
            entityUnderAudit = Entities.Task.name,
        )

        // when & Then
        assertThrows<EmptyActionInAuditEntityException> {addAuditUseCase.invoke(auditLogEntity)}

    }
    val randomUUID = UUID.randomUUID()
    val auditLogEntity = AuditLogEntity(
        projectId = randomUUID,
        action = "CREATE",
        userId = randomUUID,
        entityUnderAudit = Entities.Task.name,
    )

}