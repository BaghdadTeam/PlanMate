package data.repository.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.every
import io.mockk.mockk
import org.baghdad.data.repository.task.TaskRepositoryImpl
import org.baghdad.logic.entities.TaskEntity
import org.baghdad.logic.storage.task.TaskStorage
import org.baghdad.utils.customizedExceptions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class TaskRepositoryImplTest {

    private lateinit var taskRepository: TaskRepositoryImpl
    private lateinit var storage: TaskStorage

    @BeforeEach
    fun setup() {
        storage = mockk(relaxed = true)
        taskRepository = TaskRepositoryImpl(storage)
    }

    // region :: Create Task Region ::

    @Test
    fun `should return true if the task is created correctly and saved`() {
        // Given
        val taskItem = TaskEntityTestData.normalTask()
        every { storage.save(taskItem) } returns true

        // When
        val result = taskRepository.createTask(taskItem)

        // Then
        assertThat(result).isTrue()
    }

    @ParameterizedTest
    @MethodSource("provideTasksWithInvalidTitle")
    fun `should throw TaskMissingTitleException if the task title is empty`(taskItem: TaskEntity) {

        // When & Then
        assertThrows<TaskMissingTitleException> {
            taskRepository.createTask(taskItem)
        }
    }

    @ParameterizedTest
    @MethodSource("provideTasksWithInvalidDescription")
    fun `should throw TaskMissingDescriptionException if the task description is empty or blank`(taskItem: TaskEntity) {

        // When & Then
        assertThrows<TaskMissingDescriptionException> {
            taskRepository.createTask(taskItem)
        }
    }

    @ParameterizedTest
    @MethodSource("provideTasksWithInvalidStateId")
    fun `should throw TaskMissingStateIdException if the task description is empty or blank`(taskItem: TaskEntity) {

        // When & Then
        assertThrows<TaskMissingStateIdException> {
            taskRepository.createTask(taskItem)
        }
    }


    @ParameterizedTest
    @MethodSource("provideTasksWithInvalidProjectId")
    fun `should throw TaskMissingProjectIdException if the task description is empty or blank`(taskItem: TaskEntity) {

        // When & Then
        assertThrows<TaskMissingProjectIdException> {
            taskRepository.createTask(taskItem)
        }
    }

    @ParameterizedTest
    @MethodSource("provideTasksWithInvalidCreatorId")
    fun `should throw TaskMissingCreatorIdException if the task description is empty or blank`(taskItem: TaskEntity) {

        // When & Then
        assertThrows<TaskMissingCreatorIdException> {
            taskRepository.createTask(taskItem)
        }
    }

    // endregion

    // region :: getTaskById ::

    @Test
    fun `should return task entity if matches the input id`() {
        // Given
        val taskItem = TaskEntityTestData.normalTask()
        every { storage.getAll() } returns listOf(taskItem)
        val taskId = taskItem.id

        // When
        val result = taskRepository.getTaskById(taskId.toString())

        assertThat(result).isEqualTo(taskItem)
    }

    // endregion

    companion object {
        @JvmStatic
        fun provideTasksWithInvalidTitle(): Stream<TaskEntity> = Stream.of(
            TaskEntityTestData.taskWithEmptyTitle(),
            TaskEntityTestData.taskWithBlankTitle()
        )

        @JvmStatic
        fun provideTasksWithInvalidDescription(): Stream<TaskEntity> = Stream.of(
            TaskEntityTestData.taskWithEmptyDescription(),
            TaskEntityTestData.taskWithBlankDescription()
        )

        @JvmStatic
        fun provideTasksWithInvalidStateId(): Stream<TaskEntity> = Stream.of(
            TaskEntityTestData.taskWithBlankStateId(),
            TaskEntityTestData.taskWithEmptyStateId()
        )

        @JvmStatic
        fun provideTasksWithInvalidProjectId(): Stream<TaskEntity> = Stream.of(
            TaskEntityTestData.taskWithBlankProjectId(),
            TaskEntityTestData.taskWithEmptyProjectId()
        )

        @JvmStatic
        fun provideTasksWithInvalidCreatorId(): Stream<TaskEntity> = Stream.of(
            TaskEntityTestData.taskWithBlankCreatorId(),
            TaskEntityTestData.taskWithEmptyCreatorId()
        )
    }
}