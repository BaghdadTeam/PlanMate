package org.baghdad.ui

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.usecase.ViewServiceUseCase
import org.junit.jupiter.api.Test
import java.util.*

class SwimlaneUITest {
    private val viewServiceUseCase: ViewServiceUseCase = mockk()
    private val ui = SwimlaneUI(viewServiceUseCase)

    @Test
    fun `viewSwimlaneCommand should return success result when useCase succeeds`() {
        // give
        val projectId = "project123"
        val state = StateEntity(UUID.randomUUID(), "To Do", projectId, "user1")
        val task = TaskEntity(
            id = UUID.randomUUID(),
            title = "Task",
            description = "Desc",
            stateId = state.id.toString(),
            projectId = projectId,
            creatorId = "user1"
        )
        val expectedMap = mapOf(state to listOf(task))
        every { viewServiceUseCase.swimlane(projectId)
        } returns Result.success(expectedMap)

        // when
        val result = ui.viewSwimlaneCommand(projectId)

        // then
        result shouldBe Result.success(expectedMap)
    }

    @Test
    fun `viewSwimlaneCommand should return failure result when useCase fails`() {
        // give
        val projectId = "project123"
        val error = Exception("oops")
        every { viewServiceUseCase.swimlane(projectId)
        } returns Result.failure(error)

        // when
        val result = ui.viewSwimlaneCommand(projectId)

        // then
        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe error
    }


    @Test
    fun `should return error message when swimlane retrieval fails`() {
        // give
        val projectId = "project123"
        every { viewServiceUseCase.swimlane(projectId)
        } returns Result.failure(Exception("Failed to fetch data"))

        // when
        val result = ui.renderAsciiSwimlane(projectId)

        // then
        result shouldBe "Error: Failed to fetch data"
    }
    @Test
    fun `should render ASCII swimlane correctly`() {
        // give
        val projectId = "project123"
        val state = StateEntity(UUID.randomUUID(), "To Do", projectId, "user1")
        val task = TaskEntity(
            id = UUID.randomUUID(),
            title = "Task 1",
            description = "Some description",
            stateId = state.id.toString(),
            projectId = projectId,
            creatorId = "user1"
        )
        every { viewServiceUseCase.swimlane(projectId)
        } returns Result.success(mapOf(state to listOf(task)))

        // when
        val result = ui.renderAsciiSwimlane(projectId)

        // then
        result.contains("To Do") shouldBe true
        result.contains("Task 1") shouldBe true
    }

}
