package logic.usecase


import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.StateExceptions.NotFoundException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.StateTransitionUseCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals


class StateTransitionUseCaseTest {

    private val taskRepository = mockk<TaskRepository>()
    private val projectStatesRepository = mockk<ProjectStatesRepository>()
    private val auditRepository = mockk<AuditRepository>()
    private lateinit var service: StateTransitionUseCase

    private lateinit var task: TaskEntity
    private lateinit var oldState: StateEntity
    private lateinit var newState: StateEntity
    private lateinit var user: UserEntity

    @BeforeEach
    fun setup() {
        service = StateTransitionUseCase(taskRepository, projectStatesRepository, auditRepository)

        val projectId = UUID.randomUUID()
        val oldStateId = UUID.randomUUID()
        val newStateId = UUID.randomUUID()
        val taskId = UUID.randomUUID()

        user = UserEntity(UUID.randomUUID(), "Test", "testUser", "hash", UserType.Mate)

        oldState = StateEntity(oldStateId, "TODO", projectId, user.id)
        newState =
            StateEntity(newStateId, "IN_PROGRESS", projectId, user.id)

        task = TaskEntity(
            taskId,
            "Test Task",
            "Description",
            oldStateId,
            projectId,
            user.id
        )
    }

    @Test
    fun `should successfully transition task state`() = runTest {
        coEvery { taskRepository.getTaskById(task.id) } returns task
        coEvery { projectStatesRepository.getStateById(task.stateId) } returns oldState
        coEvery { projectStatesRepository.getStateById(newState.id) } returns newState
        coEvery { taskRepository.updateTask(any()) } returns true
        coEvery { auditRepository.addAuditEntry(any()) } just Runs

        service.changeTaskState(task.id, newState.id, user)

        coVerify { taskRepository.updateTask(match { it.stateId == newState.id }) }
        coVerify { auditRepository.addAuditEntry(any()) }
    }

    @Test
    fun `should fail if new state belongs to different project`() = runTest {
        val otherProjectState = newState.copy(projectId = UUID.randomUUID())

        coEvery { taskRepository.getTaskById(task.id) } returns task
        coEvery { projectStatesRepository.getStateById(task.stateId) } returns oldState
        coEvery { projectStatesRepository.getStateById(newState.id) } returns otherProjectState

        try {
            service.changeTaskState(task.id, newState.id, user)
            Assertions.fail("Expected exception not thrown")
        } catch (e: Exception) {
            Assertions.assertTrue(e is NotFoundException)
            coVerify(exactly = 0) { taskRepository.updateTask(any()) }
            coVerify(exactly = 0) { auditRepository.addAuditEntry(any()) }
        }
    }

    @Test
    fun `should fail if task update fails`() = runTest {
        coEvery { taskRepository.getTaskById(task.id) } returns task
        coEvery { projectStatesRepository.getStateById(newState.id) } returns newState
        coEvery { taskRepository.updateTask(any()) } returns false

        try {
            service.changeTaskState(task.id, newState.id, user)
            Assertions.fail("Expected exception not thrown")
        } catch (_: Exception) {
            coVerify(exactly = 0) { auditRepository.addAuditEntry(any()) }
        }
    }

    @Test
    fun `should not fail if transitioning to the same state`() = runTest {
        coEvery { taskRepository.getTaskById(task.id) } returns task
        coEvery { projectStatesRepository.getStateById(oldState.id) } returns oldState

        service.changeTaskState(task.id, oldState.id, user)

        coVerify(exactly = 0) { taskRepository.updateTask(any()) }
        coVerify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }

    @Test
    fun `should fail if current state is not found`() = runTest {
        val taskId = task.id
        val newStateId = newState.id

        coEvery { taskRepository.getTaskById(taskId) } returns task
        coEvery { projectStatesRepository.getStateById(task.stateId) } returns null // Simulate missing current state

        val exception = assertThrows<Exception> {
            service.changeTaskState(taskId, newStateId, user)
        }

        assertEquals("Current state not found", exception.message)
        coVerify(exactly = 0) { taskRepository.updateTask(any()) }
        coVerify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }

    @Test
    fun `should fail if task state update fails`() = runTest {
        val taskId = task.id
        val newStateId = newState.id

        coEvery { taskRepository.getTaskById(taskId) } returns task
        coEvery { projectStatesRepository.getStateById(task.stateId) } returns oldState
        coEvery { projectStatesRepository.getStateById(newStateId) } returns newState
        coEvery { taskRepository.updateTask(any()) } returns false // Simulate failure
        coEvery { auditRepository.addAuditEntry(any()) }

        val exception = assertThrows<Exception> {
            service.changeTaskState(taskId, newStateId, user)
        }

        assertEquals("Failed to update task state", exception.message)
        coVerify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }
}