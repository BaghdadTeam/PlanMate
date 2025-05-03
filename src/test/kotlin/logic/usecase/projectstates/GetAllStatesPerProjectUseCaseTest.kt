package logic.usecase.projectstates

import com.google.common.truth.Truth
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.projectstates.GetAllStatesPerProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllStatesPerProjectUseCaseTest {

    private lateinit var statesRepository: ProjectStatesRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var getStatesUseCase: GetAllStatesPerProjectUseCase
    private lateinit var userRepository: UserRepository


    @BeforeEach
    fun setup() {
        statesRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        getStatesUseCase = GetAllStatesPerProjectUseCase(statesRepository)
    }

    @Test
    fun `should return list of sates when there is a states for project`() {
        // Given
        val projectStates = ProjectStatesEntityTestData.getAllStatesPerProject()
        every { statesRepository.getAllStatesPerProject("123") } returns projectStates
        // When
        val result = getStatesUseCase.invoke("123")
        // Then
        Truth.assertThat(result).isEqualTo(projectStates)
    }

    @Test
    fun `should return empty list of sates when there is a states for project`() {
        // Given
        val projectStates = ProjectStatesEntityTestData.getAllStatesPerProject()
        every { statesRepository.getAllStatesPerProject("123") } returns projectStates
        // When
        val result = getStatesUseCase.invoke("154")
        // Then
        Truth.assertThat(result).isEmpty()
        verify { statesRepository.getAllStatesPerProject("154") }
    }


}