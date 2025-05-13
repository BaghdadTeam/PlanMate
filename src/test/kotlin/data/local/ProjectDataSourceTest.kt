package data.local

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.local.ProjectDataSource
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.ProjectNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class ProjectDataSourceTest {
    private lateinit var dataSource: DataSource<ProjectEntity>
    private lateinit var projectStatesDataSource: DataSource<TaskStateEntity>
    private lateinit var taskDataSource: DataSource<TaskEntity>
    private lateinit var projectDataSource: ProjectDataSource


    @BeforeEach
    fun setup() {
        dataSource = mockk(relaxed = true)
        projectStatesDataSource = mockk(relaxed = true)
        taskDataSource = mockk(relaxed = true)
        projectDataSource = ProjectDataSource(dataSource, projectStatesDataSource, taskDataSource)
    }

    @Test
    fun `should return data when there is a projects`() = runTest{
        // Given
        val projects = listOf(ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID()))
        coEvery { dataSource.loadAll() } returns projects

        // When
        val result = projectDataSource.getAllProjects()

        // Then
        assert(result == projects)

    }

    @Test
    fun `should throw ProjectNotFoundException when updateProject and there is no projects`() = runTest{
        // Given
        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())
        coEvery { dataSource.loadAll() } returns emptyList()

        // When & Then
        assertThrows<ProjectNotFoundException> { projectDataSource.updateProject(project) }
    }

    @Test
    fun `should throw ProjectNotFoundException when deleteProject and there is no projects`()= runTest {
        // Given
        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())
        coEvery { dataSource.loadAll() } returns emptyList()

        // When & Then
        assertThrows<ProjectNotFoundException> { projectDataSource.deleteProject(project.id) }
    }

    @Test
    fun `createProject should call append on data source`()= runTest {
        // Given
        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())
        coEvery { dataSource.append(project) } just Runs

        // When
        projectDataSource.createProject(project)

        // Then
        coVerify { dataSource.append(project) }
    }

    @Test
    fun `should return project when there is a project with same id`()= runTest {
        // Given
        val projects = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())
        coEvery { dataSource.loadAll() } returns listOf(projects)

        // When
        val result = projectDataSource.getProjectById(projects.id)

        // Then
        assert(result == projects)
    }

    @Test
    fun `should throw ProjectNotFoundException when there is no project with same id`()= runTest {
        // Given
        val projects = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())
        coEvery { dataSource.loadAll() } returns listOf(projects)

        // When & Then
        assertThrows<ProjectNotFoundException> { projectDataSource.getProjectById(UUID.randomUUID()) }
    }

    @Test
    fun `should return updated project when can update it successfully`()= runTest {
        // Given
        val allProjects = listOf(
            ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID()),
            ProjectEntity(name = "Project 2", creatorId = UUID.randomUUID())
        )
        val updatedProject = ProjectEntity(
            name = "Project 3",
            creatorId = UUID.randomUUID()
        ).copy(id = allProjects[1].id)

        coEvery { dataSource.loadAll() } returns allProjects
        coEvery { dataSource.update(updatedProject) } just Runs

        // When
        projectDataSource.updateProject(updatedProject)

        // Then
        coVerify {
            dataSource.update(withArg {
                assertThat(updatedProject.id).isEqualTo(it.id)
                assertThat("Project 3").isEqualTo(it.name)
            })
        }
    }

    @Test
    fun `should delete project with its states and tasks when delete it successfully`() = runTest {
        // Given
        val projectIdToDelete = UUID.randomUUID()

        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID()).copy(id = projectIdToDelete)
        val projectState = TaskStateEntity(name = "State 1", projectId = projectIdToDelete, creatorId = UUID.randomUUID())
        val task = TaskEntity(
            title = "Task 1",
            description = "Description 1",
            stateId = UUID.randomUUID(),
            creatorId = UUID.randomUUID(),
            projectId = projectIdToDelete
        )

        val allProjects = listOf(project)
        val allStates = listOf(projectState)
        val allTasks = listOf(task)

        coEvery { dataSource.loadAll() } returns allProjects
        coEvery { projectStatesDataSource.loadAll() } returns allStates
        coEvery { taskDataSource.loadAll() } returns allTasks

        coEvery { dataSource.delete(project) } just Runs
        coEvery { projectStatesDataSource.delete(projectState) } just Runs
        coEvery { taskDataSource.delete(task) } just Runs

        val projectDataSource = ProjectDataSource(dataSource, projectStatesDataSource, taskDataSource)

        // When
        projectDataSource.deleteProject(projectIdToDelete)

        // Then
        coVerify { dataSource.delete(project) }
        coVerify { projectStatesDataSource.delete(projectState) }
        coVerify { taskDataSource.delete(task) }
    }
}