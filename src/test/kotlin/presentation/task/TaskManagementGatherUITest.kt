package presentation.task

import io.mockk.*
import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.usecase.ViewServiceUseCase
import org.baghdad.presentation.task.TaskStateTransitionUI
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.task.CreateTaskUI
import org.baghdad.presentation.task.DeleteTaskUI
import org.baghdad.presentation.task.TaskManagementGatherUI
import org.baghdad.presentation.task.UpdateTaskUI
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class TaskManagementGatherUITest {

    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var createTaskUI: CreateTaskUI
    private lateinit var editTaskUI: UpdateTaskUI
    private lateinit var deleteTaskUI: DeleteTaskUI
    private lateinit var changeTaskStateUI: TaskStateTransitionUI
    private lateinit var viewServiceUseCase: ViewServiceUseCase
    private lateinit var ui: TaskManagementGatherUI

    private val projectId = UUID.randomUUID()
    private val creatorId = UUID.randomUUID()

    private lateinit var task1: TaskEntity
    private lateinit var task2: TaskEntity
    private lateinit var state: TaskStateEntity

    @BeforeEach
    fun setUp() {
        viewer = mockk(relaxed = true)
        reader = mockk()
        createTaskUI = mockk(relaxed = true)
        editTaskUI = mockk(relaxed = true)
        deleteTaskUI = mockk(relaxed = true)
        changeTaskStateUI = mockk(relaxed = true)

        ui = TaskManagementGatherUI(
            viewer,
            reader,
            createTaskUI,
            editTaskUI,
            deleteTaskUI,
            changeTaskStateUI,
        )

        task1 =
            TaskEntity(UUID.randomUUID(), "Task 1", "Description 1", UUID.randomUUID(), projectId, UUID.randomUUID())
        task2 =
            TaskEntity(UUID.randomUUID(), "Task 2", "Description 2", UUID.randomUUID(), projectId, UUID.randomUUID())
        state = TaskStateEntity(UUID.randomUUID(), "IN_PROGRESS", projectId, UUID.randomUUID())
    }

    @Test
    fun `should display task management options and handle invalid input`() {
        every { reader.readInput() } returnsMany listOf("5")
        val state = TaskStateEntity(UUID.randomUUID(), "To Do", projectId, creatorId)
        val task = TaskEntity(
            id = UUID.randomUUID(),
            title = "Task 1",
            description = "Some description",
            stateId = state.id,
            projectId = projectId,
            creatorId = creatorId
        )
        val project = mapOf(state to listOf(task))
        ui.execute(project , projectId)

        verifySequence {
            viewer.logMessage("=== Task Management ===")
            viewer.logMessage("1. Create Task")
            viewer.logMessage("2. Edit Task")
            viewer.logMessage("3. Delete Task")
            viewer.logMessage("4. Change Task State")
            viewer.logMessage("0. Back to Previous Screen")
            viewer.logMessage("Enter your choice: ")
            reader.readInput()
            viewer.logError("Invalid choice. Please try again.")
        }
    }


    @Test
    fun `should call CreateTaskUI when option 1 is selected`() {
        every { reader.readInput() } returns "1"
        val state = TaskStateEntity(UUID.randomUUID(), "To Do", projectId, creatorId)
        val task = TaskEntity(
            id = UUID.randomUUID(),
            title = "Task 1",
            description = "Some description",
            stateId = state.id,
            projectId = projectId,
            creatorId = creatorId
        )
        val project = mapOf(state to listOf(task))
        ui.execute(project , projectId)

        verify { createTaskUI.execute(projectId) }
    }

    @Test
    fun `should call EditTaskUI when option 2 is selected`() {
        every { reader.readInput() } returns "2"

        val state = TaskStateEntity(UUID.randomUUID(), "To Do", projectId, creatorId)
        val task = TaskEntity(
            id = UUID.randomUUID(),
            title = "Task 1",
            description = "Some description",
            stateId = state.id,
            projectId = projectId,
            creatorId = creatorId
        )
        val project = mapOf(state to listOf(task))
        ui.execute(project , projectId)

        // Verify that EditTaskUI is executed with the list of tasks
        verify { editTaskUI.execute(any()) }
    }

    @Test
    fun `should call DeleteTaskUI when option 3 is selected`() {
        every { reader.readInput() } returns "3"
        val state = TaskStateEntity(UUID.randomUUID(), "To Do", projectId, creatorId)
        val task = TaskEntity(
            id = UUID.randomUUID(),
            title = "Task 1",
            description = "Some description",
            stateId = state.id,
            projectId = projectId,
            creatorId = creatorId
        )
        val project = mapOf(state to listOf(task))
        ui.execute(project , projectId)

        verify { deleteTaskUI.execute(any()) }
    }

    @Test
    fun `should call ChangeTaskStateUI when option 4 is selected`() {
        every { reader.readInput() } returns "4"

        val state = TaskStateEntity(UUID.randomUUID(), "To Do", projectId, creatorId)
        val task = TaskEntity(
            id = UUID.randomUUID(),
            title = "Task 1",
            description = "Some description",
            stateId = state.id,
            projectId = projectId,
            creatorId = creatorId
        )
        val project = mapOf(state to listOf(task))
        ui.execute(project , projectId)

        // Verify that StateTransitionUI is executed with the expected arguments
        verify { changeTaskStateUI.execute(any(), any()) }
    }
}