package logic.usecase.audit

import io.mockk.mockk
import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.exceptions.audit.EmptyActionInAuditEntityException
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
    fun `should throw no exception when add audit`(){
        // Given
        val randomUUID = UUID.randomUUID()
        val auditEntity = AuditEntity(
            entityId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityType = Entities.Task,
        )

        // when
        addAuditUseCase.invoke(auditEntity)

    }

    @Test
    fun `should throw EmptyActionInAuditEntityException when add audit with empty action`(){
        // Given
        val randomUUID = UUID.randomUUID()
        val auditEntity = AuditEntity(
            entityId = randomUUID,
            action = "",
            user = mockk(),
            entityType = Entities.Task,
        )

        // when & Then
        assertThrows<EmptyActionInAuditEntityException> {addAuditUseCase.invoke(auditEntity)}

    }

    @Test
    fun `should throw EmptyActionInAuditEntityException when add audit with blank action`(){
        // Given
        val randomUUID = UUID.randomUUID()
        val auditEntity = AuditEntity(
            entityId = randomUUID,
            action = " ",
            user = mockk(),
            entityType = Entities.Task,
        )

        // when & Then
        assertThrows<EmptyActionInAuditEntityException> {addAuditUseCase.invoke(auditEntity)}

    }
}