package org.baghdad.logic.usecase.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.UnauthorizedException
import org.baghdad.logic.model.exceptions.user.UserAlreadyExistsException
import org.baghdad.logic.repositories.UserRepository

class CreateUserUseCaseTest {

    private val repository = mockk<UserRepository>(relaxed = true)
    private val useCase   = CreateUserUseCase(repository)

    private val admin = UserEntity(
        name = "Admin Name",
        username = "admin",
        hashedPassword = "ignored",
        type = UserType.Admin
    )
    private val mate = UserEntity(
        name = "Mate Name",
        username = "mate",
        hashedPassword = "ignored",
        type = UserType.Mate
    )

    @Test
    fun `invoke returns success for admin and unique username`() {
        // Given
        every { repository.existsByUsername("newUser") } returns false

        // When
        val result = useCase("newUser", "password", "New User", admin)

        // Then
        assertTrue(result.isSuccess, "Expected success Result")
        val created = result.getOrNull()!!
        assertEquals("newUser", created.username)
        assertEquals("New User", created.name)
        assertEquals(UserType.Mate, created.type)
        verify(exactly = 1) { repository.createUser(created) }
    }

    @Test
    fun `invoke returns failure for non-admin creator`() {
        // When
        val result = useCase("whatever", "password", "Name", mate)

        // Then
        assertTrue(result.isFailure, "Expected failure Result")
        val ex = result.exceptionOrNull()!!
        assertTrue(ex is UnauthorizedException)
        assertEquals("Only admins can create users.", ex.message)
        verify(exactly = 0) { repository.existsByUsername(any()) }
        verify(exactly = 0) { repository.createUser(any()) }
    }

    @Test
    fun `invoke returns failure for duplicate username`() {
        // Given
        every { repository.existsByUsername("dupUser") } returns true

        // When
        val result = useCase("dupUser", "password", "Name", admin)

        // Then
        assertTrue(result.isFailure, "Expected failure Result")
        val ex = result.exceptionOrNull()!!
        assertTrue(ex is UserAlreadyExistsException)
        assertEquals("Username 'dupUser' already exists.", ex.message)
        verify(exactly = 1) { repository.existsByUsername("dupUser") }
        verify(exactly = 0) { repository.createUser(any()) }
    }
}
