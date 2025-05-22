package presentation.audit

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.presentation.audit.AuditManagementUI
import org.baghdad.presentation.audit.ShowAuditByProjectIdUI
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuditManagementUITest {

    private lateinit var showAuditByProjectIdUI: ShowAuditByProjectIdUI
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var auditManagementUI: AuditManagementUI

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        showAuditByProjectIdUI = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)

        auditManagementUI = AuditManagementUI(
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
        auditManagementUI(projectId)

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
        auditManagementUI(projectId)

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
        auditManagementUI(projectId)

        // Then
        verify { viewer.logError("Invalid choice. Please try again.") }
    }


}



