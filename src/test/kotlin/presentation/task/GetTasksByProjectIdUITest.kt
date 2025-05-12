package presentation.task

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.TasksNotFoundException
import org.baghdad.logic.usecase.task.GetTasksByProjectIdUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.task.GetTasksByProjectIdUI
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test


class GetTasksByProjectIdUITest {

    private lateinit var getTasksByProjectIdUI: GetTasksByProjectIdUI
    private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader

    private val projectId = UUID.randomUUID()

    private val tasks = listOf(
        TaskEntity(UUID.randomUUID(), "Task 1", "Description 1", UUID.randomUUID(), projectId, UUID.randomUUID()),
        TaskEntity(UUID.randomUUID(), "Task 2", "Description 2", UUID.randomUUID(), projectId, UUID.randomUUID())
    )

    private val emptyProjectId = ""

    @BeforeEach
    fun setup() {
        getTasksByProjectIdUseCase = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk()

        getTasksByProjectIdUI = GetTasksByProjectIdUI(getTasksByProjectIdUseCase, viewer, reader)
    }

    @Test
    fun `test valid project ID input with tasks found`() {
        every { reader.readInput() } returns projectId.toString() // User enters valid project ID
        coEvery { getTasksByProjectIdUseCase(projectId) } returns tasks

        getTasksByProjectIdUI.execute(projectId)

        verify { viewer.logMessage("Tasks for project $projectId:") }
        tasks.forEachIndexed { index, task ->
            verify { viewer.logMessage("${index + 1}. ${task.title} - ${task.description}") }
        }
    }

    @Test
    fun `test valid project ID input with no tasks found`() {
        every { reader.readInput() } returns projectId.toString() // User enters valid project ID
        coEvery { getTasksByProjectIdUseCase(projectId) } returns emptyList()

        getTasksByProjectIdUI.execute(projectId)

        verify { viewer.logMessage("No tasks found for the given project.") }
    }

    @Test
    fun `test exception handling when tasks not found`() {
        every { reader.readInput() } returns projectId.toString() // User enters valid project ID
        coEvery { getTasksByProjectIdUseCase(projectId) } throws TasksNotFoundException("No tasks found")

        getTasksByProjectIdUI.execute(projectId)

        verify { viewer.logMessage("No tasks found for project ID: $projectId") }
    }

    @Test
    fun `test exception handling for other errors`() {
        every { reader.readInput() } returns projectId.toString() // User enters valid project ID
        coEvery { getTasksByProjectIdUseCase(projectId) } throws Exception("Unexpected error")

        getTasksByProjectIdUI.execute(projectId)

        verify { viewer.logMessage("Failed to get tasks: Unexpected error") }
    }

}