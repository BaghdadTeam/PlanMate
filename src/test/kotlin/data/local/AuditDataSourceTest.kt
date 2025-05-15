package data.local


import com.google.common.truth.Truth.assertThat
import helpers.audit.AuditTestData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.dto.AuditLogDto
import org.baghdad.data.local.AuditDataSource
import org.baghdad.data.mapper.toDomain
import org.baghdad.logic.model.exceptions.NoAuditForProjectException
import org.baghdad.logic.model.exceptions.NoAuditForTaskException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
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
        val auditLogDto = AuditTestData.createAuditDtoHelper()

        // When & Then
        auditDataSource.createAudit(auditLogDto)
    }

    @Test
    fun `should return true when getAuditByTaskId successful retrieved`() = runTest {
        // Given
        val auditLogDto = AuditTestData.createAuditDtoHelper()

        // When
        coEvery { dataSource.loadAll() } returns listOf(auditLogDto)
        val result = auditDataSource.getAuditByTaskId(auditLogDto.toDomain().entityUnderAuditId)

        // Then
        assertThat(result[0].entityUnderAuditId == auditLogDto.entityUnderAuditId).isTrue()
    }

    @Test
    fun `should return true when getAuditByProjectId successful retrieved`() = runTest {
        // Given
        val auditLogDto = AuditTestData.createAuditDtoHelper()

        // When
        coEvery { dataSource.loadAll() } returns listOf(auditLogDto)
        val result = auditDataSource.getAuditByProjectId(auditLogDto.toDomain().projectId)

        // Then
        assertThat(result[0].projectId == auditLogDto.projectId).isTrue()
    }

    @Test
    fun `should throw a NoTaskFoundException when getAuditByTaskId return empty list`() = runTest{
        // Given
        val randomUUID = UUID.randomUUID()


        // When & Then
        coEvery { dataSource.loadAll() } returns emptyList()
        assertThrows<NoAuditForTaskException> { auditDataSource.getAuditByTaskId(randomUUID) }

    }

    @Test
    fun `should throw a NoProjectFoundException when getAuditByProjectId return empty list`()= runTest {
        // Given
        val auditLogDto = AuditTestData.createAuditDtoHelper()

        // When & Then
        coEvery { dataSource.loadAll() } returns listOf(auditLogDto)
        assertThrows<NoAuditForProjectException> { auditDataSource.getAuditByProjectId(UUID.randomUUID()) }

    }

    @Test
    fun `should throw exception when entity type is not task in getAuditByTaskId`()= runTest {
        // Given
        val randomUUID = UUID.randomUUID()

        // When & Then
        coEvery { dataSource.loadAll() } returns emptyList()
        assertThrows<NoAuditForTaskException> { auditDataSource.getAuditByTaskId(randomUUID) }
    }

    @Test
    fun `should throw exception when UUID not as input match in getAuditByTaskId`() = runTest{
        // Given
        val auditLogDto = AuditTestData.createAuditDtoHelper()

        val inputRandomUUID = UUID.randomUUID()

        // When
        coEvery { dataSource.loadAll() } returns listOf(auditLogDto)
        assertThrows<NoAuditForTaskException> { auditDataSource.getAuditByTaskId(inputRandomUUID) }

    }

    @Test
    fun `should throw exception when UUID not as input match in getAuditByProjectId`()= runTest {
        // Given
        val auditLogDto = AuditTestData.createAuditDtoHelper()

        val inputRandomUUID = UUID.randomUUID()

        // When
        coEvery { dataSource.loadAll() } returns listOf(auditLogDto)
        assertThrows<NoAuditForProjectException> { auditDataSource.getAuditByProjectId(inputRandomUUID) }
    }

    @Test
    fun `should throw exception when projectId exception when not match uuid and entity type in getAuditByTaskId`() =
        runTest {

        // Given
        val auditLogDto = AuditTestData.createAuditDtoHelper()

        val inputRandomUUID = UUID.randomUUID()

            // When
            coEvery { dataSource.loadAll() } returns listOf(auditLogDto)
            assertThrows<NoAuditForTaskException> { auditDataSource.getAuditByTaskId(inputRandomUUID) }


        }

    @Test
    fun `should throw exception when projectId exception when not match uuid and entity type in getAuditByProjectId`() = runTest{
        // Given
        val auditLogDto = AuditTestData.createAuditDtoHelper()

        val inputRandomUUID = UUID.randomUUID()

            // When
            coEvery { dataSource.loadAll() } returns listOf(auditLogDto)
            assertThrows<NoAuditForProjectException> {
                auditDataSource.getAuditByProjectId(
                    inputRandomUUID
                )
            }
        }
}