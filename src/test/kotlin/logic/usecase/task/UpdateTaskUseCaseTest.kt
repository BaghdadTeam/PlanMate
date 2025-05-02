package logic.usecase.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.TaskWithMissingDescriptionException
import org.baghdad.logic.model.exceptions.TaskWithMissingTitleException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.task.UpdateTaskUseCase
import org.baghdad.utils.getFormattedTimestamp
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.Test

class UpdateTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private lateinit var userRepository: UserRepository

    private val user = UserEntity(
        id = UUID.fromString("9d597711-f9fa-40ca-9f8e-94f59ae957c9"), // <-- Set explicitly
        name = "Youssef Mohamed",
        username = "Pixelise",
        hashedPassword = "hashedPassword",
        type = UserType.Mate,
    )

    @BeforeEach
    fun setUp() {
        taskRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        updateTaskUseCase = UpdateTaskUseCase(taskRepository, auditRepository, userRepository)
    }


    @Test
    fun `should update task and log audit entry when changes are detected`() {
        val task = TaskEntityTestData.normalTask.copy(title = "Changed")
        val oldTask = TaskEntityTestData.normalTask

        every { taskRepository.getTaskById(task.id.toString()) } returns oldTask
        every { taskRepository.updateTask(task) } returns true
        every { userRepository.getUserById(user.id.toString()) } returns user

        updateTaskUseCase.invoke(task, user.id.toString())

        val auditSlot = slot<AuditEntity>()
        verify { taskRepository.updateTask(task) }
        verify { auditRepository.addAuditEntry(capture(auditSlot)) }

        assertThat(auditSlot.captured.entityType).isEqualTo("Task")
        assertThat(auditSlot.captured.entityId).isEqualTo(task.id.toString())
        assertThat(auditSlot.captured.action).isEqualTo(
            "Task “${oldTask.title}” was updated: title changed from “${oldTask.title}” to “${task.title}”"
        )
        assertThat(auditSlot.captured.user.id).isEqualTo(user.id)
    }

     @Test
    fun `should update task but not log audit when no changes detected`() {
        val task = TaskEntityTestData.normalTask

        every { taskRepository.getTaskById(task.id.toString()) } returns task
        every { taskRepository.updateTask(task) } returns true

        updateTaskUseCase.invoke(task, user.id.toString())

        verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
        verify { taskRepository.updateTask(task) }
    }

    @Test
    fun `should throw exception when title is blank`() {
        val task = TaskEntityTestData.normalTask.copy(title = "   ")

        assertThrows<TaskWithMissingTitleException> {
            updateTaskUseCase.invoke(task, user.id.toString())
        }

        verify(exactly = 0) { taskRepository.updateTask(any()) }
        verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }

    @Test
    fun `should throw exception when description is blank`() {
        val task = TaskEntityTestData.normalTask.copy(description = "   ")

        assertThrows<TaskWithMissingDescriptionException> {
            updateTaskUseCase.invoke(task, user.id.toString())
        }

        verify(exactly = 0) { taskRepository.updateTask(any()) }
        verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }

    @Test
    fun `should throw exception when updateTask returns false`() {
        val task = TaskEntityTestData.normalTask.copy(title = "Changed")
        every { taskRepository.getTaskById(task.id.toString()) } returns TaskEntityTestData.normalTask
        every { taskRepository.updateTask(task) } returns false

        val exception = assertThrows<Exception> {
            updateTaskUseCase.invoke(task, user.id.toString())
        }

        assertThat(exception.message).isEqualTo("Failed to update task")
        verify { taskRepository.updateTask(task) }
        verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }
}
