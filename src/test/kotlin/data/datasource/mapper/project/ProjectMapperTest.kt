package data.datasource.mapper.project

import com.google.common.truth.Truth.assertThat
import org.baghdad.data.datasource.mapper.ProjectMapper
import org.baghdad.logic.model.entities.ProjectEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class ProjectMapperTest {
    private lateinit var parser: ProjectMapper

    @BeforeEach
    fun setUp() {
        parser = ProjectMapper()
    }

    @Test
    fun `header returns correct CSV header`() {
        assertThat(parser.header()).isEqualTo("id,name,creatorId")
    }

    @Test
    fun `deserializer parses line into ProjectEntity`() {
        // Given
        val uuid = UUID.randomUUID()
        val name = "New Project"
        val creatorId = "creator123"
        val line = "$uuid,$name,$creatorId"

        // When
        val result = parser.deserializer(line)

        // Then
        assertThat(result.id).isEqualTo(uuid)
        assertThat(result.name).isEqualTo(name)
        assertThat(result.creatorId).isEqualTo(creatorId)
    }

    @Test
    fun `deserializer throws IllegalArgumentException for bad UUID`() {
        val badLine = "not-a-uuid,Name,creator"
        assertThrows<IllegalArgumentException> {
            parser.deserializer(badLine)
        }
    }

    @Test
    fun `deserializer throws IndexOutOfBoundsException for malformed line`() {
        // Only two fields instead of three
        val malformed = "123e4567-e89b-12d3-a456-426614174000,OnlyName"
        assertThrows<IndexOutOfBoundsException> {
            parser.deserializer(malformed)
        }
    }

    @Test
    fun `serializer produces correct CSV line`() {
        // Given
        val uuid = UUID.randomUUID()
        val name = "Project X"
        val creatorId = "user42"
        val entity = ProjectEntity(
            id = uuid,
            name = name,
            creatorId = creatorId
        )

        // When
        val csvLine = parser.serializer(entity)

        // Then
        assertThat(csvLine).isEqualTo("$uuid,$name,$creatorId")
    }
}