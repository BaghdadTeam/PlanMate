package data.datasource.mapper

import com.google.common.truth.Truth.assertThat
import org.baghdad.data.datasource.mapper.state.StateMapper
import org.baghdad.logic.model.entities.StateEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class StateMapperTest {
    private lateinit var parser: StateMapper

    @BeforeEach
    fun setUp() {
        parser = StateMapper()
    }

    @Test
    fun `header returns correct CSV header`() {
        assertThat(parser.header())
            .isEqualTo("id,name,projectId,creatorId")
    }

    @Test
    fun `deserializer parses line into StateEntity`() {
        // Given
        val uuid = UUID.randomUUID()
        val name = "InProgress"
        val projectId = "proj001"
        val creatorId = "userA"
        val line = "$uuid,$name,$projectId,$creatorId"

        // When
        val result = parser.deserializer(line)

        // Then
        assertThat(result.id).isEqualTo(uuid)
        assertThat(result.name).isEqualTo(name)
        assertThat(result.projectId).isEqualTo(projectId)
        assertThat(result.creatorId).isEqualTo(creatorId)
    }

    @Test
    fun `deserializer throws IllegalArgumentException for bad UUID`() {
        val badLine = "not-a-uuid,Name,proj,user"
        assertThrows<IllegalArgumentException> {
            parser.deserializer(badLine)
        }
    }

    @Test
    fun `deserializer throws IndexOutOfBoundsException for malformed line`() {
        // Only three fields instead of four
        val malformed = "123e4567-e89b-12d3-a456-426614174000,OnlyName,projId"
        assertThrows<IndexOutOfBoundsException> { parser.deserializer(malformed) }
    }

    @Test
    fun `serializer produces correct CSV line`() {
        // Given
        val uuid = UUID.randomUUID()
        val name = "Done"
        val projectId = "proj002"
        val creatorId = "userB"
        val entity = StateEntity(
            id = uuid,
            name = name,
            projectId = projectId,
            creatorId =
        )

        // When
        val csvLine = parser.serializer(entity)

        // Then
        assertThat(csvLine).isEqualTo("$uuid,$name,$projectId,$creatorId")
    }
}