package org.baghdad.logic.usecase

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.TaskRepository
import org.junit.jupiter.api.Test
import java.util.*

class ViewServiceUseCaseTest {
    private val taskRepository: TaskRepository = mockk()
    private val stateRepository: ProjectStatesRepository = mockk()
    private val useCase = ViewServiceUseCase(taskRepository, stateRepository)

    @Test
    fun `should group tasks by state for given project`() {
        // give
        val projectId = UUID.randomUUID()
        val state1Id = UUID.randomUUID()
        val state2Id = UUID.randomUUID()

        val state1 = StateEntity(id = state1Id, name = "To Do", projectId = projectId, creatorId = UUID.randomUUID())
        val state2 = StateEntity(id = state2Id, name = "In Progress", projectId = projectId, creatorId = UUID.randomUUID())
        val states = listOf(state1, state2)

        val task1 = TaskEntity(
            id = UUID.randomUUID(),
            title = "Task 1",
            description = "Description 1",
            stateId = state1Id.toString(),
            projectId = projectId.toString(),
            creatorId = "user1"
        )
        val task2 = TaskEntity(
            id = UUID.randomUUID(),
            title = "Task 2",
            description = "Description 2",
            stateId = state2Id.toString(),
            projectId = projectId.toString(),
            creatorId = "user1"
        )
        val tasks = listOf(task1, task2)

        every { stateRepository.getAllStatesPerProject(projectId) } returns states
        every { taskRepository.getTasksByProjectId(projectId.toString()) } returns tasks

        // when
        val result = useCase.swimlane(projectId.toString())

        // then
        result shouldBe Result.success(
            mapOf(
                state1 to listOf(task1),
                state2 to listOf(task2)
            )
        )
    }

    @Test
    fun `should return empty map when no states exist for project`() {
        // give
        val projectId = UUID.randomUUID()
        every { stateRepository.getAllStatesPerProject(projectId) } returns emptyList()
        every { taskRepository.getTasksByProjectId(projectId.toString()) } returns emptyList()

        // when
        val result = useCase.swimlane(projectId.toString())

        // then
        result shouldBe Result.success(emptyMap())
    }

    @Test
    fun `should handle repository exception gracefully`() {
        // give
        val projectId = UUID.randomUUID()
        every { stateRepository.getAllStatesPerProject(projectId)
        } throws RuntimeException("Database error")

        // when
        val result = useCase.swimlane(projectId.toString())

        // then
        result.isFailure shouldBe true
        result.exceptionOrNull()
            ?.message shouldBe "Failed to fetch states or tasks for project $projectId: Database error"
    }
}
