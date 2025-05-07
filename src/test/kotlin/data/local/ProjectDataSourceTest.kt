package data.local

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.local.ProjectDataSource
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.ProjectNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.Test

class ProjectDataSourceTest {
    private lateinit var dataSource: DataSource<ProjectEntity>
    private lateinit var projectStatesDataSource: DataSource<StateEntity>
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
    fun `should return data when there is a projects`() {
        // Given
        val projects = listOf(ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID()))
        every { dataSource.loadAll() } returns projects

        // When
        val result = projectDataSource.getAllProjects()

        // Then
        assert(result == projects)

    }

    @Test
    fun `should throw ProjectNotFoundException when updateProject and there is no projects`() {
        // Given
        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())
        every { dataSource.loadAll() } returns emptyList()

        // When & Then
        assertThrows<ProjectNotFoundException> { projectDataSource.updateProject(project) }

    }

    @Test
    fun `should throw ProjectNotFoundException when deleteProject and there is no projects`() {
        // Given
        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())
        every { dataSource.loadAll() } returns emptyList()

        // When & Then
        assertThrows<ProjectNotFoundException> { projectDataSource.deleteProject(project.id) }

    }

    @Test
    fun `createProject should call append on data source`() {
        // Given
        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())
        every { dataSource.append(project) } just Runs

        // When
        projectDataSource.createProject(project)

        // Then
        verify { dataSource.append(project) }
    }

    @Test
    fun `should return project when there is a project with same id`() {
        // Given
        val projects = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())
        every { dataSource.loadAll() } returns listOf(projects)

        // When
        val result = projectDataSource.getProjectById(projects.id)

        // Then
        assert(result == projects)
    }

    @Test
    fun `should throw ProjectNotFoundException when there is no project with same id`() {
        // Given
        val projects = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())
        every { dataSource.loadAll() } returns listOf(projects)

        // When & Then
        assertThrows<ProjectNotFoundException> { projectDataSource.getProjectById(UUID.randomUUID()) }
    }

    @Test
    fun `should return updated project when can update it successfully`() {
        // Given
        val allProjects = listOf(
            ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID()),
            ProjectEntity(name = "Project 2", creatorId = UUID.randomUUID())
        )
        val updatedProject = ProjectEntity(
            name = "Project 3",
            creatorId = UUID.randomUUID()
        ).copy(id = allProjects[1].id)

        every { dataSource.loadAll() } returns allProjects
        every { dataSource.update(listOf(updatedProject)) } just Runs

        // When
        projectDataSource.updateProject(updatedProject)

        // Then
        verify {
            dataSource.update(match {
                it.any { project -> project.id == updatedProject.id && updatedProject.name == "Project 3" }
            })
        }
    }

    @Test
    fun `should delete project with its states and tasks when delete it successfully`() {
        // Given
        val allProjects = listOf(
            ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID()),
        )
        val projectIdToDelete = allProjects[0].id
        val projectStates = listOf(
            StateEntity(
                name = "State 1",
                projectId = projectIdToDelete,
                creatorId = UUID.randomUUID()
            )
        )
        val staetIdtoDelete = projectStates[0].id
        val tasks = listOf(
            TaskEntity(
                title = "Task 1",
                description = "Description 1",
                stateId = UUID.randomUUID(),
                creatorId = UUID.randomUUID(),
                projectId = projectStates[0].id
            )
        )
        val taskToRemove = tasks[0].id
        every { dataSource.loadAll() } returns allProjects
        every { projectStatesDataSource.loadAll() } returns projectStates
        every { taskDataSource.loadAll() } returns tasks

        every { dataSource.update(any()) } just Runs
        every { projectStatesDataSource.update(any()) } just Runs
        every { taskDataSource.update(any()) } just Runs

        // when
        projectDataSource.deleteProject(projectIdToDelete)

        // Then
        verify {
            dataSource.update(match { list -> list.none { it.id == projectIdToDelete } })
            dataSource.update(match { list -> list.none { it.id == staetIdtoDelete } })
            dataSource.update(match { list -> list.none { it.id == taskToRemove } })
        }
    }

}