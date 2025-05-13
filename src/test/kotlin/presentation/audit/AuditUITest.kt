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
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var auditUI: AuditUI

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        showAuditByProjectIdUI = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)

        auditUI = AuditUI(
            showAuditByProjectIdUI,
            viewer,
            reader
        )

    }

    @Test
    fun `should show project audit when option 1 is selected`() {
        // Given
        val projectId = UUID.randomUUID()
        every { reader.readInput() } returnsMany listOf("1", "0")

        // When
        auditUI(projectId)

        // Then
        verify(exactly = 1) { showAuditByProjectIdUI.execute(projectId) }
        verify { viewer.logMessage(match { it.contains("Audit UI") }) }
    }





    @Test
    fun `should return when option 0 is selected`() {
        // Given
        val projectId = UUID.randomUUID()
        every { reader.readInput() } returns "0"

        // When
        auditUI(projectId)

        // Then
        verify { viewer.logMessage("=== Audit UI ===") }
        verify(exactly = 0) { showAuditByProjectIdUI.execute(any()) }
    }

    @Test
    fun `should show error for invalid menu option`() {
        // Given
        val projectId = UUID.randomUUID()
        every { reader.readInput() } returnsMany listOf("99", "0")

        // When
        auditUI(projectId)

        // Then
        verify { viewer.logError("Invalid choice. Please try again.") }
    }


}



