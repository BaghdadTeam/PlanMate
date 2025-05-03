package logic.usecase.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.task.GetTaskByIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class GetTaskByIdUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase

    @BeforeEach
    fun setUp() {
        taskRepository = mockk(relaxed = true)
        getTaskByIdUseCase = GetTaskByIdUseCase(taskRepository)
    }

    @Test
    fun `invoke should return task from repository`() {
        val task = TaskEntityTestData.normalTask
        val id = task.id

        every { taskRepository.getTaskById(id.toString()) } returns task

        val result = getTaskByIdUseCase(id)

        assertThat(result).isEqualTo(task)
        verify { taskRepository.getTaskById(id.toString()) }
    }

    @Test
    fun `invoke should throw exception when repository throws`() {
        val id = UUID.randomUUID()
        every { taskRepository.getTaskById(id.toString()) } throws NoSuchElementException("Task not found")

        assertThrows<NoSuchElementException> {
            getTaskByIdUseCase(id)
        }
        verify { taskRepository.getTaskById(id.toString()) }
    }
}