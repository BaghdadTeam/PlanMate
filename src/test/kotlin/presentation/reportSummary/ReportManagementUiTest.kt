package org.baghdad.presentation.reportSummary

import com.google.common.base.Verify.verify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class ReportManagementUiTest {
    private lateinit var reportUI: ReportUI
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var ui: ReportManagementUi

    @BeforeEach
    fun setup() {
        reportUI = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk()
        ui = ReportManagementUi(reportUI, viewer, reader)
    }

    @Test
    fun `should call viewReportCommand when user selects 1 then exit on 0`() {
        // Arrange
        every { reader.readInput() } returnsMany listOf("1", "0")

        // Act
        ui.showReportMenu()

        // Assert
        verifySequence {
            viewer.logMessage(match { it.contains("Report Management") })
            viewer.logMessage("1. View Project Summary Report")
            viewer.logMessage("0. Return to Main Menu")
            viewer.logMessage("========================================")
            viewer.logMessage("Enter your choice: ")
            reportUI.viewReportCommand()
            viewer.logMessage(match { it.contains("Report Management") })
            viewer.logMessage("1. View Project Summary Report")
            viewer.logMessage("0. Return to Main Menu")
            viewer.logMessage("========================================")
            viewer.logMessage("Enter your choice: ")
            viewer.logMessage("🔙 Returning to main menu...")
        }

        verify(exactly = 1) { reportUI.viewReportCommand() }
    }

    @Test
    fun `should show error when invalid input is given`() {
        // Arrange


        every { reader.readInput() } returnsMany listOf("x", "0")

        // Act
        ui.showReportMenu()

        // Assert
        verify {
            viewer.logError("❌ Invalid choice. Please try again.")
            viewer.logMessage("🔙 Returning to main menu...")
        }
    }
}
