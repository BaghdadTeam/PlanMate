package logic.usecase.audit

import com.google.common.truth.Truth.assertThat
import helpers.audit.AuditTestData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.usecase.audit.GetAuditByProjectIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class GetAuditByProjectIdUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var getAuditByProjectIdUseCase: GetAuditByProjectIdUseCase

    @BeforeEach
    fun setup() {
        auditRepository = mockk(relaxed = true)
        getAuditByProjectIdUseCase =
            GetAuditByProjectIdUseCase(auditRepository)
    }

    @Test
    fun `should fetch and combine all audit logs for project`() = runTest {
        // Given
        val audit1 = AuditTestData.createAuditHelper()
        val audit2 = audit1.copy(projectId = audit1.projectId)
        val audit3 = audit1.copy(projectId = audit1.projectId)

        coEvery { auditRepository.getAuditByProjectId(audit1.projectId) } returns listOf(
            audit1,
            audit2,
            audit3
        )

        // when
        val auditLogs = getAuditByProjectIdUseCase.invoke(audit1.projectId)

        // then
        val expectedAuditLogs = listOf(audit3, audit1, audit2)
        assertThat(expectedAuditLogs.size).isEqualTo(auditLogs.size)
        assertThat(expectedAuditLogs).isEqualTo(auditLogs)
    }

    @Test
    fun `should return empty list when there is no audit for project, states or tasks`() = runTest {
        val projectId = UUID.randomUUID()

        coEvery { auditRepository.getAuditByProjectId(any()) } returns emptyList()

        val result = getAuditByProjectIdUseCase.invoke(projectId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return audits sorted by descending timestamp`() = runTest {
        // Given
        val auditOld = AuditTestData.createAuditHelper(timestamp = LocalDateTime.now().minusDays(2))
        val auditMid = AuditTestData.createAuditHelper(
            timestamp = LocalDateTime.now().minusDays(1),
            projectId = auditOld.projectId
        )
        val auditNew = AuditTestData.createAuditHelper(
            projectId = auditOld.projectId
        )

        coEvery { auditRepository.getAuditByProjectId(projectId = auditOld.projectId) } returns listOf(
            auditOld,
            auditMid,
            auditNew
        )

        // when
        val result = getAuditByProjectIdUseCase.invoke(projectId = auditOld.projectId)
        val timestamps = result.map { it.timestamp }

        // then
        assertThat(timestamps).isEqualTo(timestamps.sortedDescending())
    }
}
