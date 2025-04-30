package logic.usecase


import io.mockk.every
import io.mockk.mockk
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.StateRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.StateTransitionUseCase
import org.junit.jupiter.api.Assertions
import java.util.UUID
import kotlin.test.Test
import org.junit.jupiter.api.BeforeEach
import io.mockk.verify
import org.baghdad.logic.model.exceptions.StateExceptions.NotFoundException


class StateTransitionUseCaseTest {

    private val taskRepository = mockk<TaskRepository>()
    private val stateRepository = mockk<StateRepository>()
    private val auditRepository = mockk<AuditRepository>()
    private lateinit var service: StateTransitionUseCase

    private lateinit var task: TaskEntity
    private lateinit var oldState: StateEntity
    private lateinit var newState: StateEntity
    private lateinit var user: UserEntity

    @BeforeEach
    fun setup() {
        service = StateTransitionUseCase(taskRepository, stateRepository, auditRepository)

        val projectId = UUID.randomUUID().toString()
        val oldStateId = UUID.randomUUID().toString()
        val newStateId = UUID.randomUUID().toString()
        val taskId = UUID.randomUUID().toString()

        user = UserEntity(UUID.randomUUID(), "Test", "testUser", "hash", UserType.Mate)

        oldState = StateEntity(UUID.fromString(oldStateId), "TODO", projectId, user.id.toString())
        newState =
            StateEntity(UUID.fromString(newStateId), "IN_PROGRESS", projectId, user.id.toString())

        task = TaskEntity(
            UUID.fromString(taskId),
            "Test Task",
            "Description",
            oldStateId,
            projectId,
            user.id.toString()
        )
    }

    @Test
    fun `should successfully transition task state`() {
        every { taskRepository.getTaskById(task.id.toString()) } returns task
        every { stateRepository.getStateById(task.stateId) } returns oldState
        every { stateRepository.getStateById(newState.id.toString()) } returns newState
        every { taskRepository.updateTask(any()) } returns true
        every { auditRepository.addAuditEntry(any()) } returns true

        service.changeTaskState(task.id.toString(), newState.id.toString(), user)

        verify { taskRepository.updateTask(match { it.stateId == newState.id.toString() }) }
        verify { auditRepository.addAuditEntry(any()) }
    }

    @Test
    fun `should fail if new state not exists`() {
        every { taskRepository.getTaskById(task.id.toString()) } returns task
        every { stateRepository.getStateById(task.stateId) } returns oldState
        every { stateRepository.getStateById("invalid-state") } returns null

        try {
            service.changeTaskState(task.id.toString(), "invalid-state", user)
            Assertions.fail("Expected exception not thrown")
        } catch (e: Exception) {
            Assertions.assertTrue(e is NotFoundException)
            verify(exactly = 0) { taskRepository.updateTask(any()) }
        }
    }

    @Test
    fun `should fail if new state belongs to different project`() {
        val otherProjectState = newState.copy(projectId = UUID.randomUUID().toString())

        every { taskRepository.getTaskById(task.id.toString()) } returns task
        every { stateRepository.getStateById(task.stateId) } returns oldState
        every { stateRepository.getStateById(newState.id.toString()) } returns otherProjectState

        try {
            service.changeTaskState(task.id.toString(), newState.id.toString(), user)
            Assertions.fail("Expected exception not thrown")
        } catch (e: Exception) {
            Assertions.assertTrue(e is NotFoundException)
            verify(exactly = 0) { taskRepository.updateTask(any()) }
            verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
        }
    }

    @Test
    fun `should fail if task update fails`() {
        every { taskRepository.getTaskById(task.id.toString()) } returns task
        every { stateRepository.getStateById(newState.id.toString()) } returns newState
        every { taskRepository.updateTask(any()) } returns false

        try {
            service.changeTaskState(task.id.toString(), newState.id.toString(), user)
            Assertions.fail("Expected exception not thrown")
        } catch (e: Exception) {
            verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
        }
    }

    @Test
    fun `should not fail if transitioning to the same state`() {
        every { taskRepository.getTaskById(task.id.toString()) } returns task
        every { stateRepository.getStateById(oldState.id.toString()) } returns oldState

        service.changeTaskState(task.id.toString(), oldState.id.toString(), user)

        verify(exactly = 0) { taskRepository.updateTask(any()) }
        verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }

}
