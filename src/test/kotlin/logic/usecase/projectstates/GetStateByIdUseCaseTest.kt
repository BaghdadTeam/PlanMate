package logic.usecase.projectstates

import com.google.common.truth.Truth.assertThat
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.exceptions.StateNotFoundException
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.usecase.projectstates.GetStateByIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetStateByIdUseCaseTest {

    private lateinit var statesRepository: ProjectStatesRepository
    private lateinit var getStateByIdUseCase: GetStateByIdUseCase

    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        getStateByIdUseCase = GetStateByIdUseCase(statesRepository)
    }

    @Test
    fun `should return state when there is a state by this id`() = runTest {
        val projectState = ProjectStatesEntityTestData.todoState()
        val id = projectState.id

        coEvery { statesRepository.getStateById(id) } returns projectState

        val result = getStateByIdUseCase.invoke(id)

        assertThat(result).isEqualTo(projectState)
        coVerify { statesRepository.getStateById(id) }
    }

    @Test
    fun `should throw StateNotFoundException when there is no state with this id`() = runTest {
        val id = UUID.randomUUID()
        coEvery { statesRepository.getStateById(id) } throws StateNotFoundException("No state found")

        val exception = assertThrows<StateNotFoundException> {
            getStateByIdUseCase.invoke(id)
        }

        assertThat(exception.message).contains("No state found")
    }

    @Test
    fun `should throw StateNotFoundException when state id matches but state is missing`() = runTest {
        val missingStateId = UUID.randomUUID()
        coEvery { statesRepository.getStateById(missingStateId) } throws StateNotFoundException("No state found")

        val exception = assertThrows<StateNotFoundException> {
            getStateByIdUseCase.invoke(missingStateId)
        }

        assertThat(exception.message).contains("No state found")
    }
}