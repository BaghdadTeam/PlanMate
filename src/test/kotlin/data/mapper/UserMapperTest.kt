package data.mapper

import org.baghdad.data.dto.UserDto
import org.baghdad.data.repositories.toDomain
import org.baghdad.data.repositories.toDto
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class UserMapperTest {
    @Test
    fun `UserDto maps to UserEntity correctly`() {
        val dto = UserDto(
            id = UUID.randomUUID(),
            username = "ali.dev",
            name = "Ali Hussein",
            hashedPassword = "hashed123",
            type = UserType.Mate.name
        )

        val entity = dto.toDomain()

        assertEquals(dto.id, entity.id)
        assertEquals(dto.username, entity.username)
        assertEquals(dto.name, entity.name)
    }
    @Test
    fun `UserEntity maps to UserDto correctly`() {
        val entity = UserEntity(
            id = UUID.randomUUID(),
            name = "Sara Ahmed",
            username = "sara.a",
            type = UserType.Mate
        )

        val dto = entity.toDto("secretHash")

        assertEquals(entity.id, dto.id)
        assertEquals(entity.username, dto.username)
        assertEquals(entity.name, dto.name)
        assertEquals("secretHash", dto.hashedPassword)
        assertEquals("Mate", dto.type)
    }
}