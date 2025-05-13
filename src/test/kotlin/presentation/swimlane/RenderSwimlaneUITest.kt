package presentation.swimlane

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.usecase.ViewServiceUseCase
import org.baghdad.presentation.swimlane.RenderSwimlaneUI
import org.junit.jupiter.api.Test
import java.util.*

class RenderSwimlaneUITest {

    private val viewServiceUseCase: ViewServiceUseCase = mockk()
    private val ui = RenderSwimlaneUI()

    @Test
    fun `should return error message when swimlane retrieval fails`() {
        // Given

        val projectId = UUID.randomUUID()
        val project : Map<TaskStateEntity, List<TaskEntity>> = emptyMap()

        // Mocking failure result by throwing exception
        coEvery {
            viewServiceUseCase.invoke(projectId)
        } throws Exception("Failed to fetch data")

        // When
        val result = ui.invoke(project)

        // Then
        assertThat(result).isEqualTo("Error: Failed to fetch data")
    }

    @Test
    fun `should render ASCII swimlane correctly`() {
        // Given
        val projectId = UUID.randomUUID()
        val creatorId = UUID.randomUUID()
        val state = TaskStateEntity(UUID.randomUUID(), "To Do", projectId, creatorId)
        val task = TaskEntity(
            id = UUID.randomUUID(),
            title = "Task 1",
            description = "Some description",
            stateId = state.id,
            projectId = projectId,
            creatorId = creatorId
        )
        val project = mapOf(state to listOf(task))

        // Mocking successful result with data
        coEvery {
            viewServiceUseCase.invoke(projectId)
        } returns project

        // When
        val result = ui.invoke(project)

        // Then
        assertThat(result).contains("To Do")
        assertThat(result).contains("Task 1")
    }

    @Test
    fun `should return error message when swimlane data is empty`() {
        // Given
        val projectId = UUID.randomUUID()
        val project : Map<TaskStateEntity, List<TaskEntity>> = emptyMap()
        // Mocking empty data result (No states)
        coEvery {
            viewServiceUseCase.invoke(projectId)
        } returns emptyMap()

        // When
        val result = ui.invoke(project)

        // Then
        assertThat(result).isEqualTo("Error: Failed to fetch data")
    }
}