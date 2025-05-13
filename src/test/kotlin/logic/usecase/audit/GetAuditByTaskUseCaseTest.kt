package logic.usecase.audit

import com.google.common.truth.Truth.assertThat
import helpers.audit.AuditTestData
import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.audit.GetAuditByTaskIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class GetAuditByTaskUseCaseTest {
    private lateinit var  auditRepository : AuditRepository
    private lateinit var  getAuditByTaskIdUseCase: GetAuditByTaskIdUseCase
    private lateinit var  userRepository : UserRepository

    @BeforeEach
    fun setup(){
        auditRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        getAuditByTaskIdUseCase = GetAuditByTaskIdUseCase(auditRepository , userRepository)

    }

    @Test
    fun `Should throw no exception when get audit by task id`() = runTest {
        // Given
        val projectID = UUID.randomUUID()
        // when & then
        getAuditByTaskIdUseCase.invoke(projectID)

    }

    @Test
    fun `should fetch and combine all audit logs for task`() = runTest {
        // Given
        val user = createUserHelper()
        val audit1 = AuditTestData.createAuditHelper()
        val audit2 = audit1.copy(entityUnderAuditId = audit1.entityUnderAuditId)
        val audit3 = audit1.copy(entityUnderAuditId = audit1.entityUnderAuditId)


        coEvery { auditRepository.getAuditByTaskId(audit1.entityUnderAuditId) } returns listOf(
            audit1,
            audit2,
            audit3)
        coEvery { userRepository.getUserById(audit1.userId) } returns user

        // when
        val auditLogs = getAuditByTaskIdUseCase.invoke(audit1.entityUnderAuditId)

        // then
        val expectedAuditLogs = Pair(listOf(audit3, audit1, audit2) , listOf(user,user,user))
        assertThat(expectedAuditLogs.first.size).isEqualTo(auditLogs.first.size)
        assertThat(expectedAuditLogs.second.size).isEqualTo(auditLogs.second.size)
        assertThat(expectedAuditLogs).isEqualTo(auditLogs)
    }
}