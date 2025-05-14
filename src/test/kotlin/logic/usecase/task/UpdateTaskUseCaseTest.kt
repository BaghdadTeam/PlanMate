package logic.usecase.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.TaskWithMissingDescriptionException
import org.baghdad.logic.model.exceptions.TaskWithMissingTitleException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.task.UpdateTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class UpdateTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private lateinit var userRepository: UserRepository
    private val sessionManager: SessionManager = mockk()

    private val user = UserEntity(
        id = UUID.fromString("9d597711-f9fa-40ca-9f8e-94f59ae957c9"), // <-- Set explicitly
        name = "Youssef Mohamed",
        username = "Pixelise",
        type = UserType.Mate,
    )

    @BeforeEach
    fun setUp() {
        taskRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        updateTaskUseCase = UpdateTaskUseCase(taskRepository, auditRepository, userRepository,sessionManager,)
        coEvery { sessionManager.isAuthenticated() } returns true
    }
    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows <UnauthorizedException> { updateTaskUseCase.invoke(TaskEntityTestData.normalTask, UUID.randomUUID())  }
        }

    @Test
    fun `should update task and log audit entry when changes are detected`() = runTest {
        val task = TaskEntityTestData.normalTask.copy(title = "Changed")
        val oldTask = TaskEntityTestData.normalTask

        coEvery { taskRepository.getTaskById(task.id) } returns oldTask
        coEvery { taskRepository.updateTask(task) } returns true
        coEvery { userRepository.getUserById(user.id) } returns user

        updateTaskUseCase.invoke(task, user.id)

        val auditSlot = slot<AuditLogEntity>()
        coVerify { taskRepository.updateTask(task) }
        coVerify { auditRepository.addAuditEntry(capture(auditSlot)) }

        assertThat(auditSlot.captured.entityUnderAudit).isEqualTo("Task")
        assertThat(auditSlot.captured.description).isEqualTo(
            "Task “${oldTask.title}” was updated: title changed from “${oldTask.title}” to “${task.title}”"
        )
        assertThat(auditSlot.captured.userId).isEqualTo(user.id)
    }

    @Test
    fun `should update task but not log audit when no changes detected`() = runTest {
        val task = TaskEntityTestData.normalTask

        coEvery { taskRepository.getTaskById(task.id) } returns task
        coEvery { taskRepository.updateTask(task) } returns true

        updateTaskUseCase.invoke(task, user.id)

        coVerify(exactly = 0) { auditRepository.addAuditEntry(any()) }
        coVerify { taskRepository.updateTask(task) }
    }

    @Test
    fun `should throw exception when title is blank`() = runTest {
        val task = TaskEntityTestData.normalTask.copy(title = "   ")

        assertThrows<TaskWithMissingTitleException> {
            updateTaskUseCase.invoke(task, user.id)
        }

        coVerify(exactly = 0) { taskRepository.updateTask(any()) }
        coVerify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }

    @Test
    fun `should throw exception when description is blank`() = runTest {
        val task = TaskEntityTestData.normalTask.copy(description = "   ")

        assertThrows<TaskWithMissingDescriptionException> {
            updateTaskUseCase.invoke(task, user.id)
        }

        coVerify(exactly = 0) { taskRepository.updateTask(any()) }
        coVerify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }

    @Test
    fun `should throw exception when updateTask returns false`() = runTest {
        val task = TaskEntityTestData.normalTask.copy(title = "Changed")
        coEvery { taskRepository.getTaskById(task.id) } returns TaskEntityTestData.normalTask
        coEvery { taskRepository.updateTask(task) } returns false

        val exception = assertThrows<Exception> {
            updateTaskUseCase.invoke(task, user.id)
        }

        assertThat(exception.message).isEqualTo("Failed to update task")
        coVerify { taskRepository.updateTask(task) }
        coVerify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }
}
