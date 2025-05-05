package data.datasource.mapper

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import org.baghdad.data.datasource.mapper.audit.AuditMapper
import org.baghdad.data.datasource.mapper.user.UserMapper
import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
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
            .isEqualTo("id,entityType,entityId,action,user,timestamp")
    }

    @Test
    fun `deserializer parses line into AuditEntity`() {
        // Given
        val uuid = UUID.randomUUID()
        val user = UserEntity(
            name = "Youssef",
            username = "Pixel",
            hashedPassword = "someHashedPassword",
            type = UserType.Mate
        )

        // → match the trailing ']' that your parser leaves in the argument
        every { anyConstructed<UserMapper>().deserializer("Pixel,Youssef]") } returns user

        val timestamp = "2025-04-29T12:34:56Z"
        val entityId = UUID.randomUUID()
        val line = "$uuid,Order,$entityId,CREATE,[Pixel,Youssef],$timestamp"

        // When
        val result = parser.deserializer(line)

        // Then
        assertThat(result.id).isEqualTo(uuid)
        assertThat(result.entityType).isEqualTo("Order")
        assertThat(result.entityId).isEqualTo(entityId)
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
        val user = UserEntity(
            name = "Youssef Mohamed",
            username = "Pixel",
            hashedPassword = "pw",
            type = UserType.Mate
        )
        every { anyConstructed<UserMapper>().serializer(user) } returns "Pixel,Youssef Mohamed"
        val timestamp = "2025-04-29T12:34:56Z"
        val entityId = UUID.randomUUID()
        val entity = AuditEntity(
            id = uuid,
            entityType = "Product",
            entityId = entityId,
            action = "UPDATE",
            user = user,
            timestamp = timestamp
        )

        // When
        val csvLine = parser.serializer(entity)

        // Then
        assertThat(csvLine).isEqualTo("$uuid,Product,$entityId,UPDATE,[Pixel,Youssef Mohamed],$timestamp")
    }

    @Test
    fun `serializer wraps user CSV in square brackets`() {
        // Given
        val uuid = UUID.randomUUID()
        val user = UserEntity(
            name = "ASDASD",
            username = "Bodi",
            hashedPassword = "pw",
            type = UserType.Mate
        )
        // simulate a user serializer with multiple commas
        every { anyConstructed<UserMapper>().serializer(user) } returns "Bodi,ASDASD,Extra"
        val timestamp = "2025-05-01T00:00:00Z"
        val entityId = UUID.randomUUID()
        val entity = AuditEntity(
            id = uuid,
            entityType = "Invoice",
            entityId = entityId,
            action = "DELETE",
            user = user,
            timestamp = timestamp
        )

        // When
        val csvLine = parser.serializer(entity)

        // Then
        // The user part should be wrapped in a single pair of brackets
        assertThat(csvLine).isEqualTo("$uuid,Invoice,$entityId,DELETE,[Bodi,ASDASD,Extra],$timestamp")
    }
}
