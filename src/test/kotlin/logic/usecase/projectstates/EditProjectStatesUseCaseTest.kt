package logic.usecase.projectstates

import com.google.common.truth.Truth.assertThat
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.UnauthorizedException
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
    private val sessionManager: SessionManager = mockk()

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        adminPermissionCheckerUseCase = mockk(relaxed = true)
        editStateUseCase = EditProjectStatesUseCase(
            statesRepository,
            auditRepository,
            adminPermissionCheckerUseCase,
            sessionManager
        )
        coEvery { sessionManager.isAuthenticated() } returns true
    }

    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows<UnauthorizedException> {
            editStateUseCase.invoke(
                UUID.randomUUID(),
                ProjectStatesEntityTestData.todoState().name,
                UUID.randomUUID()
            )
        }
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
            editStateUseCase.invoke(state.id, newState.name, userId)
        }

        // then
        assertThat(exception.message).contains("Not authorized")
    }

}
