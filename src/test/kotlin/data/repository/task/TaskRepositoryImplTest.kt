package data.repository.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.every
import io.mockk.mockk
import org.baghdad.data.repository.task.TaskRepositoryImpl
import org.baghdad.data.storage.task.TaskStorage
import org.baghdad.logic.entities.TaskEntity
import org.baghdad.utils.customizedExceptions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import java.util.UUID
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
        val exception = assertThrows<TaskMissingTitleException> {
            taskRepository.createTask(taskItem)
        }
        assertThat(exception.message).isEqualTo("The task shouldn't have an empty title")
    }

    @ParameterizedTest
    @MethodSource("provideTasksWithInvalidDescription")
    fun `should throw TaskMissingDescriptionException if the task description is empty or blank`(taskItem: TaskEntity) {

        // When & Then
        val exception = assertThrows<TaskMissingDescriptionException> {
            taskRepository.createTask(taskItem)
        }
        assertThat(exception.message).isEqualTo("The task shouldn't have an empty Description")
    }

    @ParameterizedTest
    @MethodSource("provideTasksWithInvalidStateId")
    fun `should throw TaskMissingStateIdException if the task description is empty or blank`(taskItem: TaskEntity) {

        // When & Then
        val exception = assertThrows<TaskMissingStateIdException> {
            taskRepository.createTask(taskItem)
        }
        assertThat(exception.message).isEqualTo("The task should be related to a state")
    }


    @ParameterizedTest
    @MethodSource("provideTasksWithInvalidProjectId")
    fun `should throw TaskMissingProjectIdException if the task description is empty or blank`(taskItem: TaskEntity) {

        // When & Then
        val exception = assertThrows<TaskMissingProjectIdException> {
            taskRepository.createTask(taskItem)
        }
        assertThat(exception.message).isEqualTo("The task should be related to a project")
    }

    @ParameterizedTest
    @MethodSource("provideTasksWithInvalidCreatorId")
    fun `should throw TaskMissingCreatorIdException if the task description is empty or blank`(taskItem: TaskEntity) {

        // When & Then
        val exception = assertThrows<TaskMissingCreatorIdException> {
            taskRepository.createTask(taskItem)
        }
        assertThat(exception.message).isEqualTo("The task should contain it's creator Id")
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

    @Test
    fun `should throw TaskNotFoundException if there is no task matches the id`() {
        // Given
        val taskItem = TaskEntityTestData.normalTask()
        every { storage.getAll() } returns listOf(taskItem)
        val randomId = UUID.randomUUID().toString()

        // When & Then
        val exception = assertThrows<TaskNotFoundException> {
            taskRepository.getTaskById(randomId)
        }
        assertThat(exception.message).isEqualTo("There is no task with the given ID")
    }

    @Test
    fun `should throw InvalidTaskIdException if the given id is not UUID`() {
        // Given
        val randomId = "let's goo"

        // When & Then
        val exception = assertThrows<InvalidTaskIdException> {
            taskRepository.getTaskById(randomId)
        }
        assertThat(exception.message).isEqualTo("The Given id is not of type UUID")
    }

    @ParameterizedTest
    @CsvSource("'',", "'   ',")
    fun `should throw TaskWithEmptyIDException if the given id is empty`(id: String) {
        // When & Then
        val exception = assertThrows<TaskWithEmptyIDException> {
            taskRepository.getTaskById(id)
        }
        assertThat(exception.message).isEqualTo("The id should not be empty or blank")
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