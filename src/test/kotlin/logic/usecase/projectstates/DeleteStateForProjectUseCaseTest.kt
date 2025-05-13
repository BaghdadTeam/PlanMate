package logic.usecase.projectstates

import com.google.common.truth.Truth
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import org.baghdad.logic.usecase.projectstates.DeleteStateForProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.UUID

class DeleteStateForProjectUseCaseTest {

    private lateinit var statesRepository: ProjectStatesRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var deleteStateUseCase: DeleteStateForProjectUseCase
    private lateinit var adminPermissionCheckerUseCase: AdminPermissionCheckerUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        adminPermissionCheckerUseCase = mockk(relaxed = true)
        deleteStateUseCase =
            DeleteStateForProjectUseCase(
                statesRepository,
                auditRepository,
                adminPermissionCheckerUseCase
            )
    }

    @Test
    fun `should delete state and add audit when adminPermissionCheckerUseCase return true and state is valid`() =
        runTest {
            // given
            val state = ProjectStatesEntityTestData.todoState()
            val id = state.id
            val userId = UUID.randomUUID()

            coEvery { adminPermissionCheckerUseCase(userId) } returns true

            // when
            deleteStateUseCase.invoke(id, userId)

            // then
            coVerify { statesRepository.deleteState(id) }

            val auditSlot = slot<AuditLogEntity>()
            coVerify { auditRepository.addAuditEntry(capture(auditSlot)) }

            val audit = auditSlot.captured
            Truth.assertThat(audit.projectId).isInstanceOf(UUID::class.java)
            Truth.assertThat(audit.timestamp).isInstanceOf(LocalDateTime::class.java)
        }


    @Test
    fun `should throw AccessDeniedException when adminPermissionCheckerUseCase return false`() =
        runTest {
            // Given
            val userId = UUID.randomUUID()
            val stateId = UUID.randomUUID()
            coEvery { adminPermissionCheckerUseCase(userId) } returns false

            // When
            val exception = assertThrows<AccessDeniedException> {
                deleteStateUseCase.invoke(stateId, userId)
            }

            // Then
            Truth.assertThat(exception.message).contains("Not authorized")
        }
}