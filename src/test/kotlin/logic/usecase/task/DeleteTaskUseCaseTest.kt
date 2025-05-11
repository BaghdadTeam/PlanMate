package logic.usecase.task

import helpers.task.TaskEntityTestData
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.task.DeleteTaskUseCase
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class DeleteTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var auditRepository: AuditRepository
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var userRepository: UserRepository

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
        userRepository = mockk(relaxed = true)
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository, auditRepository, userRepository)
    }

    @Test
    fun `should delete task and log audit`() = runTest {
        val task = TaskEntityTestData.normalTask
        val taskId = task.id

        coEvery { taskRepository.getTaskById(taskId) } returns task
        coEvery { userRepository.getUserById(user.id) } returns user

        deleteTaskUseCase(taskId, user.id)

        coVerifySequence {
            taskRepository.getTaskById(taskId)
            taskRepository.deleteTask(taskId)
            userRepository.getUserById(user.id)
            auditRepository.addAuditEntry(match { audit ->
                audit.entityUnderAudit == Entities.Task.name &&
                        audit.projectId == task.projectId &&
                        audit.userId == user.id &&
                        audit.action == "has been deleted task ${task.title}"
            })
        }
    }
}