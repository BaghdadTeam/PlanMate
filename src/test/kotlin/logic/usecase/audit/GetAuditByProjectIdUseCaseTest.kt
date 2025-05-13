package logic.usecase.audit

import com.google.common.truth.Truth.assertThat
import helpers.audit.AuditTestData
import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.mockk
import jdk.internal.net.http.common.Pair.pair
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.enums.UserType
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.audit.GetAuditByProjectIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class GetAuditByProjectIdUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var userRepository : UserRepository
    private lateinit var getAuditByProjectIdUseCase: GetAuditByProjectIdUseCase

    @BeforeEach
    fun setup() {
        auditRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        getAuditByProjectIdUseCase =
            GetAuditByProjectIdUseCase(auditRepository , userRepository)
    }

    @Test
    fun `should fetch and combine all audit logs for project`() = runTest {
        // Given
        val user = createUserHelper()
        val audit1 = AuditTestData.createAuditHelper()
        val audit2 = audit1.copy(projectId = audit1.projectId)
        val audit3 = audit1.copy(projectId = audit1.projectId)


        coEvery { auditRepository.getAuditByProjectId(audit1.projectId) } returns listOf(
            audit1,
            audit2,
            audit3)
        coEvery { userRepository.getUserById(audit1.userId) } returns user

        // when
        val auditLogs = getAuditByProjectIdUseCase.invoke(audit1.projectId)

        // then
        val expectedAuditLogs = Pair(listOf(audit3, audit1, audit2) , listOf(user,user,user))
        assertThat(expectedAuditLogs.first.size).isEqualTo(auditLogs.first.size)
        assertThat(expectedAuditLogs.second.size).isEqualTo(auditLogs.second.size)
        assertThat(expectedAuditLogs).isEqualTo(auditLogs)
    }

    @Test
    fun `should return empty list when there is no audit for project, states or tasks`() = runTest {
        val projectId = UUID.randomUUID()

        coEvery { auditRepository.getAuditByProjectId(any()) } returns emptyList()

        val result = getAuditByProjectIdUseCase.invoke(projectId)

        assertThat(result.first).isEmpty()
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
        val timestamps = result.first.map { it.timestamp }

        // then
        assertThat(timestamps).isEqualTo(timestamps.sortedDescending())
    }
}
