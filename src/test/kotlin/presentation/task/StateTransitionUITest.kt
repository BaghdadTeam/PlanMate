package presentation.task

import io.mockk.*
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.exceptions.StateExceptions
import org.baghdad.logic.usecase.StateTransitionUseCase
import org.baghdad.presentation.StateTransitionUI
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class StateTransitionUITest {

    private lateinit var useCase: StateTransitionUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var session: SessionManager
    private lateinit var ui: StateTransitionUI

    private val testUserId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        useCase = mockk()
        viewer = mockk(relaxed = true)
        reader = mockk()
        session = mockk(relaxed = true)

        // Mock the session to return the test user ID
        every { session.currentSession.userId } returns testUserId

        ui = StateTransitionUI(useCase, session, viewer, reader)
    }

    @Test
    fun `should log success message on successful state change`() {
        every { reader.readInput() } returnsMany listOf(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        )
        coEvery { useCase.changeTaskState(any(), any(), testUserId) } just Runs

        ui.execute()

        verify { viewer.logMessage("Task state changed successfully.") }
    }

    @Test
    fun `should log NotFoundException message if State not found in this project`() {
        every { reader.readInput() } returnsMany listOf(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        )
        coEvery {
            useCase.changeTaskState(any(), any(), testUserId)
        } throws StateExceptions.NotFoundException("State not found")

        ui.execute()

        verify { viewer.logError("State not found in this project: State not found") }
    }

    @Test
    fun `should log IllegalStateException message if invalid operation`() {
        every { reader.readInput() } returnsMany listOf(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        )
        coEvery {
            useCase.changeTaskState(any(), any(), testUserId)
        } throws IllegalStateException("Not allowed")

        ui.execute()

        verify { viewer.logError("Invalid operation: Not allowed") }
    }

    @Test
    fun `should log unexpected error`() {
        every { reader.readInput() } returnsMany listOf(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        )
        coEvery {
            useCase.changeTaskState(any(), any(), testUserId)
        } throws RuntimeException("Something went wrong")

        ui.execute()

        verify { viewer.logError("Unexpected error: Something went wrong") }
    }

    @Test
    fun `should trim and pass both taskId and newStateId correctly`() {
        val taskId = UUID.randomUUID()
        val newStateId = UUID.randomUUID()
        every { reader.readInput() } returnsMany listOf(taskId.toString(), newStateId.toString())
        coEvery { useCase.changeTaskState(any(), any(), testUserId) } just Runs

        ui.execute()

        coVerify { useCase.changeTaskState(taskId, newStateId, testUserId) }
    }

    @Test
    fun `should throw exception if task ID is null`() {
        every { reader.readInput() } returnsMany listOf(null)

        assertThrows<Exception> { ui.execute() }
    }

    @Test
    fun `should throw exception if task ID is blank`() {
        every { reader.readInput() } returnsMany listOf("   ")

        assertThrows<Exception> { ui.execute() }
    }

    @Test
    fun `should throw exception if state ID is null`() {
        every { reader.readInput() } returnsMany listOf("task123", null)

        assertThrows<Exception> { ui.execute() }
    }

    @Test
    fun `should throw exception if state ID is blank`() {
        every { reader.readInput() } returnsMany listOf("task123", "   ")

        assertThrows<Exception> { ui.execute() }
    }

    @Test
    fun `should log general error if unknown exception occurs`() {
        val taskId = UUID.randomUUID().toString()
        val newStateId = UUID.randomUUID().toString()

        every { reader.readInput() } returnsMany listOf(taskId, newStateId)
        coEvery {
            useCase.changeTaskState(any(), any(), testUserId)
        } throws Exception("Generic error")

        ui.execute()

        verify { viewer.logError("Something went wrong while trying to change task state: Generic error") }
    }
}
