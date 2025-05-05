package presentation.reportSummary
import io.mockk.*
import org.baghdad.logic.model.entities.ProjectSummaryReport
import kotlin.test.*
import org.baghdad.logic.usecase.report.ReportUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.reportSummary.ReportUI
import java.util.UUID

class ReportUITest {

    private lateinit var reportUseCase: ReportUseCase
    private lateinit var reportUI: ReportUI
    private lateinit var reader: Reader
    private lateinit var viewer: Viewer

    private val fakeReports = listOf(
        ProjectSummaryReport(
            projectName = "Demo Project",
            totalTasks = 4,
            tasksPerState = mapOf(UUID.randomUUID() to 2, UUID.randomUUID() to 2),
            tasksPerUser = mapOf(UUID.randomUUID() to 3, UUID.randomUUID() to 1)
        )
    )

    private val originalOut = System.out

    @BeforeTest
    fun setup() {
        reportUseCase = mockk()
        reader = mockk()
        viewer = mockk(relaxed = true) // relaxed allows ignoring unverified logMessage calls
        every { reportUseCase.generateProjectSummary() } returns fakeReports
        every { reader.readInput() } returns "1"

        reportUI = ReportUI(reportUseCase, viewer, reader)
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

        verify {
            viewer.logMessage(match { it.contains("Demo Project") })
            viewer.logMessage(match { it.contains("Project Report") })
            viewer.logMessage(match { it.contains("Total Tasks") })
            viewer.logMessage(match { it.contains("To Do: 2") })
            viewer.logMessage(match { it.contains("Done: 2") })
            viewer.logMessage(match { it.contains("user1: 3") })
            viewer.logMessage(match { it.contains("user2: 1") })
        }
    }

    @Test
    fun `should show error when user input is null`() {
        every { reader.readInput() } returns null
        reportUI.viewReportCommand()

        verify { viewer.logError("❌ Invalid selection.") }
    }

    @Test
    fun `when task per state is empty`() {
        every { reportUseCase.generateProjectSummary() } returns emptyList()
        reportUI.viewReportCommand()

        verify { viewer.logError("⚠️ No projects found.") }
    }

    @Test
    fun `when task per state and per user are empty`() {
        val fakeReport = ProjectSummaryReport(
            projectName = "Empty Stats Project",
            totalTasks = 3,
            tasksPerState = emptyMap(),
            tasksPerUser = emptyMap()
        )

        every {  reportUseCase.generateProjectSummary() } returns listOf(fakeReport)
        every { reader.readInput() } returns "1"

        reportUI.viewReportCommand()

        verify {
            viewer.logMessage(match { it.contains("Empty Stats Project") })
            viewer.logError("  No data available.")
        }
    }

    @Test
    fun `when generate project summary throw exception`() {
        every { reportUseCase.generateProjectSummary() } throws Exception()
        reportUI.viewReportCommand()

        verify { viewer.logError("something went wrong") }
    }

}
