package data.datasource.mapper

import com.google.common.truth.Truth.assertThat
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import org.baghdad.data.datasource.mapper.audit.AuditMapper
import org.baghdad.data.datasource.mapper.user.UserMapper
import org.baghdad.data.dto.AuditLogDto
import org.baghdad.logic.model.entities.Action
import org.baghdad.logic.model.enums.Entities
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test

class AuditMapperTest {
    private lateinit var parser: AuditMapper

    @BeforeEach
    fun setUp() {
        parser = AuditMapper()
        mockkConstructor(UserMapper::class)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `header returns correct CSV header`() {
        assertThat(parser.header())
            .isEqualTo("id,entityUnderAudit,entityUnderAuditId,projectId,description,action,userId,timestamp")
    }

    @Test
    fun `deserializer parses line into AuditEntity`() {
        // Given
        val uuid = UUID.randomUUID()
        val entityUnderAuditId = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val userId = UUID.randomUUID()


        // → match the trailing ']' that your parser leaves in the argument

        val timestamp = LocalDateTime.now()
        val entityUnderAudit = Entities.Task.name
        val line = "$uuid,$entityUnderAudit,$entityUnderAuditId,$projectId,CREATE,${Action.Create.name},$userId,$timestamp"
        // When
        val result = parser.deserializer(line)


        // Then
        assertThat(result.id).isEqualTo(uuid)
        assertThat(result.entityUnderAudit).isEqualTo(entityUnderAudit)
        assertThat(result.entityUnderAuditId).isEqualTo(entityUnderAuditId.toString())
        assertThat(result.projectId).isEqualTo(projectId.toString())
        assertThat(result.description).isEqualTo("CREATE")
        assertThat(result.action).isEqualTo(Action.Create.name)
        assertThat(result.userId).isEqualTo(userId.toString())
        assertThat(result.timestamp).isEqualTo(timestamp.toString())
    }

    @Test
    fun `deserializer throws IllegalArgumentException for bad UUID`() {
        val badLine = "not-a-uuid,Type,TypeId,ID,ACTION,userId,timestamp"
        assertThrows<IllegalArgumentException> {
            parser.deserializer(badLine)
        }

    }

    @Test
    fun `deserializer throws IndexOutOfBoundsException for malformed line`() {
        val randomUUID = UUID.randomUUID()
        val malformed = "$randomUUID,OnlyTwoFields"
        assertThrows<IndexOutOfBoundsException> {
            parser.deserializer(malformed)
        }
    }

    @Test
    fun `serializer produces correct CSV line`() {
        // Given
        val uuid = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val entityUnderAuditId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val entity = AuditLogDto(
            id = uuid,
            entityUnderAudit = Entities.Task.name,
            entityUnderAuditId = entityUnderAuditId.toString(),
            projectId = projectId.toString(),
            description = "UPDATE",
            action = Action.Update.name,
            userId = userId.toString(),
            timestamp = LocalDateTime.now().toString()
        )

        val expected = "$uuid,${entity.entityUnderAudit},$entityUnderAuditId,$projectId,UPDATE,Update,$userId,${entity.timestamp}"

        // When
        val csvLine = AuditMapper().serializer(entity)

        // Then
        assertThat(csvLine).isEqualTo(expected)
    }

    @Test
    fun `serializer wraps user CSV in square brackets`() {
        // Given
        val uuid = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val entityUnderAuditId = UUID.randomUUID()


        val entity = AuditLogDto(
            id = uuid,
            entityUnderAudit = Entities.Task.name,
            entityUnderAuditId = entityUnderAuditId.toString(),
            projectId = projectId.toString(),
            description = "DELETE",
            action = Action.Delete.name,
            userId = userId.toString(),
            timestamp = LocalDateTime.now().toString()
            )

        // When
        val csvLine = parser.serializer(entity)

        // Then
        // The user part should be wrapped in a single pair of brackets
        assertThat(csvLine).isEqualTo("$uuid,${entity.entityUnderAudit},$entityUnderAuditId,$projectId,DELETE,${Action.Delete.name},$userId,${entity.timestamp}")
    }
}
