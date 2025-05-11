package presentation.audit
import io.mockk.*
import org.baghdad.presentation.audit.AuditUI
import org.baghdad.presentation.audit.ShowAuditByProjectIdUI
import org.baghdad.presentation.audit.ShowAuditByTaskIdUI
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.task.GetTasksByProjectIdUI
import org.junit.jupiter.api.*
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuditUITest {

    private lateinit var showAuditByProjectIdUI: ShowAuditByProjectIdUI
    private lateinit var showAuditByTaskIdUI: ShowAuditByTaskIdUI
    private lateinit var getTasksByProjectIdUI: GetTasksByProjectIdUI
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var auditUI: AuditUI

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        showAuditByProjectIdUI = mockk(relaxed = true)
        showAuditByTaskIdUI = mockk(relaxed = true)
        getTasksByProjectIdUI = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)

        auditUI = AuditUI(
            showAuditByProjectIdUI,
            showAuditByTaskIdUI,
            getTasksByProjectIdUI,
            viewer,
            reader
        )

        mockkStatic("kotlin.io.ConsoleKt")
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic("kotlin.io.ConsoleKt")
    }

    @Test
    fun `should show project audit when option 1 is selected`() {
        // Given
        val projectId = UUID.randomUUID()
        every { readln() } returnsMany listOf("1", "0")

        // When
        auditUI(projectId)

        // Then
        verify(exactly = 1) { showAuditByProjectIdUI.execute(projectId) }
        verify { viewer.logMessage(match { it.contains("Audit UI") }) }
    }

    @Test
    fun `should show task audit when a valid task index is selected`() {
        // Given
        val projectId = UUID.randomUUID()
        val taskId = UUID.randomUUID()
        every { readln() } returnsMany listOf("2", "0")
        every { reader.readInput() } returns "1" // Select task 1
        every { getTasksByProjectIdUI.execute(projectId) } returns listOf(taskId)

        // When
        auditUI(projectId)

        // Then
        verify(exactly = 1) { showAuditByTaskIdUI.execute(taskId) }
        verify { viewer.logMessage("Select a task by number:") }
    }

    @Test
    fun `should log error when invalid task UUID is entered`() {
        // Given
        val projectId = UUID.randomUUID()
        every { readln() } returnsMany listOf("2", "0")
        every { getTasksByProjectIdUI.execute(projectId) } returns emptyList()
        // When
        auditUI(projectId)

        // Then
        verify { viewer.logMessage("=== Audit UI ===") }
        verify { viewer.logMessage("Selected Project ID: $projectId")}
        verify { viewer.logMessage("1. Show audit for this project") }
        verify { viewer.logMessage("2. Show audit by Task ID") }
        verify { viewer.logMessage("0. Back") }
        verify { viewer.logMessage("Enter your choice: ") }
        verify { viewer.logMessage("No tasks available to audit.") }

    }

    @Test
    fun `should log error message when invalid task UUID is entered`() {
        // Given
        val projectId = UUID.randomUUID()
        every { reader.readInput() } returns "not-a-uuid" // Invalid UUID format
        every { readln() } returnsMany listOf("2", "0")
        every { getTasksByProjectIdUI.execute(projectId) } returns listOf(projectId)
        // When
        auditUI(projectId)

        // Then
        verify { viewer.logMessage("=== Audit UI ===") }
        verify { viewer.logMessage("Selected Project ID: $projectId")}
        verify { viewer.logMessage("1. Show audit for this project") }
        verify { viewer.logMessage("2. Show audit by Task ID") }
        verify { viewer.logMessage("0. Back") }
        verify { viewer.logMessage("Enter your choice: ") }
        verify { viewer.logError("Invalid selection. Please enter a valid task number.") }
    }


    @Test
    fun `should return when option 0 is selected`() {
        // Given
        val projectId = UUID.randomUUID()
        every { readln() } returns "0"

        // When
        auditUI(projectId)

        // Then
        verify { viewer.logMessage("=== Audit UI ===") }
        verify(exactly = 0) { showAuditByProjectIdUI.execute(any()) }
        verify(exactly = 0) { showAuditByTaskIdUI.execute(any()) }
    }

    @Test
    fun `should show error for invalid menu option`() {
        // Given
        val projectId = UUID.randomUUID()
        every { readln() } returnsMany listOf("99", "0")

        // When
        auditUI(projectId)

        // Then
        verify { viewer.logError("Invalid choice. Please try again.") }
    }

    @Test
    fun `should log message when no tasks available to audit`() {
        // Given
        val projectId = UUID.randomUUID()
        every { readln() } returnsMany listOf("2", "0")
        every { getTasksByProjectIdUI.execute(projectId) } returns emptyList()

        // When
        auditUI(projectId)

        // Then
        verify { viewer.logMessage("No tasks available to audit.") }
        verify(exactly = 0) { showAuditByTaskIdUI.execute(any()) }
    }
}



