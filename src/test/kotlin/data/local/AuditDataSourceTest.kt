package data.local


import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.dto.AuditLogDto
import org.baghdad.data.local.AuditDataSource
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.exceptions.NoProjectFoundException
import org.baghdad.logic.model.exceptions.NoTaskFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*
import java.util.UUID.randomUUID
import kotlin.test.Test

class AuditDataSourceTest {
    private lateinit var auditDataSource: AuditDataSource
    private lateinit var dataSource: DataSource<AuditLogDto>

    @BeforeEach


    fun setup() {
        dataSource = mockk(relaxed = true)
        auditDataSource = AuditDataSource(dataSource)
    }

    @Test
    fun `should not throw any exception when add`() = runTest {
        // Given
        val randomUUID = randomUUID()
        val auditLogDto = AuditLogDto(
            id = randomUUID(),
            projectId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityUnderAudit = Entities.Task.name,
            timestamp = LocalDateTime.now()
        )


        // When & Then
        auditDataSource.createAudit(auditLogDto)
    }

    @Test
    fun `should return true when getAuditByTaskId successful retrieved`() = runTest {
        // Given
        val randomUUID = randomUUID()
        val auditLogDto = AuditLogDto(
            id = randomUUID(),
            projectId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityUnderAudit = Entities.Task.name,
            timestamp = LocalDateTime.now()
        )

        // When
        coEvery { dataSource.loadAll() } returns listOf(auditLogDto)
        val result = auditDataSource.getAuditByTaskId(randomUUID)

        // Then
        assertThat(result[0].projectId.toString() == randomUUID.toString()).isTrue()
    }

    @Test
    fun `should return true when getAuditByProjectId successful retrieved`() = runTest {
        // Given
        val randomUUID = randomUUID()
        val auditLogDto = AuditLogDto(
            id = randomUUID(),
            projectId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityUnderAudit = Entities.Project.name,
            timestamp = LocalDateTime.now()
        )

        // When
        coEvery { dataSource.loadAll() } returns listOf(auditLogDto)
        val result = auditDataSource.getAuditByProjectId(randomUUID)

        // Then
        assertThat(result[0].projectId.toString() == randomUUID.toString()).isTrue()
    }

    @Test
    fun `should throw a NoTaskFoundException when getAuditByTaskId return empty list`() = runTest {
        // Given
        val randomUUID = randomUUID()


        // When & Then
        coEvery { dataSource.loadAll() } returns emptyList()
        assertThrows<NoTaskFoundException> { auditDataSource.getAuditByTaskId(randomUUID) }

    }

    @Test
    fun `should throw a NoProjectFoundException when getAuditByProjectId return empty list`() =
        runTest {
            // Given
            val projectId = randomUUID()
            val auditLogDto = AuditLogDto(
                id = randomUUID(),
                projectId = projectId,
                action = "CREATE",
                user = mockk(),
                entityUnderAudit = Entities.Task.name,
                timestamp = LocalDateTime.now()
            )

            // When & Then
            coEvery { dataSource.loadAll() } returns listOf(auditLogDto)
            assertThrows<NoProjectFoundException> { auditDataSource.getAuditByProjectId(randomUUID()) }

        }

    @Test
    fun `should throw exception when entity type is not task in getAuditByTaskId`() = runTest {
        // Given
        val randomUUID = randomUUID()

        // When & Then
        coEvery { dataSource.loadAll() } returns emptyList()
        assertThrows<NoTaskFoundException> { auditDataSource.getAuditByTaskId(randomUUID) }
    }

    @Test
    fun `should throw exception when UUID not as input match in getAuditByTaskId`() = runTest {
        // Given
        val randomUUID = randomUUID()
        val auditLogDto = AuditLogDto(
            id = randomUUID(),
            projectId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityUnderAudit = Entities.Task.name,
            timestamp = LocalDateTime.now()
        )
        val inputRandomUUID = randomUUID()

        // When
        coEvery { dataSource.loadAll() } returns listOf(auditLogDto)
        assertThrows<NoTaskFoundException> { auditDataSource.getAuditByTaskId(inputRandomUUID) }

    }

    @Test
    fun `should throw exception when UUID not as input match in getAuditByProjectId`() = runTest {
        // Given
        val randomUUID = randomUUID()
        val auditLogDto = AuditLogDto(
            id = randomUUID(),
            projectId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityUnderAudit = Entities.Project.name,
            timestamp = LocalDateTime.now()
        )
        val inputRandomUUID = randomUUID()

        // When
        coEvery { dataSource.loadAll() } returns listOf(auditLogDto)
        assertThrows<NoProjectFoundException> { auditDataSource.getAuditByProjectId(inputRandomUUID) }
    }

    @Test
    fun `should throw exception when projectId exception when not match uuid and entity type in getAuditByTaskId`() =
        runTest {

            // Given
            val randomUUID = randomUUID()
            val auditLogDto = AuditLogDto(
                id = randomUUID(),
                projectId = randomUUID,
                action = "CREATE",
                user = mockk(),
                entityUnderAudit = Entities.State.name,
                timestamp = LocalDateTime.now()
            )
            val inputRandomUUID = randomUUID()

            // When
            coEvery { dataSource.loadAll() } returns listOf(auditLogDto)
            assertThrows<NoTaskFoundException> { auditDataSource.getAuditByTaskId(inputRandomUUID) }


        }

    @Test
    fun `should throw exception when projectId exception when not match uuid and entity type in getAuditByProjectId`() =
        runTest {
            // Given
            val randomUUID = randomUUID()
            val auditLogDto = AuditLogDto(
                id = randomUUID(),
                projectId = randomUUID,
                action = "CREATE",
                user = mockk(),
                entityUnderAudit = Entities.State.name,
                timestamp = LocalDateTime.now()
            )
            val inputRandomUUID = randomUUID()

            // When
            coEvery { dataSource.loadAll() } returns listOf(auditLogDto)
            assertThrows<NoProjectFoundException> {
                auditDataSource.getAuditByProjectId(
                    inputRandomUUID
                )
            }
        }
}