package data.repositories.audit

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.data.local.AuditDataSource
import org.baghdad.data.repositories.audit.AuditRepositoryImpl
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.Entities
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test

class AuditRepositoryImplTest {
    private lateinit var auditDataSource: AuditDataSource
    private lateinit var auditRepository: AuditRepositoryImpl

    @BeforeEach
    fun setup() {
        auditDataSource = mockk(relaxed = true)
        auditRepository = AuditRepositoryImpl(auditDataSource)
    }

    @Test
    fun `should throw no exception when add audit entry`() = runTest {
        // Given
        val auditLogEntity = AuditLogEntity(
            id = UUID.randomUUID(),
            projectId = UUID.randomUUID(),
            action = "CREATE",
            user = mockk(),
            entityUnderAudit = Entities.Task.name,
            timestamp = java.time.LocalDateTime.now()
        )

        // When
        auditRepository.addAuditEntry(auditLogEntity)

        // Then
        coVerify { auditDataSource.createAudit(any()) }
    }

    @Test
    fun `should throw no exception when get audit by task id`() = runTest{
        auditRepository.getAuditByTaskId(mockk())
    }

    @Test
    fun `should throw no exception when get audit by project id`() = runTest{
        auditRepository.getAuditByProjectId(mockk())
    }
}