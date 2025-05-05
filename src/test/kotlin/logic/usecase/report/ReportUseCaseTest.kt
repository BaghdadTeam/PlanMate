package logic.usecase.report
import org.junit.jupiter.api.BeforeEach
import io.mockk.every
import io.mockk.mockk
import org.baghdad.logic.model.entities.ProjectSummaryReport
import org.baghdad.logic.usecase.report.ReportService
import org.baghdad.logic.usecase.report.ReportUseCase
import org.junit.jupiter.api.Assertions.*
import java.util.UUID
import kotlin.test.Test

class ReportUseCaseTest{
    private lateinit var reportUseCase: ReportUseCase
    private lateinit var reportService: ReportService

    @BeforeEach
    fun setup(){
        reportService = mockk(relaxed = true)
        reportUseCase = ReportUseCase(reportService)
    }

    @Test
    fun `generateProjectSummary should call reportService summary`() {
        val fakeReports = listOf(
            ProjectSummaryReport(
                projectName = "Test Project",
                totalTasks = 5,
                tasksPerState = mapOf(UUID.randomUUID() to 3),
                tasksPerUser = mapOf(UUID.randomUUID() to 5)
            )
        )
        every { reportService.summary() } returns fakeReports
        
        val result = reportUseCase.generateProjectSummary()

        assertEquals(fakeReports, result)
    }

}