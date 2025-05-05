package org.baghdad.logic.usecase.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.*
import org.baghdad.logic.repositories.UserRepository
import org.junit.jupiter.api.assertThrows

class CreateUserUseCaseTest {
    private val repo = mockk<UserRepository>(relaxed = true)
    private val uc   = CreateUserUseCase(repo)
    private val admin = UserEntity( name = "A",
        username = "a",
        hashedPassword = "h",
        type = UserType.Admin)
    private val mate  = UserEntity(name = "M",
        username = "m",
        hashedPassword = "h",
        type = UserType.Mate)

    @Test fun `success when admin, valid inputs, unique`() {
        every { repo.findByUsername("u1") } returns null

        val created = uc("u1","secret", "Name", admin)

        assertEquals("u1",    created.username)
        assertEquals("Name",  created.name)
        assertEquals(UserType.Mate, created.type)
        verify(exactly=1) { repo.createUser(created) }
    }
    @Test
    fun `throws when non-admin`() {
        assertThrows<UnauthorizedException> {
            uc("user1", "goodPass", "Good Name", mate)
        }
    }

    @Test
    fun `throws for blank username`() {
        assertThrows<InvalidUsernameException> {
            uc("", "goodPass", "Good Name", admin)
        }
    }


    @Test
    fun `throws for bad username pattern`() {
        assertThrows<InvalidUsernameException> {
            uc("no spaces!", "goodPass", "Good Name", admin)
        }
    }

    @Test
    fun `throws for blank name`() {
        assertThrows<InvalidNameException> {
            uc("goodUser", "goodPass", "", admin)
        }
    }

    @Test
    fun `throws for short password`() {
        assertThrows<InvalidPasswordException> {
            uc("goodUser", "123", "Good Name", admin)
        }
    }

    @Test fun `throws when username exists`() {
        every { repo.findByUsername("dup") } returns UserEntity(id=UUID.randomUUID(),"X","dup","h",UserType.Mate)
        assertFailsWith<UserAlreadyExistsException> {
            uc("dup","passwd","N", admin)
        }
    }
    @Test
    fun `throws on duplicate username`() {
        every { repo.findByUsername("dup") } returns UserEntity(name="x",username="dup",hashedPassword="",type=UserType.Mate)
        assertThrows<UserAlreadyExistsException> {
            uc("dup", "goodPass", "Good Name", admin)
        }
    }
}
