package data.repository.audit

import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.data.local.AuditDataSource
import org.baghdad.data.repositories.audit.AuditRepositoryImpl
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
    fun `should throw no exception when add audit entry`() = runTest{
        auditRepository.addAuditEntry(mockk())
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