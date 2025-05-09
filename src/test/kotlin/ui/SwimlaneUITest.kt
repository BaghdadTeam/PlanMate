package ui

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.usecase.ViewServiceUseCase
import org.baghdad.ui.RenderSwimlaneUI
import org.junit.jupiter.api.Test
import java.util.*

class SwimlaneUITest {
    private val viewServiceUseCase: ViewServiceUseCase = mockk()
    private val ui = RenderSwimlaneUI(viewServiceUseCase)


    @Test
    fun `should return error message when swimlane retrieval fails`() {
        // give
        val projectId = UUID.randomUUID()
        coEvery {
            viewServiceUseCase.swimlane(projectId)
        } returns Result.failure(Exception("Failed to fetch data"))

        // when
        val result = ui.invoke(projectId)

        // then
        result shouldBe "Error: Failed to fetch data"
    }

    @Test
    fun `should render ASCII swimlane correctly`() {
        // give
        val projectId = UUID.randomUUID()
        val creatorId = UUID.randomUUID()
        val state = StateEntity(UUID.randomUUID(), "To Do", projectId, creatorId)
        val task = TaskEntity(
            id = UUID.randomUUID(),
            title = "Task 1",
            description = "Some description",
            stateId = state.id,
            projectId = projectId,
            creatorId = creatorId
        )
        coEvery {
            viewServiceUseCase.swimlane(projectId)
        } returns Result.success(mapOf(state to listOf(task)))

        // when
        val result = ui.invoke(projectId)

        // then
        result.contains("To Do") shouldBe true
        result.contains("Task 1") shouldBe true
    }

}
