package logic.usecase.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.Called
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
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
import java.util.UUID
import kotlin.jvm.java
import kotlin.test.Test

class CreateTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var createTaskUseCase: CreateTaskUseCase
    private lateinit var userRepository: UserRepository
    private val user = UserEntity(
        name = "Youssef Mohamed",
        username = "Pixelise",
        hashedPassword = "jd12d1sad",
        type = UserType.Mate
    )

    @BeforeEach
    fun setup() {
        taskRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        createTaskUseCase = CreateTaskUseCase(taskRepository, auditRepository, userRepository)
    }

    @Test
    fun `should create task and log audit when task is valid`() {
        val task = TaskEntityTestData.normalTask

        createTaskUseCase(task, user.id)

        verify { taskRepository.createTask(task) }

        val auditSlot = slot<AuditLogEntity>()
        verify { auditRepository.addAuditEntry(capture(auditSlot)) }

        val audit = auditSlot.captured
        assertThat(audit.entityUnderAudit).isEqualTo("Task")
        assertThat(audit.entityId).isInstanceOf(UUID::class.java) // assuming ID is auto-generated or assigned
        assertThat(audit.action).isEqualTo("created task ${task.title}")
    }

    @Test
    fun `should throw TaskWithMissingTitleException when title is blank`() {
        val task = TaskEntityTestData.taskWithBlankTitle()

        val exception = assertThrows<TaskWithMissingTitleException> {
            createTaskUseCase(task, user.id)
        }

        assertThat(exception).hasMessageThat().contains("title cannot be empty")
        verify { taskRepository wasNot Called }
        verify { auditRepository wasNot Called }
    }

    @Test
    fun `should throw TaskWithMissingDescriptionException when description is blank`() {
        val task = TaskEntityTestData.taskWithBlankDescription()

        val exception = assertThrows<TaskWithMissingDescriptionException> {
            createTaskUseCase(task, user.id)
        }

        assertThat(exception).hasMessageThat().contains("description cannot be empty")
        verify { taskRepository wasNot Called }
        verify { auditRepository wasNot Called }
    }
}