package data.repositories.projectstate

import com.google.common.truth.Truth
import helpers.projectStates.ProjectStatesEntityTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.data.local.ProjectStatesDataSource
import org.baghdad.data.repositories.projectstates.ProjectStatesRepositoryImp
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ProjectStatesRepositoryImpTest {

    private lateinit var dataSource: ProjectStatesDataSource
    private lateinit var projectStatesDataSource: ProjectStatesRepositoryImp

    @BeforeEach
    fun setup() {
        dataSource = mockk(relaxed = true)
        projectStatesDataSource = ProjectStatesRepositoryImp(dataSource)
    }


    @Test
    fun `should return data when there is a states for project`() = runTest {
        // Given
        val projectStates = ProjectStatesEntityTestData.getAllStatesPerProject()
        val projectId = projectStates[0].projectId
        coEvery { dataSource.getAllStatesForProject() } returns projectStates

        // When
        val result = projectStatesDataSource.getAllStatesPerProject(projectId)

        // Then
        Truth.assertThat(result).isEqualTo(listOf(projectStates[0]))
    }

    @Test
    fun `should not return data when there is no project id match`() = runTest {
        // Given
        val projectStates = ProjectStatesEntityTestData.getAllStatesPerProject()
        val projectId = UUID.randomUUID()
        coEvery { dataSource.getAllStatesForProject() } returns projectStates

        // When
        val result = projectStatesDataSource.getAllStatesPerProject(projectId)

        // Then
        Truth.assertThat(result).isEmpty()
    }


    @Test
    fun `should return state when there is a state with same id`() = runTest {
        val projectStates = ProjectStatesEntityTestData.todoState()
        val id = projectStates.id
        coEvery { dataSource.getStateById(id) } returns projectStates

        // When
        val result = projectStatesDataSource.getStateById(id)

        // Then
        Truth.assertThat(result).isEqualTo(projectStates)
        coVerify { dataSource.getStateById(id) }
    }


    @Test
    fun `should return state when can add state successfully`() = runTest {
        val state = ProjectStatesEntityTestData.inProgressState()

        projectStatesDataSource.createState(state)

        coVerify { dataSource.createState(state) }
    }
}