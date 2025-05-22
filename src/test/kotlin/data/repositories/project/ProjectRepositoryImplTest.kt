package data.repositories.project

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.data.local.ProjectDataSource
import org.baghdad.data.repositories.project.ProjectRepositoryImpl
import org.baghdad.data.repositories.toDto
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.repositories.ProjectRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ProjectRepositoryImplTest {

    private lateinit var dataSource: ProjectDataSource
    private lateinit var projectRepository: ProjectRepository

    @BeforeEach
    fun setUp() {
        dataSource = mockk(relaxed = true)
        projectRepository = ProjectRepositoryImpl(dataSource)
    }

    @Test
    fun `should return all projects from dataSource`() = runTest {
        // Given
        val projects = listOf(
            ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID()),
        )
        coEvery { dataSource.getAllProjects() } returns projects.map { it.toDto() }

        // When
        val result = projectRepository.getAllProjects()

        // Then
        assertThat(result == projects).isTrue()
    }

    @Test
    fun `should return true when project is found by id`() = runTest {
        // Given
        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())
        coEvery { dataSource.getProjectById(project.id) } returns project.toDto()
        // When

        val result = projectRepository.getProjectById(project.id)
        // Then
        assertThat(result).isEqualTo(project)
    }

    @Test
    fun `editProject should call dataSource editProject`() = runTest {
        // Given
        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())
        // Then
        projectRepository.editProject(project)
        // When
        coVerify { projectRepository.editProject(project) }
    }

    @Test
    fun `deleteProject should call dataSource deleteProject`() = runTest {
        // Given
        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())
        // Then
        projectRepository.deleteProject(project.id)
        // When
        coVerify { projectRepository.deleteProject(project.id) }
    }

    @Test
    fun `createProject should call dataSource createProject`() = runTest {
        // Given
        val project = ProjectEntity(name = "Project 1", creatorId = UUID.randomUUID())
        // Then
        projectRepository.createProject(project)
        // When
        coVerify { projectRepository.createProject(project) }
    }

}
