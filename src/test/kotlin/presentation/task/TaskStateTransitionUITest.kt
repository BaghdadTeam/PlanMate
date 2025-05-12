package presentation.task

import io.mockk.*
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.NotFoundException
import org.baghdad.logic.usecase.task.TaskStateTransitionUseCase
import org.baghdad.presentation.task.TaskStateTransitionUI
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class TaskStateTransitionUITest {

    private lateinit var useCase: TaskStateTransitionUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var session: SessionManager
    private lateinit var ui: TaskStateTransitionUI

    private val testUserId = UUID.randomUUID()

    private lateinit var task: TaskEntity
    private lateinit var state: StateEntity

    @BeforeEach
    fun setUp() {
        useCase = mockk()
        viewer = mockk(relaxed = true)
        reader = mockk()
        session = mockk(relaxed = true)

        every { session.currentSession.userId } returns testUserId

        ui = TaskStateTransitionUI(useCase, session, viewer, reader)

        val projectId = UUID.randomUUID()
        val creatorId = UUID.randomUUID()
        task = TaskEntity(UUID.randomUUID(), "Sample Task", "A sample task", UUID.randomUUID(), projectId, creatorId)
        state = StateEntity(UUID.randomUUID(), "IN_PROGRESS", projectId, creatorId)
    }

    @Test
    fun `should log success message on successful state change`() {
        every { reader.readInput() } returnsMany listOf(task.id.toString(), state.id.toString())
        coEvery { useCase.changeTaskState(task.id, state.id, testUserId) } just Runs

        ui.execute(listOf(state), listOf(task))

        verify { viewer.logMessage("Task state changed successfully.") }
    }

    @Test
    fun `should log NotFoundException message if state not found in this project`() {
        every { reader.readInput() } returnsMany listOf(task.id.toString(), state.id.toString())
        coEvery { useCase.changeTaskState(task.id, state.id, testUserId) } throws NotFoundException("State not found")

        ui.execute(listOf(state), listOf(task))

        verify { viewer.logError("State not found in this project: State not found") }
    }

    @Test
    fun `should log IllegalStateException message if invalid operation`() {
        every { reader.readInput() } returnsMany listOf(task.id.toString(), state.id.toString())
        coEvery { useCase.changeTaskState(task.id, state.id, testUserId) } throws IllegalStateException("Not allowed")

        ui.execute(listOf(state), listOf(task))

        verify { viewer.logError("Invalid operation: Not allowed") }
    }

    @Test
    fun `should log unexpected error`() {
        val validTaskId = task.id.toString()  // Using valid UUID string
        val validStateId = state.id.toString()  // Using valid UUID string

        every { reader.readInput() } returnsMany listOf(validTaskId, validStateId)
        coEvery { useCase.changeTaskState(task.id, state.id, testUserId) } throws RuntimeException("Oops")

        ui.execute(listOf(state), listOf(task))

        verify { viewer.logError("Unexpected error: Oops") }
    }

    @Test
    fun `should trim and pass both taskId and newStateId correctly`() {
        every { reader.readInput() } returnsMany listOf(task.id.toString(), state.id.toString())
        coEvery { useCase.changeTaskState(task.id, state.id, testUserId) } just Runs

        ui.execute(listOf(state), listOf(task))

        coVerify { useCase.changeTaskState(task.id, state.id, testUserId) }
    }


    @Test
    fun `should throw exception if task ID is null`() {
        every { reader.readInput() } returnsMany listOf(null)

        assertThrows<Exception> {
            ui.execute(listOf(state), listOf(task))
        }
    }

    @Test
    fun `should throw exception if task ID is blank`() {
        every { reader.readInput() } returnsMany listOf("   ")

        assertThrows<Exception> {
            ui.execute(listOf(state), listOf(task))
        }
    }

    @Test
    fun `should throw exception if state ID is null`() {
        every { reader.readInput() } returnsMany listOf("1", null)

        assertThrows<Exception> {
            ui.execute(listOf(state), listOf(task))
        }
    }

    @Test
    fun `should throw exception if state ID is blank`() {
        every { reader.readInput() } returnsMany listOf("1", "   ")

        assertThrows<Exception> {
            ui.execute(listOf(state), listOf(task))
        }
    }

    @Test
    fun `should log general error if unknown exception occurs`() {
        val validTaskId = task.id.toString()
        val validStateId = state.id.toString()

        every { reader.readInput() } returnsMany listOf(validTaskId, validStateId)
        coEvery { useCase.changeTaskState(task.id, state.id, testUserId) } throws Exception("Generic")

        ui.execute(listOf(state), listOf(task))

        verify { viewer.logError(" something went wrong while trying to change task state.") }
    }

}