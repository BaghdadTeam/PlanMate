package data.repository.audit

import data.local.AuditDataSource
import io.mockk.mockk
import org.baghdad.data.repository.audit.AuditRepositoryImpl
import org.junit.jupiter.api.BeforeEach
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
    fun `should throw no exception when add audit entry`() {
        auditRepository.addAuditEntry(mockk())
    }

    @Test
    fun `should throw no exception when get audit by task id`() {
        auditRepository.getAuditByTaskId(mockk())
    }

    @Test
    fun `should throw no exception when get audit by project id`() {
        auditRepository.getAuditByProjectId(mockk())
    }

}