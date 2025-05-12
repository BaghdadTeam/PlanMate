package data.repositories.projectstate

import com.google.common.truth.Truth.assertThat
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.baghdad.data.local.ProjectStatesDataSource
import org.baghdad.data.mapper.toDomain
import org.baghdad.data.mapper.toDto
import org.baghdad.data.repositories.projectstates.ProjectStatesRepositoryImp
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ProjectStatesRepositoryImpTest{

    private lateinit var dataSource: ProjectStatesDataSource
    private lateinit var projectStatesDataSource: ProjectStatesRepositoryImp

    @BeforeEach
    fun setup() {
        dataSource = mockk(relaxed = true)
        projectStatesDataSource = ProjectStatesRepositoryImp(dataSource)
    }


    @Test
    fun `should return data when there are states for the project`() = runTest {
        // Given
        val projectStates = ProjectStatesEntityTestData.getAllStatesPerProject()
        val projectId = projectStates.first().projectId
        coEvery { dataSource.getAllStatesForProject(projectId) } returns projectStates
        // When
        val result = projectStatesDataSource.getAllStatesPerProject(projectId)

        // Then
        assertThat(result).isEqualTo(projectStates)
    }

    @Test
    fun `should return empty list when there are no states for the project`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        coEvery { dataSource.getAllStatesForProject(projectId) } returns emptyList()

        // When
        val result = projectStatesDataSource.getAllStatesPerProject(projectId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return state when there is a state with same id`() = runTest {
        val stateEntity = ProjectStatesEntityTestData.todoState()
        val id = stateEntity.id
        coEvery { dataSource.getStateById(id) } returns stateEntity

        // When
        val result = projectStatesDataSource.getStateById(id)

        // Then
        assertThat(result.id).isEqualTo(stateEntity.id)
        assertThat(result.name).isEqualTo(stateEntity.name)

    }


    @Test
    fun `should return state when can add state successfully`() = runTest {
        val stateEntity = ProjectStatesEntityTestData.inProgressState()
        val domainModel = stateEntity.toDto()

        coEvery { dataSource.createState(domainModel.toDomain()) } just Runs

        projectStatesDataSource.createState(domainModel.toDomain())

        coVerify { dataSource.createState(domainModel.toDomain()) }
    }

    @Test
    fun `should delete state when can delete it successfully`() = runTest {
        // Given
        val state = ProjectStatesEntityTestData.inProgressState()
        val projectId = state.projectId

        coEvery { dataSource.deleteState(state.id) } just Runs
        coEvery { dataSource.getAllStatesForProject(projectId) } returns
                ProjectStatesEntityTestData.getStatesAfterDelete()

        // When
        projectStatesDataSource.deleteState(state.id)
        val result = projectStatesDataSource.getAllStatesPerProject(projectId)

        // Then
        assertThat(result.find { it.id == state.id }).isNull()
    }

    @Test
    fun `should call editState with updated data`() = runTest {
        // Given
        val state = ProjectStatesEntityTestData.inProgressState()
        val updatedState = state.copy(name = "Doing")
        coEvery { dataSource.editState(updatedState.toDto().toDomain()) } just Runs

        // When
        projectStatesDataSource.editState(state.id, updatedState.toDto().toDomain())

        // Then
        coVerify(exactly = 1) { dataSource.editState(any()) }
    }

}
