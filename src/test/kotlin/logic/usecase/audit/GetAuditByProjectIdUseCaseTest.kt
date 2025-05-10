package logic.usecase.audit

import com.google.common.truth.Truth.assertThat
import helpers.projectStates.ProjectStatesEntityTestData
import helpers.task.TaskEntityTestData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.audit.GetAuditByProjectIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class GetAuditByProjectIdUseCaseTest {

    private lateinit var auditRepository: AuditRepository
    private lateinit var projectStatesRepository: ProjectStatesRepository
    private lateinit var taskRepository: TaskRepository
    private lateinit var getAuditByProjectIdUseCase: GetAuditByProjectIdUseCase

    private val mockUser = UserEntity(
        name = "Audit User",
        username = "audituser",
        hashedPassword = "hashed",
        type = UserType.Admin
    )

    @BeforeEach
    fun setup() {
        auditRepository = mockk(relaxed = true)
        projectStatesRepository = mockk(relaxed = true)
        taskRepository = mockk(relaxed = true)
        getAuditByProjectIdUseCase =
            GetAuditByProjectIdUseCase(auditRepository, projectStatesRepository, taskRepository)
    }

    @Test
    fun `should fetch and combine all audit logs for project`() = runTest {
        val projectId = UUID.randomUUID()

        // Prepare mock data for the audit logs
        val audit1 = AuditLogEntity(
            id = UUID.randomUUID(),
            entityUnderAudit = "Task",
            projectId = projectId,
            action = "Created",
            user = mockUser,
            timestamp = LocalDateTime.now().minusDays(1)
        )

        val audit2 = AuditLogEntity(
            id = UUID.randomUUID(),
            entityUnderAudit = "Project",
            projectId = projectId,
            action = "Updated",
            user = mockUser,
            timestamp = LocalDateTime.now().minusDays(2)
        )

        val audit3 = AuditLogEntity(
            id = UUID.randomUUID(),
            entityUnderAudit = "State",
            projectId = projectId,
            action = "Created",
            user = mockUser,
            timestamp = LocalDateTime.now()
        )

        coEvery { auditRepository.getAuditByProjectId(projectId) } returns listOf(audit1, audit2, audit3)

        // Run the use case
        val auditLogs = getAuditByProjectIdUseCase.invoke(projectId)

        // Define the expected list of audit logs
        val expectedAuditLogs = listOf(audit3, audit1, audit2)

        // Verify that the fetched logs are as expected
        assertThat(expectedAuditLogs.size).isEqualTo(auditLogs.size)
        assertThat(expectedAuditLogs).isEqualTo(auditLogs)
    }

    @Test
    fun `should return empty list when there is no audit for project, states or tasks`() = runTest {
        val projectId = UUID.randomUUID()

        coEvery { auditRepository.getAuditByProjectId(any()) } returns emptyList()
        coEvery { projectStatesRepository.getAllStatesPerProject(projectId) } returns emptyList()
        coEvery { taskRepository.getTasksByProjectId(projectId) } returns emptyList()

        val result = getAuditByProjectIdUseCase.invoke(projectId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should include duplicate audits if multiple entities share audit log`() = runTest {
        val projectId = UUID.randomUUID()
        val sharedAudit = AuditLogEntity(UUID.randomUUID(), "Task", projectId, "Viewed", mockUser, LocalDateTime.now())

        val state = ProjectStatesEntityTestData.todoState().copy(projectId = projectId)
        val task = TaskEntityTestData.normalTask.copy(projectId = projectId)

        coEvery { auditRepository.getAuditByProjectId(projectId) } returns listOf(sharedAudit)
        coEvery { projectStatesRepository.getAllStatesPerProject(projectId) } returns listOf(state)
        coEvery { auditRepository.getAuditByProjectId(state.projectId) } returns listOf(sharedAudit)
        coEvery { taskRepository.getTasksByProjectId(projectId) } returns listOf(task)

        val result = getAuditByProjectIdUseCase.invoke(projectId)

        assertThat(result.count { it == sharedAudit }).isEqualTo(3)
    }

    @Test
    fun `should return audits sorted by descending timestamp`() = runTest {
        val projectId = UUID.randomUUID()
        val auditOld = AuditLogEntity(
            UUID.randomUUID(),
            "Project",
            projectId,
            "Old log",
            mockUser,
            LocalDateTime.now().minusDays(2)
        )
        val auditMid = AuditLogEntity(
            UUID.randomUUID(),
            "Project",
            projectId,
            "Mid log",
            mockUser,
            LocalDateTime.now().minusDays(1)
        )
        val auditNew = AuditLogEntity(UUID.randomUUID(), "Project", projectId, "New log", mockUser, LocalDateTime.now())

        coEvery { auditRepository.getAuditByProjectId(projectId) } returns listOf(auditOld, auditMid, auditNew)
        coEvery { projectStatesRepository.getAllStatesPerProject(projectId) } returns emptyList()
        coEvery { taskRepository.getTasksByProjectId(projectId) } returns emptyList()

        val result = getAuditByProjectIdUseCase.invoke(projectId)

        val timestamps = result.map { it.timestamp }
        assertThat(timestamps).isEqualTo(timestamps.sortedDescending())
    }
}
