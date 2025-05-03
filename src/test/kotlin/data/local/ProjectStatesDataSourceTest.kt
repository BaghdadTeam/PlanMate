package data.local

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.local.ProjectStatesDataSource
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProjectStatesDataSourceTest {

    private lateinit var dataSource: DataSource<StateEntity>
    private lateinit var taskDataSource: DataSource<TaskEntity>
    private lateinit var projectStatesDataSource: ProjectStatesDataSource

    @BeforeEach
    fun setup() {
        dataSource = mockk(relaxed = true)
        taskDataSource = mockk(relaxed = true)
        projectStatesDataSource = ProjectStatesDataSource(
            dataSource,
            taskDataSource = taskDataSource
        )
    }

    @Test
    fun `should return data when there is a states for project`() {
        // Given
        val projectStates = ProjectStatesEntityTestData.getAllStatesPerProject()
        every { dataSource.loadAll() } returns projectStates
        // When
        val result = projectStatesDataSource.getAllStatesForProject()
        // Then
        Truth.assertThat(result).isEqualTo(projectStates)
    }


    @Test
    fun `should return state when there is a state with same id`() {
        // Given
        val projectStates = ProjectStatesEntityTestData.todoState()
        every { dataSource.loadAll() } returns listOf(projectStates)
        // When
        val result = projectStatesDataSource.getStateById(projectStates.id.toString())
        // Then
        assertThat(result).isEqualTo(projectStates)
    }

    @Test
    fun `should return state when can add state successfully`() {
        // Given
        val projectStates = ProjectStatesEntityTestData.inProgressState()
        every { dataSource.loadAll() } returns listOf(projectStates)
        // When
        projectStatesDataSource.createState(projectStates)
        val result = projectStatesDataSource.getStateById(projectStates.id.toString())

        // Then
        assertThat(result).isEqualTo(projectStates)
    }


    @Test
    fun `should return updated state when can update it successfully`() {
        // Given
        val allStates = ProjectStatesEntityTestData.getAllStatesPerProject().toMutableList()
        val updatedProject = ProjectStatesEntityTestData.inProgressState().copy(id = allStates[1].id, name = "doing")

        every { dataSource.loadAll() } returns allStates
        every { dataSource.update(listOf(updatedProject)) } just Runs

        // When
        projectStatesDataSource.editState(updatedProject)
        val result = projectStatesDataSource.getAllStatesForProject()

        // Then
        assertThat(result.contains(updatedProject))
    }


    @Test
    fun `should trow exception when no state found while trying to update`() {

        // Given
        val allStates = ProjectStatesEntityTestData.getAllStatesPerProject().toMutableList()
        val updatedProject = ProjectStatesEntityTestData.inProgressState().copy(name = "doing")

        every { dataSource.loadAll() } returns allStates
        every { dataSource.update(listOf(updatedProject)) } just Runs

        val exception = assertThrows<Exception> {
            projectStatesDataSource.editState(updatedProject)
        }
        assertThat(exception.message).contains("No state found")

    }


    @Test
    fun `should delete state when found it`() {

        // Given
        val allStates = ProjectStatesEntityTestData.getAllStatesPerProject().toMutableList()
        val stateAfterDelete = ProjectStatesEntityTestData.getStatesAfterDelete()
        allStates.last().id
        val deleteState = ProjectStatesEntityTestData.doingState()

        every { dataSource.loadAll() } returns allStates
        every { dataSource.update(stateAfterDelete) } just Runs

        // When
        projectStatesDataSource.deleteState(allStates.last().id.toString())
        val result = projectStatesDataSource.getAllStatesForProject()//.getStateById(deleteState.id.toString())

        // Then
        assertThat(result.contains(deleteState)).isFalse()
    }

    @Test
    fun `should throw exception when trying ti delete not exist state`() {

        // Given
        val allStates = ProjectStatesEntityTestData.getAllStatesPerProject().toMutableList()
        val stateAfterDelete = ProjectStatesEntityTestData.getStatesAfterDelete()
        val deleteState = ProjectStatesEntityTestData.doingState()

        every { dataSource.loadAll() } returns allStates
        every { dataSource.update(stateAfterDelete) } just Runs

        // Then
        val exception = assertThrows<Exception> {
            projectStatesDataSource.deleteState(deleteState.id.toString())
        }
        assertThat(exception.message).contains("No state found")

    }

}