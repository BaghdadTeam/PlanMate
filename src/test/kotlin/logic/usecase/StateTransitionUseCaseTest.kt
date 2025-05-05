package logic.usecase


import io.mockk.every
import io.mockk.mockk
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.StateTransitionUseCase
import org.junit.jupiter.api.Assertions
import java.util.UUID
import kotlin.test.Test
import org.junit.jupiter.api.BeforeEach
import io.mockk.verify
import org.baghdad.logic.model.exceptions.StateExceptions.NotFoundException
import org.junit.jupiter.api.assertThrows
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
    fun `should successfully transition task state`() {
        every { taskRepository.getTaskById(task.id) } returns task
        every { projectStatesRepository.getStateById(task.stateId) } returns oldState
        every { projectStatesRepository.getStateById(newState.id) } returns newState
        every { taskRepository.updateTask(any()) } returns true
        every { auditRepository.addAuditEntry(any()) } returns true

        service.changeTaskState(task.id, newState.id, user)

        verify { taskRepository.updateTask(match { it.stateId == newState.id }) }
        verify { auditRepository.addAuditEntry(any()) }
    }

    @Test
    fun `should fail if new state belongs to different project`() {
        val otherProjectState = newState.copy(projectId = UUID.randomUUID())

        every { taskRepository.getTaskById(task.id) } returns task
        every { projectStatesRepository.getStateById(task.stateId) } returns oldState
        every { projectStatesRepository.getStateById(newState.id) } returns otherProjectState

        try {
            service.changeTaskState(task.id, newState.id, user)
            Assertions.fail("Expected exception not thrown")
        } catch (e: Exception) {
            Assertions.assertTrue(e is NotFoundException)
            verify(exactly = 0) { taskRepository.updateTask(any()) }
            verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
        }
    }

    @Test
    fun `should fail if task update fails`() {
        every { taskRepository.getTaskById(task.id) } returns task
        every { projectStatesRepository.getStateById(newState.id) } returns newState
        every { taskRepository.updateTask(any()) } returns false

        try {
            service.changeTaskState(task.id, newState.id, user)
            Assertions.fail("Expected exception not thrown")
        } catch (_: Exception) {
            verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
        }
    }

    @Test
    fun `should not fail if transitioning to the same state`() {
        every { taskRepository.getTaskById(task.id) } returns task
        every { projectStatesRepository.getStateById(oldState.id) } returns oldState

        service.changeTaskState(task.id, oldState.id, user)

        verify(exactly = 0) { taskRepository.updateTask(any()) }
        verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }

    @Test
    fun `should fail if current state is not found`() {
        val taskId = task.id
        val newStateId = newState.id

        every { taskRepository.getTaskById(taskId) } returns task
        every { projectStatesRepository.getStateById(task.stateId) } returns null // Simulate missing current state

        val exception = assertThrows<Exception> {
            service.changeTaskState(taskId, newStateId, user)
        }

        assertEquals("Current state not found", exception.message)
        verify(exactly = 0) { taskRepository.updateTask(any()) }
        verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }

    @Test
    fun `should fail if task state update fails`() {
        val taskId = task.id
        val newStateId = newState.id

        every { taskRepository.getTaskById(taskId) } returns task
        every { projectStatesRepository.getStateById(task.stateId) } returns oldState
        every { projectStatesRepository.getStateById(newStateId) } returns newState
        every { taskRepository.updateTask(any()) } returns false // Simulate failure
        every { auditRepository.addAuditEntry(any()) } returns true

        val exception = assertThrows<Exception> {
            service.changeTaskState(taskId, newStateId, user)
        }

        assertEquals("Failed to update task state", exception.message)
        verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }
}