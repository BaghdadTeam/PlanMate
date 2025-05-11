package data.local

import com.google.common.truth.Truth.assertThat
import helpers.projectStates.ProjectStatesEntityTestData
import helpers.task.TaskTestData
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.local.ProjectStatesDataSource
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.StateNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

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
    fun `should return data when there are states for the project`() = runTest {
        // Given
        val projectStates = ProjectStatesEntityTestData.getAllStatesPerProject()
        val id = projectStates.first().projectId
        coEvery { dataSource.loadAll() } returns projectStates

        // When
        val result = projectStatesDataSource.getAllStatesForProject(id)

        // Then
        assertThat(result).isEqualTo(projectStates.filter { it.projectId == id })
    }

    @Test
    fun `should throw exception when there are no states for the project`() = runTest {
        // Given
        val id = UUID.randomUUID()
        val projectStates = ProjectStatesEntityTestData.getAllStatesPerProject()
        coEvery { dataSource.loadAll() } returns projectStates // no states match the random ID

        // When & Then
        assertThrows<StateNotFoundException> {
            projectStatesDataSource.getAllStatesForProject(id)
        }
    }

    @Test
    fun `should return state when there is a state with same id`() = runTest {
        // Given
        val projectStates = ProjectStatesEntityTestData.todoState()
        coEvery { dataSource.loadAll() } returns listOf(projectStates)
        // When
        val result = projectStatesDataSource.getStateById(projectStates.id)
        // Then
        assertThat(result).isEqualTo(projectStates)
    }

    @Test
    fun `should return state when can add state successfully`() = runTest {
        // Given
        val projectState = ProjectStatesEntityTestData.inProgressState()
        coEvery { dataSource.loadAll() } returns listOf(projectState)
        coEvery { projectStatesDataSource.createState(projectState) } just Runs
        // When
        val result = projectStatesDataSource.getStateById(projectState.id)

        // Then
        assertThat(result).isEqualTo(projectState)
    }


    @Test
    fun `should return updated state when can update it successfully`() = runTest {
        // Given
        val allStates = ProjectStatesEntityTestData.getAllStatesPerProject().toMutableList()
        val id = allStates.first().projectId
        val updatedProject = ProjectStatesEntityTestData.inProgressState().copy(id = allStates[1].id, name = "doing")

        coEvery { dataSource.loadAll() } returns allStates
        coEvery { dataSource.update(updatedProject) } just Runs

        // When
        projectStatesDataSource.editState(updatedProject)
        val result = projectStatesDataSource.getAllStatesForProject(id)

        // Then
        assertThat(result.contains(updatedProject))
    }


    @Test
    fun `should trow exception when no state found while trying to update`() = runTest {

        // Given
        val allStates = ProjectStatesEntityTestData.getAllStatesPerProject().toMutableList()
        val updatedProject = ProjectStatesEntityTestData.inProgressState().copy(name = "doing")

        coEvery { dataSource.loadAll() } returns allStates
        coEvery { dataSource.update(updatedProject) } just Runs

        assertThrows<Exception> {
            projectStatesDataSource.editState(updatedProject)
        }
    }

    @Test
    fun `should delete state and associated tasks when state exists`() = runTest {
        // Given
        val stateToDelete = ProjectStatesEntityTestData.todoState()
        val otherState   = ProjectStatesEntityTestData.inProgressState()

        val task1 = TaskTestData.taskWithState(stateId = stateToDelete.id)
        val task2 = TaskTestData.taskWithState(stateId = otherState.id)

        coEvery { dataSource.loadAll() } returns listOf(stateToDelete, otherState)
        coEvery { taskDataSource.loadAll() } returns listOf(task1, task2)

        coEvery { dataSource.delete(stateToDelete) } just Runs
        coEvery { taskDataSource.delete(task1) } just Runs

        // When
        projectStatesDataSource.deleteState(stateToDelete.id)

        // Then
        coVerify   { dataSource.delete(stateToDelete) }
        coVerify   { taskDataSource.delete(task1) }
        coVerify(exactly = 0) { taskDataSource.delete(task2) }
    }
    @Test
    fun `should throw exception when deleting non-existent state`() {
        // Given
        val existingState = ProjectStatesEntityTestData.todoState()
        val nonexistentStateId = UUID.randomUUID()

        coEvery { dataSource.loadAll() } returns listOf(existingState)

        // Then
        val exception = assertThrows<Exception> {
            runTest {
                projectStatesDataSource.deleteState(nonexistentStateId)
            }
        }
        assertThat(exception.message).isEqualTo("No state found")
    }
}