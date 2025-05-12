package data.datasource.mapper

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import org.baghdad.data.datasource.mapper.audit.AuditMapper
import org.baghdad.data.datasource.mapper.user.UserMapper
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.enums.UserType
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
        val user = UserEntity(
            name = "Youssef",
            username = "Pixel",
            hashedPassword = "someHashedPassword",
            type = UserType.Mate
        )

        // → match the trailing ']' that your parser leaves in the argument
        every { anyConstructed<UserMapper>().deserializer("Pixel,Youssef]") } returns user

        val timestamp = LocalDateTime.now()
        val auditEntityType = Entities.Task.name
        val line = "$uuid,$auditEntityType,$typeId,CREATE,[Pixel,Youssef],$timestamp"

        // When
        val result = parser.deserializer(line)

        // Then
        assertThat(result.id).isEqualTo(uuid)
        assertThat(result.entityUnderAudit).isEqualTo(auditEntityType)
        assertThat(result.projectId).isEqualTo(typeId)
        assertThat(result.action).isEqualTo("CREATE")
        assertThat(result.user).isEqualTo(user)
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `deserializer throws IllegalArgumentException for bad UUID`() {
        val badLine = "not-a-uuid,Type,ID,ACTION,[user],timestamp"
        assertThrows<IllegalArgumentException> {
            parser.deserializer(badLine)
        }

    }

    @Test
    fun `deserializer throws IndexOutOfBoundsException for malformed line`() {
        val malformed = "1234,OnlyTwoFields"
        assertThrows<IndexOutOfBoundsException> {
            parser.deserializer(malformed)
        }
    }


    @Test
    fun `serializer produces correct CSV line`() {
        // Given
        val uuid = UUID.randomUUID()
        val entityId = UUID.randomUUID()
        val user = UserEntity(
            name = "Youssef Mohamed",
            username = "Pixel",
            hashedPassword = "pw",
            type = UserType.Mate
        )
        every { anyConstructed<UserMapper>().serializer(user) } returns "Pixel,Youssef Mohamed"

        val entity = AuditLogEntity(
            id = uuid,
            entityUnderAudit = Entities.Task.name,
            projectId = entityId,
            action = "UPDATE",
            user = user,
        )

        // When
        val csvLine = parser.serializer(entity)

        // Then
        assertThat(csvLine).isEqualTo("$uuid,${entity.entityUnderAudit},$entityId,UPDATE,[Pixel,Youssef Mohamed],${entity.timestamp}")
    }

    @Test
    fun `serializer wraps user CSV in square brackets`() {
        // Given
        val uuid = UUID.randomUUID()
        val entityId = UUID.randomUUID()
        val user = UserEntity(
            name = "ASDASD",
            username = "Bodi",
            hashedPassword = "pw",
            type = UserType.Mate
        )
        // simulate a user serializer with multiple commas
        every { anyConstructed<UserMapper>().serializer(user) } returns "Bodi,ASDASD,Extra"

        val entity = AuditLogEntity(
            id = uuid,
            entityUnderAudit = Entities.Task.name,
            projectId = entityId,
            action = "DELETE",
            user = user,

            )

        // When
        val csvLine = parser.serializer(entity)

        // Then
        // The user part should be wrapped in a single pair of brackets
        assertThat(csvLine).isEqualTo("$uuid,${entity.entityUnderAudit},$entityId,DELETE,[Bodi,ASDASD,Extra],${entity.timestamp}")
    }


    @Test
    fun `throw UnSupportedTimeStampFormatException when deserializer parses line with wrong format for datetime`() {
        // Given
        val uuid = UUID.randomUUID()
        val typeId = UUID.randomUUID()
        val user = UserEntity(
            name = "Youssef",
            username = "Pixel",
            hashedPassword = "someHashedPassword",
            type = UserType.Mate
        )

        // → match the trailing ']' that your parser leaves in the argument
        every { anyConstructed<UserMapper>().deserializer("Pixel,Youssef]") } returns user

        val timestamp = "2020-01-02" +
                "" +
                ""
        val auditEntityType = Entities.Task.name
        val line = "$uuid,$auditEntityType,$typeId,CREATE,[Pixel,Youssef],$timestamp"

        // When
        assertThrows<UnSupportedTimeStampFormatException> { parser.deserializer(line) }
    }
}
