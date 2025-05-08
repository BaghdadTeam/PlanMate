package logic.usecase.projectstates

import com.google.common.truth.Truth
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.projectstates.GetStateByIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetStateByIdUseCaseTest {

    private lateinit var statesRepository: ProjectStatesRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var getStateByIdUseCase: GetStateByIdUseCase
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        getStateByIdUseCase = GetStateByIdUseCase(statesRepository)
    }

    @Test
    fun `should return sate when there is a state by this id`() = runTest {

        val projectState = ProjectStatesEntityTestData.todoState()
        val id = projectState.id

        coEvery { statesRepository.getStateById(id) } returns projectState

        val result = getStateByIdUseCase.invoke(id)

        Truth.assertThat(result).isEqualTo(projectState)
        coVerify { statesRepository.getStateById(id) }
    }

    @Test
    fun `should throw exception when there is no states with this id`() = runTest {
        val id = UUID.randomUUID()
        coEvery { statesRepository.getStateById(id) } throws Exception("No State found")

        assertThrows<Exception> {
            getStateByIdUseCase.invoke(id)
        }
        coVerify { statesRepository.getStateById(id) }

    }

    @Test
    fun `should throw exception when there is no states for project`() = runTest {
        // Given
        val projectStates = ProjectStatesEntityTestData.getAllStatesPerProject()
        val stateId = projectStates[0].projectId
        coEvery { statesRepository.getStateById(stateId) } returns null

        // when
        val exception = assertThrows<Exception> {
            getStateByIdUseCase.invoke(stateId)
        }
        // then
        Truth.assertThat(exception.message).contains("No state found")
    }

}