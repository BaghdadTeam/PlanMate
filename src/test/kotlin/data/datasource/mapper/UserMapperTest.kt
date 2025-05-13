package data.datasource.mapper

import com.google.common.truth.Truth.assertThat
import org.baghdad.data.datasource.mapper.user.UserMapper
import org.baghdad.data.dto.UserDto
import org.baghdad.logic.model.entities.UserType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class UserMapperTest {
    private lateinit var parser: UserMapper

    @BeforeEach
    fun setUp() {
        parser = UserMapper()
    }

    @Test
    fun `header returns correct CSV header`() {
        assertThat(parser.header())
            .isEqualTo("id,name,username,hashedPassword,type")
    }

    @Test
    fun `deserializer parses line into UserDto`() {
        // Given
        val uuid = UUID.randomUUID()
        val name = "Alice"
        val username = "alice123"
        val hashedPassword = "hashedPw"
        val type = UserType.Admin.name
        val line = "$uuid,$name,$username,$hashedPassword,${type}"

        // When
        val result = parser.deserializer(line)

        // Then
        assertThat(result.id).isEqualTo(uuid)
        assertThat(result.name).isEqualTo(name)
        assertThat(result.username).isEqualTo(username)
        assertThat(result.hashedPassword).isEqualTo(hashedPassword)
        assertThat(result.type).isEqualTo(type)
    }

    @Test
    fun `deserializer throws IllegalArgumentException for bad UUID`() {
        val badLine = "not-a-uuid,Alice,alice,pass,Admin"
        assertThrows<IllegalArgumentException> { parser.deserializer(badLine) }
    }

    @Test
    fun `deserializer throws IndexOutOfBoundsException for malformed line`() {
        // Only four fields instead of five
        val uuid = UUID.randomUUID()
        val malformed = "$uuid,"
        assertThrows<IndexOutOfBoundsException> { parser.deserializer(malformed) }
    }

    @Test
    fun `serializer produces correct CSV line`() {
        // Given
        val uuid = UUID.randomUUID()
        val name = "Charlie"
        val username = "charlieX"
        val hashedPassword = "pw123"
        val type = UserType.Mate.toString()
        val entity = UserDto(
            id = uuid,
            name = name,
            username = username,
            hashedPassword = hashedPassword,
            type = type
        )

        // When
        val csvLine = parser.serializer(entity)

        // Then
        assertThat(csvLine).isEqualTo("$uuid,$name,$username,$hashedPassword,${type}")
    }
}
