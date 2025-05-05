package data.datasource.mapper

import com.google.common.truth.Truth.assertThat
import org.baghdad.data.datasource.mapper.task.TaskMapper
import org.baghdad.logic.model.entities.TaskEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class TaskMapperTest {
    private lateinit var parser: TaskMapper

    @BeforeEach
    fun setUp() {
        parser = TaskMapper()
    }

    @Test
    fun `header returns correct CSV header`() {
        assertThat(parser.header()).isEqualTo("id,title,description,stateId,projectId,creatorId")
    }

    @Test
    fun `deserializer parses line into TaskEntity`() {
        // Given
        val uuid = UUID.randomUUID()
        val title = "Implement feature"
        val description = "Add user login"
        val stateId = "state123"
        val projectId = "projXYZ"
        val creatorId = "user42"
        val line = "$uuid,$title,$description,$stateId,$projectId,$creatorId"

        // When
        val result = parser.deserializer(line)

        // Then
        assertThat(result.id).isEqualTo(uuid)
        assertThat(result.title).isEqualTo(title)
        assertThat(result.description).isEqualTo(description)
        assertThat(result.stateId).isEqualTo(stateId)
        assertThat(result.projectId).isEqualTo(projectId)
        assertThat(result.creatorId).isEqualTo(creatorId)
    }

    @Test
    fun `deserializer throws IllegalArgumentException for bad UUID`() {
        val badLine = "not-a-uuid,Title,Desc,state,proj,creator"
        assertThrows<IllegalArgumentException> { parser.deserializer(badLine) }

    }

    @Test
    fun `deserializer throws IndexOutOfBoundsException for malformed line`() {
        // Only five fields instead of six
        val malformed = "123e4567-e89b-12d3-a456-426614174000,Title,Desc,state,proj"
        assertThrows<IndexOutOfBoundsException> { parser.deserializer(malformed) }
    }

    @Test
    fun `serializer produces correct CSV line`() {
        // Given
        val uuid = UUID.randomUUID()
        val title = "Fix bug"
        val description = "Null pointer on load"
        val stateId = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val creatorId = UUID.randomUUID()
        val entity = TaskEntity(
            id = uuid,
            title = title,
            description = description,
            stateId = stateId,
            projectId = projectId,
            creatorId = creatorId
        )

        // When
        val csvLine = parser.serializer(entity)

        // Then
        assertThat(csvLine).isEqualTo("$uuid,$title,$description,$stateId,$projectId,$creatorId")
    }
}
