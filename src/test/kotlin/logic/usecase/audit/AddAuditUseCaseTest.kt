package logic.usecase.audit

import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.exceptions.EmptyActionInAuditEntityException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.usecase.audit.AddAuditUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class AddAuditUseCaseTest {
    lateinit var  auditRepository : AuditRepository
    lateinit var  addAuditUseCase : AddAuditUseCase

    @BeforeEach
    fun setup(){
        auditRepository = mockk(relaxed = true)
        addAuditUseCase = AddAuditUseCase(auditRepository)

    }

    @Test
    fun `should throw no exception when add audit`() = runTest {
        // Given
        val randomUUID = UUID.randomUUID()
        val auditLogEntity = AuditLogEntity(
            projectId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityUnderAudit = Entities.Task.name,
        )

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
            user = mockk(),
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
            user = mockk(),
            entityUnderAudit = Entities.Task.name,
        )

        // when & Then
        assertThrows<EmptyActionInAuditEntityException> {addAuditUseCase.invoke(auditLogEntity)}

    }
}