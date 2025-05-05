import io.mockk.*
import kotlin.test.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.baghdad.logic.usecase.report.ReportService
import org.baghdad.logic.model.entities.*
import org.baghdad.logic.repositories.*
import java.util.*

class ReportServiceTest {

    private lateinit var projectRepo: ProjectRepository
    private lateinit var taskRepo: TaskRepository
    private lateinit var stateRepo: ProjectStatesRepository

    private lateinit var reportService: ReportService

    private val projectId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        projectRepo = mockk()
        taskRepo = mockk()
        stateRepo = mockk()

        reportService = ReportService(projectRepo, taskRepo, stateRepo)

        val project = ProjectEntity(id = projectId, name = "Test Project", creatorId = "user1")

        val states = listOf(
            StateEntity(
                id = UUID.randomUUID(),
                name = "To Do",
                projectId = UUID.randomUUID(),
                creatorId = UUID.randomUUID()
            ),

            StateEntity(
                id = UUID.randomUUID(),
                name = "In Progress",
                projectId = UUID.randomUUID(),
                creatorId = UUID.randomUUID()
            )
        )

        val tasks = listOf(
            TaskEntity(
                title = "T1",
                description = "D1",
                stateId = states[0].id,
                projectId = projectId,
                creatorId = UUID.randomUUID()
            ),
            TaskEntity(
                title = "T2",
                description = "D2",
                stateId = states[0].id,
                projectId = projectId,
                creatorId = UUID.randomUUID()
            ),
            TaskEntity(
                title = "T3",
                description = "D3",
                stateId = UUID.randomUUID(),
                projectId = projectId,
                creatorId = UUID.randomUUID()
            )
        )


        every { projectRepo.getAllProjects() } returns listOf(project)
        every { taskRepo.getTasksByProjectId(projectId) } returns tasks
        every { stateRepo.getAllStatesPerProject(projectId) } returns states
    }

    @Test
    fun `test summary with mockk`() {
        val summaryList = reportService.summary()
        val report = summaryList.first()

        assertEquals(2, report.tasksPerState[UUID.randomUUID()] ?: 0
        )
    }

    @Test
    fun `test summary with user`() {
        val summaryList = reportService.summary()
        val report = summaryList.first()

        assertEquals(1, report.tasksPerUser[UUID.randomUUID()] ?: 0)
    }


    @Test
    fun `test summary with totalTask`() {
        val summaryList = reportService.summary()
        val report = summaryList.first()

        assertEquals(3, report.totalTasks)
    }

    @Test
    fun `test summary with projectName`() {
        val summaryList = reportService.summary()
        val report = summaryList.first()

        assertEquals("Test Project", report.projectName)
    }
}