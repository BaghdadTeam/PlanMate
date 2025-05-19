package data.local

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.dto.project.ProjectDto
import org.baghdad.data.local.ProjectDataSource
import org.baghdad.data.repositories.toDto
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.ProjectNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class ProjectDataSourceTest {
    private lateinit var dataSource: DataSource<ProjectDto>
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
    fun `should return data when there is a projects`() = runTest {
        // Given
        val projectDto = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID()).toDto()
        coEvery { dataSource.loadAll() } returns listOf(projectDto)

        // When
        val result = projectDataSource.getAllProjects()

        // Then
        assertThat(result).containsExactly(projectDto)
    }

    @Test
    fun `should throw ProjectNotFoundException when updateProject and there is no projects`() =
        runTest {
            // Given
            val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID()).toDto()
            coEvery { dataSource.loadAll() } returns emptyList()

            // When & Then
            assertThrows<ProjectNotFoundException> {
                projectDataSource.updateProject(project)
            }
        }

    @Test
    fun `should throw ProjectNotFoundException when deleteProject and there is no projects`() =
        runTest {
            // Given
            val id = UUID.randomUUID()
            coEvery { dataSource.loadAll() } returns emptyList()

            // When & Then
            assertThrows<ProjectNotFoundException> {
                projectDataSource.deleteProject(id)
            }
        }

    @Test
    fun `createProject should call append on data source`() = runTest {
        // Given
        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID()).toDto()

        coEvery { dataSource.append(project) } just Runs

        // When
        projectDataSource.createProject(project)

        // Then
        coVerify { dataSource.append(project) }
    }

    @Test
    fun `should return project when there is a project with same id`() = runTest {
        // Given
        val projectDto = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID()).toDto()
        coEvery { dataSource.loadAll() } returns listOf(projectDto)

        // When
        val result = projectDataSource.getProjectById(projectDto.id)

        // Then
        assertThat(result).isEqualTo(projectDto)
    }

    @Test
    fun `should throw ProjectNotFoundException when there is no project with same id`() = runTest {
        // Given
        val entity = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())
        coEvery { dataSource.loadAll() } returns listOf(entity.toDto())

        // When & Then
        assertThrows<ProjectNotFoundException> {
            projectDataSource.getProjectById(UUID.randomUUID())
        }
    }

    @Test
    fun `should return updated project when can update it successfully`() = runTest {
        // Given
        val existing = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID()).toDto()
        val updated = existing.copy(name = "Project 3")
        coEvery { dataSource.loadAll() } returns listOf(existing)
        coEvery { dataSource.update(updated) } just Runs

        // When
        projectDataSource.updateProject(updated)

        // Then
        coVerify {
            dataSource.update(withArg {
                assertThat(it.id).isEqualTo(updated.id)
                assertThat(it.name).isEqualTo("Project 3")
            })
        }
    }

    @Test
    fun `should delete project with its states and tasks when delete it successfully`() = runTest {
        // Given
        val projectIdToDelete = UUID.randomUUID()
        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID()).copy(id = projectIdToDelete)
        val state = TaskStateEntity(name = "State 1", projectId = projectIdToDelete, creatorId = UUID.randomUUID())
        val task = TaskEntity(
            title = "Task 1",
            description = "Description 1",
            stateId = UUID.randomUUID(),
            creatorId = UUID.randomUUID(),
            projectId = projectIdToDelete
        )

        coEvery { dataSource.loadAll() } returns listOf(project.toDto())
        coEvery { projectStatesDataSource.loadAll() } returns listOf(state)
        coEvery { taskDataSource.loadAll() } returns listOf(task)
        coEvery { dataSource.delete(project.toDto()) } just Runs
        coEvery { projectStatesDataSource.delete(state) } just Runs
        coEvery { taskDataSource.delete(task) } just Runs

        val projectDataSource = ProjectDataSource(dataSource, projectStatesDataSource, taskDataSource)

        // When
        projectDataSource.deleteProject(projectIdToDelete)

        // Then
        coVerify { dataSource.delete(project.toDto()) }
        coVerify { projectStatesDataSource.delete(state) }
        coVerify { taskDataSource.delete(task) }
    }

    @Test
    fun `deleteProject should not call delete on states and tasks if none are associated`() =
        runTest {
            // Given
            val projectIdToDelete = UUID.randomUUID()
            val projectToDelete = ProjectEntity(
                name = "Project to Delete",
                creatorId = UUID.randomUUID()
            ).copy(id = projectIdToDelete)

            coEvery { dataSource.loadAll() } returns listOf(projectToDelete.toDto())
            coEvery { projectStatesDataSource.loadAll() } returns emptyList()
            coEvery { taskDataSource.loadAll() } returns emptyList()
            coEvery { dataSource.delete(projectToDelete.toDto()) } just Runs

            // When
            projectDataSource.deleteProject(projectIdToDelete)

            // Then
            coVerify { dataSource.delete(projectToDelete.toDto()) }
            coVerify(exactly = 0) { projectStatesDataSource.delete(any()) }
            coVerify(exactly = 0) { taskDataSource.delete(any()) }
        }

    @Test
    fun `getProjectById throws when project not found in non-empty list`() = runTest {
        val existingEntity =
            ProjectEntity(name = "Existing", creatorId = UUID.randomUUID(), id = UUID.randomUUID())
        coEvery { dataSource.loadAll() } returns listOf(existingEntity.toDto())

        assertThrows<ProjectNotFoundException> {
            projectDataSource.getProjectById(UUID.randomUUID())
        }
    }

    @Test
    fun `deleteProject does not call delete on dataSource when project not found`() = runTest {
        coEvery { dataSource.loadAll() } returns emptyList()

        assertThrows<ProjectNotFoundException> {
            projectDataSource.deleteProject(UUID.randomUUID())
        }

        coVerify(exactly = 0) { dataSource.delete(any()) }
    }
}
