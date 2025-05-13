package logic.usecase.report
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.ProjectSummaryReport
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.usecase.report.ReportService
import org.baghdad.logic.usecase.report.ReportUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class ReportUseCaseTest{
    private lateinit var reportUseCase: ReportUseCase
    private lateinit var reportService: ReportService
    private val sessionManager: SessionManager = mockk()

    @BeforeEach
    fun setup(){
        reportService = mockk(relaxed = true)
        reportUseCase = ReportUseCase(reportService,sessionManager)
        coEvery { sessionManager.isAuthenticated() } returns true
    }

    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows<UnauthorizedException> { reportUseCase.generateProjectSummary() }
    }
    @Test
    fun `generateProjectSummary should call reportService summary`() = runTest {
        val fakeReports = listOf(
            ProjectSummaryReport(
                projectName = "Test Project",
                totalTasks = 5,
                tasksPerState = mapOf(UUID.randomUUID() to 3),
                tasksPerUser = mapOf(UUID.randomUUID() to 5)
            )
        )
        coEvery { reportService.summary() } returns fakeReports
        
        val result = reportUseCase.generateProjectSummary()

        assertEquals(fakeReports, result)
    }

}