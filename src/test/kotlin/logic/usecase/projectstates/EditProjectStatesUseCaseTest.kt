package logic.usecase.projectstates

import com.google.common.truth.Truth
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.NotAccessException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.projectstates.EditProjectStatesUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class EditProjectStatesUseCaseTest {

    private lateinit var statesRepository: ProjectStatesRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var editStateUseCase: EditProjectStatesUseCase
    private lateinit var userRepository: UserRepository
    private val sessionManager: SessionManager = mockk()

    private val adminUser = UserEntity(
        name = "Narges Nagy",
        username = "nargesnagy",
        type = UserType.Admin
    )

    private val mateUser = UserEntity(
        name = "Narges Nagy",
        username = "narges21",
        type = UserType.Mate
    )

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        editStateUseCase = EditProjectStatesUseCase(statesRepository, auditRepository, userRepository,sessionManager)
        coEvery { sessionManager.isAuthenticated() } returns true
    }

    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows <UnauthorizedException> {
            editStateUseCase.invoke(
                UUID.randomUUID(),
                ProjectStatesEntityTestData.todoState().name,
                UUID.randomUUID()
            )
        }}

    @Test
    fun `should edit state and add audit when user is admin`() = runTest {
        // Given
        val state = ProjectStatesEntityTestData.todoState()
        val newName = "To Do Updated"
        val newState = state.copy(name = newName)

        coEvery { userRepository.getUserById(adminUser.id) } returns adminUser
        coEvery { statesRepository.getStateById(state.id) } returns state

        // When
        editStateUseCase.invoke(state.id, newName, adminUser.id)

        // Then
        coVerify { statesRepository.editState(newState) }

        // Verifying that the auditRepository.addAuditEntry was called with the correct parameters
        coVerify {
            auditRepository.addAuditEntry(
                match { audit ->
                    audit.userId == adminUser.id &&
                            audit.description.contains("state has been updated successfully")
                })
        }
    }


    @Test
    fun `should throw exception when user type is mate`() = runTest {
        // given
        val state = ProjectStatesEntityTestData.todoState()
        val newState = state.copy(name = "Done")
        coEvery { userRepository.getUserById(mateUser.id) } returns mateUser

        // when
        val exception = assertThrows<NotAccessException> {
            editStateUseCase.invoke(state.id, newState.name, mateUser.id)
        }
        // then
        Truth.assertThat(exception.message).contains("Only Admin can edit states")
    }

}
