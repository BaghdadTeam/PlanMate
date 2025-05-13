package data.repositories.project

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.dto.project.ProjectDto
import org.baghdad.data.mapper.toDto
import org.baghdad.data.repositories.project.ProjectRepositoryImpl
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.exceptions.ProjectNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class ProjectRepositoryImplTest {

    private lateinit var dataSource: DataSource<ProjectDto>
    private lateinit var projectRepository: ProjectRepositoryImpl

    @BeforeEach
    fun setUp() {
        dataSource = mockk(relaxed = true)
        projectRepository = ProjectRepositoryImpl(dataSource)
    }

    @Test
    fun `getAllProjects returns converted entities`() = runTest {
        val entity = ProjectEntity(name = "Project One", creatorId = UUID.randomUUID())
        val dto = entity.toDto()
        coEvery { dataSource.loadAll() } returns listOf(dto)

        val result = projectRepository.getAllProjects()

        assertThat(result).containsExactly(entity)
    }

    @Test
    fun `getAllProjects returns empty list when dataSource returns empty list`() = runTest {
        coEvery { dataSource.loadAll() } returns emptyList()

        val result = projectRepository.getAllProjects()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getAllProjects returns multiple entities when dataSource has multiple DTOs`() = runTest {
        val projectOne = ProjectEntity(name = "Project One", creatorId = UUID.randomUUID())
        val projectTwo = ProjectEntity(name = "Project Two", creatorId = UUID.randomUUID())
        val dtos = listOf(projectOne.toDto(), projectTwo.toDto())
        coEvery { dataSource.loadAll() } returns dtos

        val result = projectRepository.getAllProjects()

        assertThat(result).containsExactly(projectOne, projectTwo)
    }

    @Test
    fun `getProjectById returns entity when found`() = runTest {
        val entity = ProjectEntity(name = "Project Alpha", creatorId = UUID.randomUUID())
        val dto = entity.toDto()
        coEvery { dataSource.loadAll() } returns listOf(dto)

        val result = projectRepository.getProjectById(entity.id)

        assertThat(result).isEqualTo(entity)
    }

    @Test
    fun `getProjectById throws when not found`() = runTest {
        coEvery { dataSource.loadAll() } returns emptyList()

        assertThrows<ProjectNotFoundException> {
            projectRepository.getProjectById(UUID.randomUUID())
        }
    }

    @Test
    fun `getProjectById finds correct entity in multiple projects`() = runTest {
        val targetProject = ProjectEntity(name = "Target Project", creatorId = UUID.randomUUID())
        val otherProject = ProjectEntity(name = "Other Project", creatorId = UUID.randomUUID())
        val dtos = listOf(targetProject.toDto(), otherProject.toDto())
        coEvery { dataSource.loadAll() } returns dtos

        val result = projectRepository.getProjectById(targetProject.id)

        assertThat(result).isEqualTo(targetProject)
    }

    @Test
    fun `createProject calls append on data source`() = runTest {
        val entity = ProjectEntity(name = "New Project", creatorId = UUID.randomUUID())
        val dto = entity.toDto()
        coEvery { dataSource.append(dto) } just Runs

        projectRepository.createProject(entity)

        coVerify { dataSource.append(dto) }
    }

    @Test
    fun `editProject calls update on data source`() = runTest {
        val entity = ProjectEntity(name = "Edited Project", creatorId = UUID.randomUUID())
        val dto = entity.toDto()
        coEvery { dataSource.update(dto) } just Runs

        projectRepository.editProject(entity)

        coVerify { dataSource.update(dto) }
    }

    @Test
    fun `deleteProject calls delete on data source when exists`() = runTest {
        val entity = ProjectEntity(name = "Project To Delete", creatorId = UUID.randomUUID())
        val dto = entity.toDto()
        coEvery { dataSource.loadAll() } returns listOf(dto)
        coEvery { dataSource.delete(dto) } just Runs

        projectRepository.deleteProject(entity.id)

        coVerify { dataSource.delete(dto) }
    }

    @Test
    fun `deleteProject throws when project not found`() = runTest {
        val nonExistentId = UUID.randomUUID()
        coEvery { dataSource.loadAll() } returns emptyList()

        assertThrows<ProjectNotFoundException> {
            projectRepository.deleteProject(nonExistentId)
        }

        coVerify(exactly = 0) { dataSource.delete(any()) }
    }

    @Test
    fun `deleteProject throws when project not found in non-empty list`() = runTest {
        val existingEntity = ProjectEntity(
            name = "Existing Project",
            creatorId = UUID.randomUUID(),
            id = UUID.randomUUID()
        )
        val nonExistentId = UUID.randomUUID()
        coEvery { dataSource.loadAll() } returns listOf(existingEntity.toDto())

        assertThrows<ProjectNotFoundException> {
            projectRepository.deleteProject(nonExistentId)
        }

        coVerify(exactly = 0) { dataSource.delete(any()) }
    }

    @Test
    fun `deleteProject deletes correct project in multiple entries`() = runTest {
        val projectToDelete = ProjectEntity(name = "Delete Me", creatorId = UUID.randomUUID())
        val projectToKeep = ProjectEntity(name = "Keep Me", creatorId = UUID.randomUUID())
        coEvery { dataSource.loadAll() } returns listOf(
            projectToDelete.toDto(),
            projectToKeep.toDto()
        )
        coEvery { dataSource.delete(projectToDelete.toDto()) } just Runs

        projectRepository.deleteProject(projectToDelete.id)

        coVerify(exactly = 1) { dataSource.delete(projectToDelete.toDto()) }
        coVerify(exactly = 0) { dataSource.delete(projectToKeep.toDto()) }
    }

    @Test
    fun `deleteProject passes correct DTO to delete`() = runTest {
        val entity = ProjectEntity(name = "Test", creatorId = UUID.randomUUID())
        val capturedDto = slot<ProjectDto>()
        coEvery { dataSource.loadAll() } returns listOf(entity.toDto())
        coEvery { dataSource.delete(capture(capturedDto)) } just Runs

        projectRepository.deleteProject(entity.id)

        assertThat(capturedDto.captured).isEqualTo(entity.toDto())
    }

    @Test
    fun `getProjectById calls loadAll and throws if not found`() = runTest {
        val id = UUID.randomUUID()
        coEvery { dataSource.loadAll() } returns emptyList()

        assertThrows<ProjectNotFoundException> {
            projectRepository.getProjectById(id)
        }

        coVerify { dataSource.loadAll() }
    }

    @Test
    fun `getAllProjects calls loadAll on data source`() = runTest {
        coEvery { dataSource.loadAll() } returns emptyList()

        projectRepository.getAllProjects()

        coVerify { dataSource.loadAll() }
    }

    @Test
    fun `deleteProject calls loadAll before deletion`() = runTest {
        val entity = ProjectEntity(name = "To be deleted", creatorId = UUID.randomUUID())
        coEvery { dataSource.loadAll() } returns listOf(entity.toDto())
        coEvery { dataSource.delete(any()) } just Runs

        projectRepository.deleteProject(entity.id)

        coVerify { dataSource.loadAll() }
    }

    @Test
    fun `deleteProject only deletes when project found`() = runTest {
        val existing = ProjectEntity(name = "X", creatorId = UUID.randomUUID())
        val dto = existing.toDto()
        coEvery { dataSource.loadAll() } returns listOf(dto)
        coEvery { dataSource.delete(dto) } just Runs

        projectRepository.deleteProject(existing.id)

        coVerify { dataSource.delete(dto) }
    }

    @Test
    fun `deleteProject does not call delete when project not found`() = runTest {
        coEvery { dataSource.loadAll() } returns emptyList()

        assertThrows<ProjectNotFoundException> {
            projectRepository.deleteProject(UUID.randomUUID())
        }

        coVerify(exactly = 0) { dataSource.delete(any()) }
    }
}