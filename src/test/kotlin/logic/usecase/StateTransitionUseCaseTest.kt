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
        every { stateRepository.getStateById(newState.id.toString()) } returns newState
        every { taskRepository.updateTask(any()) } returns true
        every { auditRepository.addAuditEntry(any()) } returns true

        val result = service.changeTaskState(task.id.toString(), newState.id.toString(), user)

        Assertions.assertTrue(result.isSuccess)
        verify { taskRepository.updateTask(match { it.stateId == newState.id.toString() }) }
        verify { auditRepository.addAuditEntry(any()) }
    }

    @Test
    fun `should fail if new state not found`() {
        every { taskRepository.getTaskById(task.id.toString()) } returns task
        every { stateRepository.getStateById(any()) } returns null

        val result = service.changeTaskState(task.id.toString(), "invalid-state", user)

        Assertions.assertTrue(result.isFailure)
        verify(exactly = 0) { taskRepository.updateTask(any()) }
    }

    @Test
    fun `should fail if new state belongs to different project`() {
        val otherProjectState = newState.copy(projectId = UUID.randomUUID().toString())
        every { taskRepository.getTaskById(task.id.toString()) } returns task
        every { stateRepository.getStateById(otherProjectState.id.toString()) } returns otherProjectState

        val result =
            service.changeTaskState(task.id.toString(), otherProjectState.id.toString(), user)

        Assertions.assertTrue(result.isFailure)
        verify(exactly = 0) { taskRepository.updateTask(any()) }
    }

    @Test
    fun `should fail if task update fails`() {
        every { taskRepository.getTaskById(task.id.toString()) } returns task
        every { stateRepository.getStateById(newState.id.toString()) } returns newState
        every { taskRepository.updateTask(any()) } returns false

        val result = service.changeTaskState(task.id.toString(), newState.id.toString(), user)

        Assertions.assertTrue(result.isFailure)
        verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }

    @Test
    fun `should update task state and log audit if state exists in same project`() {
    }

    @Test
    fun `should fail if state does not belong to task's project`() {
    }

    @Test
    fun `should fail if task or state does not exist`() {
    }

}
