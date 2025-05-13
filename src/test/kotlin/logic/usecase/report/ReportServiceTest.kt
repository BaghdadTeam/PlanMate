package logic.usecase.report

import io.mockk.*
import kotlinx.coroutines.test.runTest
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
    private val state1Id = UUID.randomUUID()
    private val state2Id = UUID.randomUUID()
    private val user1Id = UUID.randomUUID()
    private val user2Id = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        projectRepo = mockk()
        taskRepo = mockk()
        stateRepo = mockk()

        reportService = ReportService(projectRepo, taskRepo, stateRepo)

        val project = ProjectEntity(id = projectId, name = "Test Project", creatorId = user1Id)

        val states = listOf(
            TaskStateEntity(id = state1Id, name = "To Do", projectId = projectId, creatorId = user1Id),
            TaskStateEntity(id = state2Id, name = "In Progress", projectId = projectId, creatorId = user1Id)
        )

        val tasks = listOf(
            TaskEntity(
                title = "T1",
                description = "D1",
                stateId = state1Id,
                projectId = projectId,
                creatorId = user1Id
            ),
            TaskEntity(
                title = "T2",
                description = "D2",
                stateId = state1Id,
                projectId = projectId,
                creatorId = user1Id
            ),
            TaskEntity(
                title = "T3",
                description = "D3",
                stateId = state2Id,
                projectId = projectId,
                creatorId = user2Id
            )
        )

        coEvery { projectRepo.getAllProjects() } returns listOf(project)
        coEvery { taskRepo.getTasksByProjectId(projectId) } returns tasks
        coEvery { stateRepo.getAllStatesPerProject(projectId) } returns states
    }

    @Test
    fun `test summary returns correct totalTask`() = runTest {
        val summaryList = reportService.summary()
        val report = summaryList.first()

        assertEquals(3, report.totalTasks)
    }

    @Test
    fun `test summary returns correct projectName`() = runTest {
        val summaryList = reportService.summary()
        val report = summaryList.first()

        assertEquals("Test Project", report.projectName)
    }

    @Test
    fun `test summary returns correct tasks per state`() = runTest {
        val summaryList = reportService.summary()
        val report = summaryList.first()

        assertEquals(2, report.tasksPerState[state1Id])
        assertEquals(1, report.tasksPerState[state2Id])
    }

    @Test
    fun `test summary returns correct tasks per user`() = runTest {
        val summaryList = reportService.summary()
        val report = summaryList.first()

        assertEquals(2, report.tasksPerUser[user1Id])
        assertEquals(1, report.tasksPerUser[user2Id])
    }
}
