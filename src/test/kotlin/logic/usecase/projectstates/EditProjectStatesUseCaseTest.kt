package logic.usecase.projectstates

import com.google.common.truth.Truth.assertThat
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
import org.baghdad.logic.usecase.projectstates.EditProjectStatesUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class EditProjectStatesUseCaseTest {

    private lateinit var statesRepository: ProjectStatesRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var editStateUseCase: EditProjectStatesUseCase
    private lateinit var adminPermissionCheckerUseCase: AdminPermissionCheckerUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        adminPermissionCheckerUseCase = mockk(relaxed = true)
        editStateUseCase = EditProjectStatesUseCase(
            statesRepository,
            auditRepository,
            adminPermissionCheckerUseCase
        )
    }

    @Test
    fun `should edit state and add audit when user is admin`() = runTest {
        // Given
        val state = ProjectStatesEntityTestData.todoState()
        val newName = "To Do Updated"
        val newState = state.copy(name = newName)
        val userId = UUID.randomUUID()

        coEvery { adminPermissionCheckerUseCase(userId) } returns true

        // When
        editStateUseCase.invoke(state.id, newState, userId)

        // Then
        coVerify { statesRepository.editState(state.id, newState) }

        val auditSlot = slot<AuditLogEntity>()
        coVerify { auditRepository.addAuditEntry(capture(auditSlot)) }

        val audit = auditSlot.captured
        assertThat(audit.userId).isEqualTo(userId)
        assertThat(audit.description).contains("state is updated successfully")
    }

    @Test
    fun `should throw exception when user type is mate`() = runTest {
        // given
        val state = ProjectStatesEntityTestData.todoState()
        val newState = state.copy(name = "Done")
        val userId = UUID.randomUUID()

        coEvery { adminPermissionCheckerUseCase(userId) } returns false

        // when
        val exception = assertThrows<AccessDeniedException> {
            editStateUseCase.invoke(state.id, newState, userId)
        }
        // then
        assertThat(exception.message).contains("Not authorized")
    }

}
