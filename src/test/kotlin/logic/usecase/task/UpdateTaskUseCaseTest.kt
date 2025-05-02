package logic.usecase.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.TaskWithMissingDescriptionException
import org.baghdad.logic.model.exceptions.TaskWithMissingTitleException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.task.UpdateTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class UpdateTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var updateTaskUseCase: UpdateTaskUseCase

    private val user = UserEntity(
        name = "Youssef Mohamed",
        username = "Pixelise",
        hashedPassword = "hashedPassword",
        type = UserType.Mate,
    )

    @BeforeEach
    fun setUp() {
        taskRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        updateTaskUseCase = UpdateTaskUseCase(taskRepository, auditRepository)
    }

    @Test
    fun `should update task and log audit when title or description changes`() {
        val oldTask = TaskEntityTestData.normalTask
        val updatedTask = oldTask.copy(title = "Updated Title", description = "Updated Description")

        every { taskRepository.getTaskById(oldTask.id.toString()) } returns oldTask
        every { taskRepository.updateTask(updatedTask) } returns true

        updateTaskUseCase(updatedTask, user)

        verify {
            auditRepository.addAuditEntry(match {
                it.entityId == updatedTask.id.toString() &&
                        it.user == user &&
                        it.action.contains("title changed") &&
                        it.action.contains("description changed")
            })
        }

        verify { taskRepository.updateTask(updatedTask) }
    }

    @Test
    fun `should update task but not log audit when no changes detected`() {
        val task = TaskEntityTestData.normalTask

        every { taskRepository.getTaskById(task.id.toString()) } returns task
        every { taskRepository.updateTask(task) } returns true

        updateTaskUseCase(task, user)

        verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
        verify { taskRepository.updateTask(task) }
    }

    @Test
    fun `should throw exception when title is blank`() {
        val task = TaskEntityTestData.normalTask.copy(title = "   ")

        assertThrows<TaskWithMissingTitleException> {
            updateTaskUseCase(task, user)
        }

        verify(exactly = 0) { taskRepository.updateTask(any()) }
        verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }

    @Test
    fun `should throw exception when description is blank`() {
        val task = TaskEntityTestData.normalTask.copy(description = "   ")

        assertThrows<TaskWithMissingDescriptionException> {
            updateTaskUseCase(task, user)
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
            updateTaskUseCase(task, user)
        }

        assertThat(exception.message).isEqualTo("Failed to update task")
        verify { taskRepository.updateTask(task) }
        verify(exactly = 0) { auditRepository.addAuditEntry(any()) }
    }
}
