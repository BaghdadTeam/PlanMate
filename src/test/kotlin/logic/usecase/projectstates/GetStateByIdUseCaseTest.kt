package logic.usecase.projectstates

import com.google.common.truth.Truth
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.projectstate.GetStateByIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    fun `should return sate when there is a state by this id`() {

        val projectState = ProjectStatesEntityTestData.todoState()
        val id = projectState.id

        every { statesRepository.getStateById(id.toString()) } returns projectState

        val result = getStateByIdUseCase.invoke(id.toString())

        Truth.assertThat(result).isEqualTo(projectState)
        verify { statesRepository.getStateById(id.toString()) }
    }

    @Test
    fun `should throw exception when there is no states with this id`() {
        val id = UUID.randomUUID()
        every { statesRepository.getStateById(id.toString()) } throws Exception("No State found")

        org.junit.jupiter.api.assertThrows<Exception> {
            getStateByIdUseCase.invoke(id.toString())
        }
        verify { statesRepository.getStateById(id.toString()) }

    }

}