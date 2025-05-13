package logic.usecase.projectstates

import com.google.common.truth.Truth
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.projectstates.GetAllStatesPerProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class GetAllStatesPerProjectUseCaseTest {

    private lateinit var statesRepository: ProjectStatesRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var getStatesUseCase: GetAllStatesPerProjectUseCase
    private lateinit var userRepository: UserRepository
    private val sessionManager: SessionManager = mockk()


    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        getStatesUseCase = GetAllStatesPerProjectUseCase(statesRepository,sessionManager)
        coEvery { sessionManager.isAuthenticated() } returns true
    }
    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows <UnauthorizedException> {
            getStatesUseCase.invoke(UUID.randomUUID())
        }
    }


    @Test
    fun `should return list of sates when there is a states for project`() = runTest {
        // Given
        val projectStates = ProjectStatesEntityTestData.getAllStatesPerProject()
        val projectId = projectStates[0].projectId
        coEvery { statesRepository.getAllStatesPerProject(projectId) } returns projectStates

        // When
        val result = getStatesUseCase.invoke(projectId)

        // Then
        Truth.assertThat(result).isEqualTo(projectStates)
    }

    @Test
    fun `should return empty list of states when there is no states for project`() = runTest {
        // Given
        val projectStates = ProjectStatesEntityTestData.getAllStatesPerProject()
        val stateId = projectStates[0].projectId
        coEvery { statesRepository.getAllStatesPerProject(stateId) } returns emptyList()

        // When
        val result = getStatesUseCase.invoke(stateId)

        // Then
        Truth.assertThat(result).isEmpty()
        coVerify { statesRepository.getAllStatesPerProject(stateId) }
    }


}