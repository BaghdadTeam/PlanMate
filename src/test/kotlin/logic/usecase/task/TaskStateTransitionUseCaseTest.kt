package logic.usecase.task

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.NotFoundException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.task.TaskStateTransitionUseCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.assertEquals

class TaskStateTransitionUseCaseTest {

    private val taskRepository = mockk<TaskRepository>()
    private val projectStatesRepository = mockk<ProjectStatesRepository>()
    private val userRepository = mockk<UserRepository>() // NEW
    private val auditRepository = mockk<AuditRepository>()
    private lateinit var service: TaskStateTransitionUseCase

    private lateinit var task: TaskEntity
    private lateinit var oldState: StateEntity
    private lateinit var newState: StateEntity
    private lateinit var user: UserEntity
    private val sessionManager = mockk<SessionManager>()

    @BeforeEach
    fun setup() {
        service = TaskStateTransitionUseCase(taskRepository, projectStatesRepository, userRepository, auditRepository,sessionManager)

        val projectId = UUID.randomUUID()
        val oldStateId = UUID.randomUUID()
        val newStateId = UUID.randomUUID()
        val taskId = UUID.randomUUID()

        user = UserEntity(UUID.randomUUID(), "Test", "testUser", UserType.Mate)

        oldState = StateEntity(oldStateId, "TODO", projectId, user.id)
        newState = StateEntity(newStateId, "IN_PROGRESS", projectId, user.id)

        task = TaskEntity(
            taskId,
            "Test Task",
            "Description",
            oldStateId,
            projectId,
            user.id
        )
        coEvery { sessionManager.isAuthenticated() } returns true
    }

    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows <UnauthorizedException> { service.changeTaskState(UUID.randomUUID(), task.stateId,user.id)  }
    }
    @Test
    fun `should successfully transition task state`() = runTest {
        coEvery { taskRepository.getTaskById(task.id) } returns task
        coEvery { projectStatesRepository.getStateById(task.stateId) } returns oldState
        coEvery { projectStatesRepository.getStateById(newState.id) } returns newState
        coEvery { taskRepository.updateTask(any()) } returns true
        coEvery { auditRepository.addAuditEntry(any()) } just Runs
        coEvery { userRepository.getUserById(user.id) } returns user // NEW

        service.changeTaskState(task.id, newState.id, user.id)

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
            service.changeTaskState(task.id, newState.id, user.id)
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
        coEvery { projectStatesRepository.getStateById(task.stateId) } returns oldState
        coEvery { projectStatesRepository.getStateById(newState.id) } returns newState
        coEvery { taskRepository.updateTask(any()) } returns false
        coEvery { userRepository.getUserById(user.id) } returns user

        try {
            service.changeTaskState(task.id, newState.id, user.id)
            Assertions.fail("Expected exception not thrown")
        } catch (_: Exception) {
            coVerify(exactly = 0) { auditRepository.addAuditEntry(any()) }
        }
    }

    @Test
    fun `should fail if transitioning to the same state`() = runTest {
        coEvery { taskRepository.getTaskById(task.id) } returns task
        coEvery { projectStatesRepository.getStateById(oldState.id) } returns oldState

        assertThrows<Exception> {
            service.changeTaskState(task.id, oldState.id, user.id)
        }
    }

    @Test
    fun `should fail if task state update fails`() = runTest {
        val taskId = task.id
        val newStateId = newState.id

        coEvery { taskRepository.getTaskById(taskId) } returns task
        coEvery { projectStatesRepository.getStateById(task.stateId) } returns oldState
        coEvery { projectStatesRepository.getStateById(newStateId) } returns newState
        coEvery { taskRepository.updateTask(any()) } returns false
        coEvery { userRepository.getUserById(user.id) } returns user

        val exception = assertThrows<Exception> {
            service.changeTaskState(taskId, newStateId, user.id)
        }

        assertEquals("Failed to update task state", exception.message)
        coVerify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }
}