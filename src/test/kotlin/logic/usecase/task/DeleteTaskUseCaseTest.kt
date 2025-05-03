package logic.usecase.task

import helpers.task.TaskEntityTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
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
    fun `should delete task and log audit`() {
        val task = TaskEntityTestData.normalTask
        val taskId = task.id.toString()

        every { taskRepository.getTaskById(taskId) } returns task
        every { userRepository.getUserById(user.id) } returns user

        deleteTaskUseCase(taskId, user.id)

        verifySequence {
            taskRepository.getTaskById(taskId)
            taskRepository.deleteTask(taskId)
            userRepository.getUserById(user.id)
            auditRepository.addAuditEntry(match {
                it.entityType == Entities.Task.name &&
                        it.entityId == taskId &&
                        it.user == user &&
                        it.action == "has been deleted task ${task.title}"
            })
        }
    }

}