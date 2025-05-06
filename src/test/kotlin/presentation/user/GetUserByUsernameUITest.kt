package org.baghdad.presentation.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.InvalidUsernameException
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.baghdad.logic.usecase.user.GetUserByUsernameUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import kotlin.test.BeforeTest
import kotlin.test.Test

class GetUserByUsernameUITest {

    private lateinit var reader: Reader
    private lateinit var viewer: Viewer
    private lateinit var getUserByUsernameUseCase: GetUserByUsernameUseCase
    private lateinit var getUserInterface: GetUserByUsernameUI

    private val sampleUser = UserEntity(
        name           = "Alice",
        username       = "alice",
        hashedPassword = "hashed",
        type           = UserType.Mate
    )

    @BeforeTest
    fun setup() {
        reader                     = mockk()
        viewer                     = mockk(relaxed = true)
        getUserByUsernameUseCase   = mockk()
        getUserInterface           = GetUserByUsernameUI(reader, viewer, getUserByUsernameUseCase)
    }

    @Test
    fun `success path prints user details`() {
        // Given
        every { reader.readInput() } returns "alice"
        every { getUserByUsernameUseCase.invoke("alice") } returns sampleUser
        // When
        getUserInterface.run()
        // Then
        verify { viewer.logMessage("=== Find User ===") }
        verify { viewer.logMessage("Please enter the username:") }
        verify { viewer.logMessage("Found: alice  Name: Alice  Role: Mate") }
    }

    @Test
    fun `blank input throws invalid username error`() {
        // Given
        every { reader.readInput() } returns ""
        every { getUserByUsernameUseCase.invoke("") } throws InvalidUsernameException("must not be empty")
        // When
        getUserInterface.run()
        // Then
        verify { viewer.logError("Invalid username: must not be empty") }
    }

    @Test
    fun `input with spaces is trimmed`() {
        // Given
        every { reader.readInput() } returns "  alice  "
        every { getUserByUsernameUseCase.invoke("alice") } returns sampleUser
        // When
        getUserInterface.run()
        // Then
        verify { viewer.logMessage("Found: alice  Name: Alice  Role: Mate") }
    }

    @Test
    fun `user not found error with message`() {
        // Given
        every { reader.readInput() } returns "bob"
        every { getUserByUsernameUseCase.invoke("bob") } throws UserNotFoundException("bob not found")
        // When
        getUserInterface.run()
        // Then
        verify { viewer.logError("bob not found") }
    }

    @Test
    fun `user not found with empty message uses default`() {
        // Given
        every { reader.readInput() } returns null
        every { getUserByUsernameUseCase.invoke("") } throws UserNotFoundException("")
        // When
        getUserInterface.run()
        // Then
        verify { viewer.logError("User not found.") }
    }


}
