package logic.usecase.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.TaskWithMissingDescriptionException
import org.baghdad.logic.model.exceptions.TaskWithMissingTitleException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.task.CreateTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class CreateTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var createTaskUseCase: CreateTaskUseCase
    private lateinit var userRepository: UserRepository
    private val user = UserEntity(
        name = "Youssef Mohamed",
        username = "Pixelise",
        type = UserType.Mate
    )
    private val sessionManager: SessionManager = mockk()

    @BeforeEach
    fun setup() {
        taskRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        createTaskUseCase = CreateTaskUseCase(taskRepository, auditRepository, userRepository,sessionManager)
        coEvery { sessionManager.isAuthenticated() } returns true
    }
    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows<Exception> {
            createTaskUseCase(TaskEntityTestData.normalTask, UUID.randomUUID())
        }
    }

    @Test
    fun `should create task and log audit when task is valid`() = runTest {
        val task = TaskEntityTestData.normalTask

        createTaskUseCase(task, user.id)

        coVerify { taskRepository.createTask(task) }

        val auditSlot = slot<AuditLogEntity>()
        coVerify { auditRepository.addAuditEntry(capture(auditSlot)) }

        val audit = auditSlot.captured
        assertThat(audit.entityUnderAudit).isEqualTo("Task")
        assertThat(audit.projectId).isInstanceOf(UUID::class.java) // assuming ID is auto-generated or assigned
        assertThat(audit.description).isEqualTo("created task ${task.title}")
    }

    @Test
    fun `should throw TaskWithMissingTitleException when title is blank`() = runTest {
        val task = TaskEntityTestData.taskWithBlankTitle()

        val exception = assertThrows<TaskWithMissingTitleException> {
            createTaskUseCase(task, user.id)
        }

        assertThat(exception).hasMessageThat().contains("title cannot be empty")
        verify { taskRepository wasNot Called }
        verify { auditRepository wasNot Called }
    }

    @Test
    fun `should throw TaskWithMissingDescriptionException when description is blank`() = runTest {
        val task = TaskEntityTestData.taskWithBlankDescription()

        val exception = assertThrows<TaskWithMissingDescriptionException> {
            createTaskUseCase(task, user.id)
        }

        assertThat(exception).hasMessageThat().contains("description cannot be empty")
        verify { taskRepository wasNot Called }
        verify { auditRepository wasNot Called }
    }
}