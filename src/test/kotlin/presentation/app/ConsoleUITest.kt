package presentation.app

import io.mockk.every
import io.mockk.mockk
import org.baghdad.presentation.app.ConsoleUI
import org.baghdad.presentation.output.Viewer
import io.mockk.verify
import org.baghdad.presentation.app.Feature
import org.baghdad.presentation.input.Reader
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class ConsoleUITest {
    private lateinit var feature1: Feature
    private lateinit var feature2: Feature
    private lateinit var consoleUI: ConsoleUI

    private lateinit var viewer: Viewer
    private lateinit var reader: Reader

    @BeforeEach
    fun setUp() {
        feature1 = mockk(relaxed = true)
        feature2 = mockk(relaxed = true)

        every { feature1.id } returns 1
        every { feature1.name } returns "Feature One"
        every { feature2.id } returns 2
        every { feature2.name } returns "Feature Two"

        viewer = mockk(relaxed = true)
        reader = mockk()

        consoleUI = ConsoleUI(
            mapOf(1 to feature1, 2 to feature2), viewer, reader
        )
    }

    @Test
    fun `should print the welcome message and exit immediately on 0 input`() {
        every { reader.readInput() } returns "0"
        consoleUI.start()
        verify { viewer.logMessage("Welcome!") }
        verify { viewer.logMessage("Exiting... Goodbye!") }
    }

    @Test
    fun `should execute the selected feature`() {
        every { reader.readInput() } returnsMany listOf("1", "0")
        consoleUI.start()
        verify { feature1.execute() }
    }

    @Test
    fun `should exit when user input is null`() {
        every { reader.readInput() } returns null
        consoleUI.start()
        verify(exactly = 0) { feature1.execute() }
        verify(exactly = 0) { feature2.execute() }
        verify { viewer.logMessage("Exiting... Goodbye!") }
    }

    @Test
    fun `should handle invalid option`() {
        every { reader.readInput() } returnsMany listOf("3", "0")
        consoleUI.start()
        verify { viewer.logError("Invalid input, please try again.") }
    }
}