package data.repositories.projectstate

import com.google.common.truth.Truth.assertThat
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.baghdad.data.local.ProjectStatesDataSource
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
        val projectStates = ProjectStatesEntityTestData.todoState()
        val id = projectStates.id
        coEvery { dataSource.getStateById(id) } returns projectStates

        // When
        val result = projectStatesDataSource.getStateById(id)

        // Then
        assertThat(result).isEqualTo(projectStates)
        coVerify { dataSource.getStateById(id) }
    }


    @Test
    fun `should return state when can add state successfully`() = runTest {
        val state = ProjectStatesEntityTestData.inProgressState()

        projectStatesDataSource.createState(state)

        coVerify { dataSource.createState(state) }
    }

    @Test
    fun `should call editState with updated data`() = runTest {
        // Given
        val state = ProjectStatesEntityTestData.inProgressState()
        val updatedState = state.copy(name = "Doing")
        coEvery { dataSource.editState(updatedState) } just Runs

        // When
        projectStatesDataSource.editState(state.id, updatedState)

        // Then
        coVerify(exactly = 1) { dataSource.editState(updatedState) }
    }

    @Test
    fun `should delete state when can delete it successfully`() = runTest {
        val state = ProjectStatesEntityTestData.inProgressState()

        coEvery { projectStatesDataSource.createState(state)} just Runs
        coEvery { projectStatesDataSource.deleteState(state.id) } just Runs

        val result = projectStatesDataSource.getAllStatesPerProject(state.projectId)

        val finalResult = result.find { it.id == state.id }
        assertThat(finalResult).isNull()
    }

}