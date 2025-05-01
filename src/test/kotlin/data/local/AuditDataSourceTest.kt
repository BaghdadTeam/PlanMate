package data.local

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.datasource.csv.CsvReader
import org.baghdad.data.datasource.csv.CsvWriter
import org.baghdad.logic.model.entities.AuditEntity
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
    private lateinit var csvWriter: CsvWriter
    private lateinit var csvReader: CsvReader

    @BeforeEach
    fun setup() {
        dataSource = mockk(relaxed = true)
        csvWriter = mockk(relaxed = true)
        csvReader = mockk(relaxed = true)
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
            entityType = "Task",
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
            entityType = "Task",
        )

        // When
        every { dataSource.loadAll() } returns listOf(auditEntity)
        val result = auditDataSource.getAuditByTaskId(randomUUID)

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
            entityType = "Task",
        )

        // When & Then
        every { dataSource.loadAll() } returns listOf(auditEntity)
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
            entityType = "Task",
        )

        // When & Then
        every { dataSource.loadAll() } returns listOf(auditEntity)
        assertThrows<NoProjectFoundException> { auditDataSource.getAuditByProjectId(randomUUID) }

    }






    @Test
    fun `addAuditEntry null audit`() {
        // Test that addAuditEntry throws an exception when given a null AuditEntity.
        // TODO implement test
    }

    @Test
    fun `addAuditEntry audit with null entityId`() {
        // Test that addAuditEntry properly handles an AuditEntity with a null entityId
        // (whether by throwing an exception or handling it gracefully)
        // TODO implement test
    }

    @Test
    fun `addAuditEntry audit with null associatedData`() {
        // Test that addAuditEntry properly handles an AuditEntity with null associatedData 
        // (whether by throwing an exception or handling it gracefully) 
        // TODO implement test
    }

    @Test
    fun `getAuditByTaskId returns null`() {
        // Test that getAuditByTaskId returns a null value for the list of entities in cases of exception
        // TODO implement test
    }

}