package presentation.project

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.usecase.project.GetAllProjectsUseCase
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.project.GetAllProjectsUi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class GetAllProjectsUiTest {

    private lateinit var getAllProjectsUi: GetAllProjectsUi
    private lateinit var listProjectsUseCase: GetAllProjectsUseCase
    private lateinit var viewer: Viewer

    @BeforeEach
    fun setUp() {
        viewer = mockk(relaxed = true)
        listProjectsUseCase = mockk()
        getAllProjectsUi = GetAllProjectsUi(listProjectsUseCase, viewer)

        every { viewer.logMessage(any()) } just Runs
    }

    @Test
    fun `should list all projects successfully`() = runBlocking {
        // Given
        val project1 = ProjectEntity(name = "Project One", creatorId = UUID.randomUUID())
        val project2 = ProjectEntity(name = "Project Two", creatorId = UUID.randomUUID())
        val projects = listOf(project1, project2)

        // when
        coEvery { listProjectsUseCase() } returns projects

        // Act
        val result = getAllProjectsUi()

        // Then
        verify { viewer.logMessage("+----------------------+----------------------+") }
        verify { viewer.logMessage("| Project Number       | Name                 |") }
        verify { viewer.logMessage("+----------------------+----------------------+") }
        verify { viewer.logMessage("| 1                    | Project One          |") }
        verify { viewer.logMessage("| 2                    | Project Two          |") }
        verify { viewer.logMessage("+----------------------+----------------------+") }

        assert(result == listOf(project1.id, project2.id))  // Ensure the correct UUIDs are returned
    }

    @Test
    fun `should return empty list when no projects are available`() = runBlocking {
        // Given
        val projects = emptyList<ProjectEntity>()

        // when
        coEvery { listProjectsUseCase() } returns projects

        // Act
        val result = getAllProjectsUi()

        // Then
        verify { viewer.logMessage("+----------------------+----------------------+") }
        verify { viewer.logMessage("| Project Number       | Name                 |") }
        verify { viewer.logMessage("+----------------------+----------------------+") }
        verify { viewer.logMessage("+----------------------+----------------------+") }

        assertThat(result.first).isEmpty()
        assertThat(result.second).isEmpty()
    }
}