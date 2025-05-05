package presentation

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.exceptions.StateExceptions.NotFoundException
import org.baghdad.logic.usecase.StateTransitionUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.task.StateTransitionUI
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.Test

class StateTransitionUITest {

    private lateinit var useCase: StateTransitionUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var ui: StateTransitionUI

    @BeforeEach
    fun setUp() {
        useCase = mockk()
        viewer = mockk(relaxed = true)
        reader = mockk()
        ui = StateTransitionUI(useCase, viewer, reader)
    }

    @Test
    fun `should log success message on successful state change`() {
        every { reader.readInput() } returnsMany listOf(UUID.randomUUID().toString(), UUID.randomUUID().toString())
        every { useCase.changeTaskState(any(), any(), any()) } just Runs

        ui.execute()

        verify { viewer.logMessage("Task state changed successfully.") }
    }

    @Test
    fun `should log NotFoundException message if State not found in this project`() {
        every { reader.readInput() } returnsMany listOf(UUID.randomUUID().toString(), UUID.randomUUID().toString())
        every {
            useCase.changeTaskState(
                any(),
                any(),
                any()
            )
        } throws NotFoundException("State not found")

        ui.execute()

        verify { viewer.logError("State not found in this project: State not found") }
    }

    @Test
    fun `should log IllegalStateException message if invalid operation`() {
        every { reader.readInput() } returnsMany listOf(UUID.randomUUID().toString(), UUID.randomUUID().toString())
        every {
            useCase.changeTaskState(
                any(),
                any(),
                any()
            )
        } throws IllegalStateException("Not allowed")

        ui.execute()

        verify { viewer.logError("Invalid operation: Not allowed") }
    }

    @Test
    fun `should log unexpected error`() {
        every { reader.readInput() } returnsMany listOf(UUID.randomUUID().toString(), UUID.randomUUID().toString())
        every {
            useCase.changeTaskState(
                any(),
                any(),
                any()
            )
        } throws RuntimeException("Something went wrong")

        ui.execute()

        verify { viewer.logError("Unexpected error: Something went wrong") }
    }

    @Test
    fun `should trim and pass both taskId and newStateId correctly`() {
        val taskId = UUID.randomUUID()
        val newStateId = UUID.randomUUID()
        every { reader.readInput() } returnsMany listOf(taskId.toString(), newStateId.toString())
        every { useCase.changeTaskState(any(), any(), any()) } just Runs

        ui.execute()

        verify { useCase.changeTaskState(taskId, newStateId, any()) }
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
}