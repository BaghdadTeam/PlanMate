package data.repositories.projectstate

import com.google.common.truth.Truth.assertThat
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.baghdad.data.local.ProjectStatesDataSource
import org.baghdad.data.repositories.toDomain
import org.baghdad.data.repositories.toDto
import org.baghdad.data.repositories.projectstates.ProjectStatesRepositoryImp
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ProjectStatesRepositoryImpTest {

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
    fun `should call editState with updated data`() = runTest {
        // Given
        val state = ProjectStatesEntityTestData.inProgressState()
        val updatedState = state.copy(name = "Doing")
        coEvery { dataSource.editState(updatedState.toDto().toDomain()) } just Runs

        // When

        projectStatesDataSource.editState(updatedState.toDto().toDomain())

        // Then
        coVerify(exactly = 1) { dataSource.editState(any()) }
    }

}
