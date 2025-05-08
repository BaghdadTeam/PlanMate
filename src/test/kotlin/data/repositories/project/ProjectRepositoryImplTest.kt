package data.repositories.project

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.data.local.ProjectDataSource
import org.baghdad.data.repository.project.ProjectRepositoryImpl
import org.baghdad.logic.model.entities.ProjectEntity
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test

class ProjectRepositoryImplTest {
    private lateinit var dataSource: ProjectDataSource
    private lateinit var projectRepository: ProjectRepositoryImpl

    @BeforeEach
    fun setUp() {
        dataSource = mockk(relaxed = true)
        projectRepository = ProjectRepositoryImpl(dataSource)
    }

    @Test
    fun `should return all projects from dataSource`() {
        // Given
        val projects = listOf(
            ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID()),
        )
        every { dataSource.getAllProjects() } returns projects

        // When
        val result = projectRepository.getAllProjects()

        // Then
        assertThat(result == projects).isTrue()

    }

    @Test
    fun `should return true when project is found by id`() {
        // Given

        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())

        every { dataSource.getProjectById(project.id) } returns project

        // When
        val result = projectRepository.getProjectById(project.id)

        // Then
        assertThat(result).isEqualTo(project)

    }

    @Test
    fun `editProject should call dataSource editProject`() {
        // Given
        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())

        // Then
        projectRepository.editProject(project)

        // When
        verify { projectRepository.editProject(project) }
    }

    @Test
    fun `deleteProject should call dataSource deleteProject`() {
        // Given
        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())

        // Then
        projectRepository.deleteProject(project.id)

        // When
        verify { projectRepository.deleteProject(project.id) }
    }

    @Test
    fun `createProject should call dataSource createProject`() {
        // Given
        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())

        // Then
        projectRepository.createProject(project)

        // When
        verify { projectRepository.createProject(project) }
    }
}
