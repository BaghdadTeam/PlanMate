package data.datasource.mapper

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import org.baghdad.data.datasource.mapper.audit.AuditMapper
import org.baghdad.data.datasource.mapper.user.UserMapper
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.UnSupportedTimeStampFormatException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.UUID
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
            .isEqualTo("id,entityType,entityId,action,user,timestamp")
    }

    @Test
    fun `deserializer parses line into AuditEntity`() {
        // Given
        val uuid = UUID.randomUUID()
        val typeId = UUID.randomUUID()
        val userId = UUID.randomUUID()


        // → match the trailing ']' that your parser leaves in the argument

        val timestamp = LocalDateTime.now()
        val auditEntityType = Entities.Task.name
        val line = "$uuid,$auditEntityType,$typeId,CREATE,$userId,$timestamp"

        // When
        val result = parser.deserializer(line)

        // Then
        assertThat(result.id).isEqualTo(uuid)
        assertThat(result.entityUnderAudit).isEqualTo(auditEntityType)
        assertThat(result.projectId).isEqualTo(typeId)
        assertThat(result.action).isEqualTo("CREATE")
        assertThat(result.userId).isEqualTo(userId)
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `deserializer throws IllegalArgumentException for bad UUID`() {
        val badLine = "not-a-uuid,Type,ID,ACTION,userId,timestamp"
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
        val entityId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val entity = AuditLogEntity(
            id = uuid,
            entityUnderAudit = Entities.Task.name,
            projectId = entityId,
            action = "UPDATE",
            userId = userId,
        )

        // When
        val csvLine = parser.serializer(entity)

        // Then
        assertThat(csvLine).isEqualTo("$uuid,${entity.entityUnderAudit},$entityId,UPDATE,$userId,${entity.timestamp}")
    }

    @Test
    fun `serializer wraps user CSV in square brackets`() {
        // Given
        val uuid = UUID.randomUUID()
        val entityId = UUID.randomUUID()
        val userId = UUID.randomUUID()


        val entity = AuditLogEntity(
            id = uuid,
            entityUnderAudit = Entities.Task.name,
            projectId = entityId,
            action = "DELETE",
            userId = userId,

            )

        // When
        val csvLine = parser.serializer(entity)

        // Then
        // The user part should be wrapped in a single pair of brackets
        assertThat(csvLine).isEqualTo("$uuid,${entity.entityUnderAudit},$entityId,DELETE,$userId,${entity.timestamp}")
    }


    @Test
    fun `throw UnSupportedTimeStampFormatException when deserializer parses line with wrong format for datetime`() {
        // Given
        val uuid = UUID.randomUUID()
        val typeId = UUID.randomUUID()
        val userId = UUID.randomUUID()



        val timestamp = "2020-01-02" +
                "" +
                ""
        val auditEntityType = Entities.Task.name
        val line = "$uuid,$auditEntityType,$typeId,CREATE,$userId,$timestamp"

        // When
        assertThrows<UnSupportedTimeStampFormatException> { parser.deserializer(line) }
    }
}
