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

    private val toDoStateId = UUID.fromString("00000000-0000-0000-0000-000000000001")
    private val doneStateId = UUID.fromString("00000000-0000-0000-0000-000000000002")
    private val user1Id = UUID.fromString("00000000-0000-0000-0000-000000000011")
    private val user2Id = UUID.fromString("00000000-0000-0000-0000-000000000012")

    private val fakeReports = listOf(
        ProjectSummaryReport(
            projectName = "Demo Project",
            totalTasks = 4,
            tasksPerState = mapOf(toDoStateId to 2, doneStateId to 2),
            tasksPerUser = mapOf(user1Id to 3, user2Id to 1)
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
            viewer.logMessage(match { it.contains("00000000-0000-0000-0000-000000000001: 2") }) // To Do
            viewer.logMessage(match { it.contains("00000000-0000-0000-0000-000000000002: 2") }) // Done
            viewer.logMessage(match { it.contains("00000000-0000-0000-0000-000000000011: 3") }) // user1
            viewer.logMessage(match { it.contains("00000000-0000-0000-0000-000000000012: 1") }) // user2
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
