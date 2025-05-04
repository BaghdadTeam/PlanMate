package presentation.reportSummary
import org.junit.jupiter.api.Assertions.*
import io.mockk.*
import org.baghdad.logic.model.entities.ProjectSummaryReport
import kotlin.test.*
import org.baghdad.logic.usecase.report.ReportUseCase
import org.baghdad.presentation.reportSummary.ReportUI
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ReportUITest {

    private lateinit var reportUseCase: ReportUseCase
    private lateinit var reportUI: ReportUI

    private val fakeReports = listOf(
        ProjectSummaryReport(
            projectName = "Demo Project",
            totalTasks = 4,
            tasksPerState = mapOf("To Do" to 2, "Done" to 2),
            tasksPerUser = mapOf("user1" to 3, "user2" to 1)
        )
    )

    private val originalOut = System.out
    private lateinit var outputStream: ByteArrayOutputStream

    @BeforeTest
    fun setup() {
        reportUseCase = mockk()
        every { reportUseCase.generateProjectSummary() } returns fakeReports
        reportUI = ReportUI(reportUseCase)

        // Redirect stdout
        outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        // Mock readLine()
        mockkStatic("kotlin.io.ConsoleKt")
        every { readLine() } returns "1"
    }

    @AfterTest
    fun teardown() {
        // Restore stdout
        System.setOut(originalOut)
        unmockkAll()
    }

    @Test
    fun `viewReportCommand should print project report correctly`() {
        reportUI.viewReportCommand()

        val output = outputStream.toString()

        assertTrue(output.contains("List of Projects"))
        assertTrue(output.contains("Demo Project"))
        assertTrue(output.contains("Project Report: Demo Project"))
        assertTrue(output.contains("Total Tasks:"))
        assertTrue(output.contains("Tasks by State:"))
        assertTrue(output.contains("To Do: 2 task"))
        assertTrue(output.contains("Done: 2 task"))
        assertTrue(output.contains("Tasks by User:"))
        assertTrue(output.contains("user1: 3 task"))
        assertTrue(output.contains("user2: 1 task"))
    }

    @Test
    fun `when user input is null`() {
        every { readLine()?.toIntOrNull() } returns null
        reportUI.viewReportCommand()

        val output = outputStream.toString()

        assertTrue(output.contains("Invalid selection."))
    }

    @Test
    fun `when task per state is empty`() {
        every { readLine()?.toIntOrNull() } returns null
        every { reportUseCase.generateProjectSummary()} returns emptyList()

        reportUI.viewReportCommand()

        val output = outputStream.toString()

        assertTrue(output.contains("No projects found."))
    }

    @Test
    fun `when task per state and per user are empty`() {
        val fakeReport = ProjectSummaryReport(
            projectName = "Empty Stats Project",
            totalTasks = 3,
            tasksPerState = emptyMap(),
            tasksPerUser = emptyMap()
        )

        every { reportUseCase.generateProjectSummary() } returns listOf(fakeReport)
        every { readLine() } returns "1"

        reportUI.viewReportCommand()
        val output = outputStream.toString()

        assertTrue(output.contains("Empty Stats Project"))
        assertTrue(output.contains("No data available."))
    }

}
