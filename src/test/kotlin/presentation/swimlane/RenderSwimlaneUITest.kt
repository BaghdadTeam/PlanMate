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
    private val ui = RenderSwimlaneUI(viewServiceUseCase)

    @Test
    fun `should return error message when swimlane retrieval fails`() {
        // Given
        val projectId = UUID.randomUUID()

        // Mocking failure result by throwing exception
        coEvery {
            viewServiceUseCase.swimlane(projectId)
        } throws Exception("Failed to fetch data")

        // When
        val result = ui.invoke(projectId)

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

        // Mocking successful result with data
        coEvery {
            viewServiceUseCase.swimlane(projectId)
        } returns mapOf(state to listOf(task))

        // When
        val result = ui.invoke(projectId)

        // Then
        assertThat(result).contains("To Do")
        assertThat(result).contains("Task 1")
    }

    @Test
    fun `should return error message when swimlane data is empty`() {
        // Given
        val projectId = UUID.randomUUID()

        // Mocking empty data result (No states)
        coEvery {
            viewServiceUseCase.swimlane(projectId)
        } returns emptyMap()

        // When
        val result = ui.invoke(projectId)

        // Then
        assertThat(result).isEqualTo("Error: Failed to fetch data")
    }
}