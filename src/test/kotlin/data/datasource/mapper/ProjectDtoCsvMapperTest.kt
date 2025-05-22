package data.datasource.mapper

import com.google.common.truth.Truth.assertThat
import org.baghdad.data.datasource.mapper.project.ProjectDtoCsvMapper
import org.baghdad.data.dto.project.ProjectDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class ProjectDtoCsvMapperTest {
    private lateinit var parser: ProjectDtoCsvMapper

    @BeforeEach
    fun setUp() {
        parser = ProjectDtoCsvMapper()
    }

    @Test
    fun `header returns correct CSV header`() {
        assertThat(parser.header()).isEqualTo("id,name,creatorId")
    }

    @Test
    fun `deserializer parses line into ProjectDto`() {
        // Given
        val uuid = UUID.randomUUID()
        val name = "New Project"
        val creatorId = UUID.randomUUID()
        val line = "$uuid,$name,$creatorId"

        // When
        val result = parser.deserializer(line)

        // Then
        assertThat(result.id).isEqualTo(uuid)
        assertThat(result.name).isEqualTo(name)
        assertThat(result.creatorId).isEqualTo(creatorId)
    }

    @Test
    fun `deserializer throws IllegalArgumentException for invalid UUID`() {
        // Given malformed UUIDs
        val badLine = "not-a-uuid,Name,creator"

        // When & Then
        assertThrows<IllegalArgumentException> {
            parser.deserializer(badLine)
        }
    }

    @Test
    fun `deserializer throws IndexOutOfBoundsException for too few fields`() {
        // Given only two fields
        val malformed = "123e4567-e89b-12d3-a456-426614174000,OnlyName"

        // When & Then
        assertThrows<IndexOutOfBoundsException> {
            parser.deserializer(malformed)
        }
    }

    @Test
    fun `serializer produces correct CSV line`() {
        // Given
        val uuid = UUID.randomUUID()
        val name = "Project X"
        val creatorId = UUID.randomUUID()
        val dto = ProjectDto(
            id = uuid,
            name = name,
            creatorId = creatorId
        )

        // When
        val csvLine = parser.serializer(dto)

        // Then
        assertThat(csvLine).isEqualTo("$uuid,$name,$creatorId")
    }

    @Test
    fun `getId returns the string representation of ProjectDto id`() {
        // Given
        val uuid = UUID.randomUUID()
        val dto = ProjectDto(
            id = uuid,
            name = "Sample",
            creatorId = UUID.randomUUID()
        )

        // When
        val idString = parser.getId(dto)

        // Then
        assertThat(idString).isEqualTo(uuid.toString())
    }
    @Test
    fun `getId returns string representation of dto id`() {
        // Given
        val uuid = UUID.randomUUID()
        val dto = ProjectDto(
            id = uuid,
            name = "Sample Project",
            creatorId = UUID.randomUUID()
        )

        // When
        val result = parser.getId(dto)

        // Then
        assertThat(result).isEqualTo(uuid.toString())
    }
}
