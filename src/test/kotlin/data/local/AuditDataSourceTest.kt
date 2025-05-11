package data.local


import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.local.AuditDataSource
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.exceptions.NoProjectFoundException
import org.baghdad.logic.model.exceptions.NoTaskFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class AuditDataSourceTest {
    private lateinit var auditDataSource: AuditDataSource
    private lateinit var dataSource: DataSource<AuditLogEntity>

    @BeforeEach


    fun setup() {
        dataSource = mockk(relaxed = true)
        auditDataSource = AuditDataSource(dataSource)
    }

    @Test
    fun `should not throw any exception when add`() = runTest{
        // Given
        val randomUUID = UUID.randomUUID()
        val auditLogEntity = AuditLogEntity(
            projectId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityUnderAudit = Entities.Task.name,
        )

        // When & Then
        auditDataSource.createAudit(auditLogEntity)
    }

    @Test
    fun `should return true when getAuditByTaskId successful retrieved`() = runTest {
        // Given
        val randomUUID = UUID.randomUUID()
        val auditLogEntity = AuditLogEntity(
            projectId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityUnderAudit = Entities.Task.name,
        )

        // When
        coEvery { dataSource.loadAll() } returns listOf(auditLogEntity)
        val result = auditDataSource.getAuditByTaskId(randomUUID)

        // Then
        assertThat(result[0].projectId.toString() == randomUUID.toString()).isTrue()
    }

    @Test
    fun `should return true when getAuditByProjectId successful retrieved`() = runTest{
        // Given
        val randomUUID = UUID.randomUUID()
        val auditLogEntity = AuditLogEntity(
            projectId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityUnderAudit = Entities.Project.name,
        )

        // When
        coEvery { dataSource.loadAll() } returns listOf(auditLogEntity)
        val result = auditDataSource.getAuditByProjectId(randomUUID)

        // Then
        assertThat(result[0].projectId.toString() == randomUUID.toString()).isTrue()
    }

    @Test
    fun `should throw a NoTaskFoundException when getAuditByTaskId return empty list`() = runTest{
        // Given
        val randomUUID = UUID.randomUUID()


        // When & Then
        coEvery { dataSource.loadAll() } returns emptyList()
        assertThrows<NoTaskFoundException> { auditDataSource.getAuditByTaskId(randomUUID) }

    }

    @Test
    fun `should throw a NoProjectFoundException when getAuditByProjectId return empty list`()= runTest {
        // Given
        val projectId = UUID.randomUUID()
        val auditLogEntity = AuditLogEntity(
            projectId = projectId,
            action = "CREATE",
            user = mockk(),
            entityUnderAudit = Entities.Task.name,
        )

        // When & Then
        coEvery { dataSource.loadAll() } returns listOf(auditLogEntity)
        assertThrows<NoProjectFoundException> { auditDataSource.getAuditByProjectId(UUID.randomUUID()) }

    }

    @Test
    fun `should throw exception when entity type is not task in getAuditByTaskId`()= runTest {
        // Given
        val randomUUID = UUID.randomUUID()

        // When & Then
        coEvery { dataSource.loadAll() } returns emptyList()
        assertThrows<NoTaskFoundException> { auditDataSource.getAuditByTaskId(randomUUID) }
    }

    @Test
    fun `should throw exception when UUID not as input match in getAuditByTaskId`() = runTest{
        // Given
        val randomUUID = UUID.randomUUID()
        val auditLogEntity = AuditLogEntity(
            projectId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityUnderAudit = Entities.Task.name,
        )
        val inputRandomUUID = UUID.randomUUID()

        // When
        coEvery { dataSource.loadAll() } returns listOf(auditLogEntity)
        assertThrows<NoTaskFoundException> { auditDataSource.getAuditByTaskId(inputRandomUUID) }

    }

    @Test
    fun `should throw exception when UUID not as input match in getAuditByProjectId`()= runTest {
        // Given
        val randomUUID = UUID.randomUUID()
        val auditLogEntity = AuditLogEntity(
            projectId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityUnderAudit = Entities.Project.name,
        )
        val inputRandomUUID = UUID.randomUUID()

        // When
        coEvery { dataSource.loadAll() } returns listOf(auditLogEntity)
        assertThrows<NoProjectFoundException> { auditDataSource.getAuditByProjectId(inputRandomUUID) }
    }

    @Test
    fun `should throw exception when projectId exception when not match uuid and entity type in getAuditByTaskId`()= runTest {

        // Given
        val randomUUID = UUID.randomUUID()
        val auditLogEntity = AuditLogEntity(
            projectId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityUnderAudit = Entities.State.name,
        )
        val inputRandomUUID = UUID.randomUUID()

        // When
        coEvery { dataSource.loadAll() } returns listOf(auditLogEntity)
        assertThrows<NoTaskFoundException> { auditDataSource.getAuditByTaskId(inputRandomUUID) }


    }

    @Test
    fun `should throw exception when projectId exception when not match uuid and entity type in getAuditByProjectId`() = runTest{
        // Given
        val randomUUID = UUID.randomUUID()
        val auditLogEntity = AuditLogEntity(
            projectId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityUnderAudit = Entities.State.name,
        )
        val inputRandomUUID = UUID.randomUUID()

        // When
        coEvery { dataSource.loadAll() } returns listOf(auditLogEntity)
        assertThrows<NoProjectFoundException> { auditDataSource.getAuditByProjectId(inputRandomUUID) }
    }
}