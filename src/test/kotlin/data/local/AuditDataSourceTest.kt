package data.local

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.datasource.csv.CsvReader
import org.baghdad.data.datasource.csv.CsvWriter
import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.exceptions.audit.EmptyInputProjectIdException
import org.baghdad.logic.model.exceptions.audit.EmptyInputTaskIdException
import org.baghdad.logic.model.exceptions.audit.NoProjectFoundException
import org.baghdad.logic.model.exceptions.audit.NoTaskFoundException
import org.baghdad.logic.model.exceptions.audit.NullInputProjectIdException
import org.baghdad.logic.model.exceptions.audit.NullInputTaskIdException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.Test

class AuditDataSourceTest {
    private lateinit var auditDataSource: AuditDataSource
    private lateinit var dataSource: DataSource<AuditEntity>

    @BeforeEach


    fun setup() {
        dataSource = mockk(relaxed = true)
        auditDataSource = AuditDataSource(dataSource)
    }

    @Test
    fun `should not throw any exception when add`() {
        // Given
        val randomUUID = UUID.randomUUID()
        val auditEntity = AuditEntity(
            entityId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityType = Entities.Task,
        )

        // When & Then
        val result = auditDataSource.createAudit(auditEntity)
    }

    @Test
    fun `should return true when getAuditByTaskId successful retrieved`() {
        // Given
        val randomUUID = UUID.randomUUID()
        val auditEntity = AuditEntity(
            entityId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityType = Entities.Task,
        )

        // When
        every { dataSource.loadAll() } returns listOf(auditEntity)
        val result = auditDataSource.getAuditByTaskId(randomUUID)

        // Then
        assertThat(result[0].entityId.toString() == randomUUID.toString()).isTrue()
    }

    @Test
    fun `should return true when getAuditByProjectId successful retrieved`() {
        // Given
        val randomUUID = UUID.randomUUID()
        val auditEntity = AuditEntity(
            entityId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityType = Entities.Project,
        )

        // When
        every { dataSource.loadAll() } returns listOf(auditEntity)
        val result = auditDataSource.getAuditByProjectId(randomUUID)

        // Then
        assertThat(result[0].entityId.toString() == randomUUID.toString()).isTrue()
    }
    @Test
    fun `should throw a NoTaskFoundException when getAuditByTaskId return empty list`() {
        // Given
        val randomUUID = UUID.randomUUID()
        val auditEntity = AuditEntity(
            entityId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityType = Entities.Task,
        )

        // When & Then
        every { dataSource.loadAll() } returns emptyList()
        assertThrows<NoTaskFoundException> { auditDataSource.getAuditByTaskId(randomUUID) }

    }

    @Test
    fun `should throw a NoProjectFoundException when getAuditByProjectId return empty list`() {
        // Given
        val randomUUID = UUID.randomUUID()
        val auditEntity = AuditEntity(
            entityId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityType = Entities.Task,
        )

        // When & Then
        every { dataSource.loadAll() } returns listOf(auditEntity)
        assertThrows<NoProjectFoundException> { auditDataSource.getAuditByProjectId(randomUUID) }

    }

    @Test
    fun `should throw exception when entity type is not task in getAuditByTaskId`() {
        // Given
        val randomUUID = UUID.randomUUID()
        val auditEntity = AuditEntity(
            entityId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityType = Entities.State,
        )

        // When & Then
        every { dataSource.loadAll() } returns emptyList()
        assertThrows<NoTaskFoundException> { auditDataSource.getAuditByTaskId(randomUUID) }
    }

    @Test
    fun `should throw exception when entity type is not project in getAuditByProjectId`() {
        // Given
        val randomUUID = UUID.randomUUID()
        val auditEntity = AuditEntity(
            entityId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityType = Entities.Task,
        )

        // When & Then
        every { dataSource.loadAll() } returns listOf(auditEntity)
        assertThrows<NoProjectFoundException> { auditDataSource.getAuditByProjectId(randomUUID) }
    }

    @Test
    fun `should throw exception when UUID not as input match in getAuditByTaskId`() {
        // Given
        val randomUUID = UUID.randomUUID()
        val auditEntity = AuditEntity(
            entityId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityType = Entities.Task,
        )
        val inputRandomUUID = UUID.randomUUID()

        // When
        every { dataSource.loadAll() } returns listOf(auditEntity)
        assertThrows<NoTaskFoundException> { auditDataSource.getAuditByTaskId(inputRandomUUID) }

    }

    @Test
    fun `should throw exception when UUID not as input match in getAuditByProjectId`() {
        // Given
        val randomUUID = UUID.randomUUID()
        val auditEntity = AuditEntity(
            entityId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityType = Entities.Project,
        )
        val inputRandomUUID = UUID.randomUUID()

        // When
        every { dataSource.loadAll() } returns listOf(auditEntity)
        assertThrows<NoProjectFoundException> { auditDataSource.getAuditByProjectId(inputRandomUUID) }
    }

    @Test
    fun `should throw exception when entityId exception when not match uuid and entity type in getAuditByTaskId`() {

        // Given
        val randomUUID = UUID.randomUUID()
        val auditEntity = AuditEntity(
            entityId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityType = Entities.State,
        )
        val inputRandomUUID = UUID.randomUUID()

        // When
        every { dataSource.loadAll() } returns listOf(auditEntity)
        assertThrows<NoTaskFoundException> { auditDataSource.getAuditByTaskId(inputRandomUUID) }


    }

    @Test
    fun `should throw exception when entityId exception when not match uuid and entity type in getAuditByProjectId`() {
        // Given
        val randomUUID = UUID.randomUUID()
        val auditEntity = AuditEntity(
            entityId = randomUUID,
            action = "CREATE",
            user = mockk(),
            entityType = Entities.State,
        )
        val inputRandomUUID = UUID.randomUUID()

        // When
        every { dataSource.loadAll() } returns listOf(auditEntity)
        assertThrows<NoProjectFoundException> { auditDataSource.getAuditByProjectId(inputRandomUUID) }
    }
}