package logic.usecase

import com.google.common.truth.Truth.assertThat
import io.github.classgraph.AnnotationInfoList.emptyList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.ViewServiceUseCase
import org.junit.jupiter.api.Test
import java.util.*

class ViewServiceUseCaseTest {
    private val taskRepository: TaskRepository = mockk()
    private val stateRepository: ProjectStatesRepository = mockk()
    private val useCase = ViewServiceUseCase(taskRepository, stateRepository)

    @Test
    fun `should group tasks by state for given project`() = runTest {
        // given
        val projectId = UUID.randomUUID()
        val state1Id = UUID.randomUUID()
        val state2Id = UUID.randomUUID()

        val state1 = TaskStateEntity(id = state1Id, name = "To Do", projectId = projectId, creatorId = UUID.randomUUID())
        val state2 =
            TaskStateEntity(id = state2Id, name = "In Progress", projectId = projectId, creatorId = UUID.randomUUID())
        val states = listOf(state1, state2)

        val task1 = TaskEntity(
            id = UUID.randomUUID(),
            title = "Task 1",
            description = "Description 1",
            stateId = state1Id,
            projectId = projectId,
            creatorId = UUID.randomUUID()
        )
        val task2 = TaskEntity(
            id = UUID.randomUUID(),
            title = "Task 2",
            description = "Description 2",
            stateId = state2Id,
            projectId = projectId,
            creatorId = UUID.randomUUID()
        )
        val tasks = listOf(task1, task2)

        coEvery { stateRepository.getAllStatesPerProject(projectId) } returns states
        coEvery { taskRepository.getTasksByProjectId(projectId) } returns tasks

        // when
        val result = useCase.invoke(projectId)

        // then
        assertThat(result).isEqualTo(
            mapOf(
                state1 to listOf(task1),
                state2 to listOf(task2)
            )
        )
    }

    @Test
    fun `should return empty map when no states exist for project`() = runTest {
        // given
        val projectId = UUID.randomUUID()
        coEvery { stateRepository.getAllStatesPerProject(projectId) } returns emptyList<TaskStateEntity>()
        coEvery { taskRepository.getTasksByProjectId(projectId) } returns emptyList<TaskEntity>()

        // when
        val result = useCase.invoke(projectId)

        // then
        assertThat(result).isEqualTo(emptyMap<TaskStateEntity, List<TaskEntity>>())
    }

    @Test
    fun `should handle repository exception gracefully`() = runTest {
        // given
        val projectId = UUID.randomUUID()
        coEvery { stateRepository.getAllStatesPerProject(projectId) } throws RuntimeException("Database error")

        // when
        val exception = try {
            useCase.invoke(projectId)
            null // This should not be reached
        } catch (e: Exception) {
            e
        }

        // then
        assertThat(exception).isNotNull()
        assertThat(exception?.message).isEqualTo("Failed to fetch states or tasks for project $projectId: Database error")
    }

    @Test
    fun `should return states with empty task lists when no tasks exist for project`() = runTest {
        // given
        val projectId = UUID.randomUUID()
        val state1Id = UUID.randomUUID()
        val state2Id = UUID.randomUUID()

        val state1 = TaskStateEntity(id = state1Id, name = "To Do", projectId = projectId, creatorId = UUID.randomUUID())
        val state2 =
            TaskStateEntity(id = state2Id, name = "In Progress", projectId = projectId, creatorId = UUID.randomUUID())
        val states = listOf(state1, state2)

        coEvery { stateRepository.getAllStatesPerProject(projectId) } returns states
        coEvery { taskRepository.getTasksByProjectId(projectId) } returns emptyList<TaskEntity>()

        // when
        val result = useCase.invoke(projectId)

        // then
        assertThat(result).isEqualTo(
            mapOf(
                state1 to emptyList(),
                state2 to emptyList()
            )
        )
    }
}