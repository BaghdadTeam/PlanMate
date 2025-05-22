package presentation.user

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.InvalidUsernameException
import org.baghdad.logic.model.exceptions.UserNotFoundException
import org.baghdad.logic.usecase.user.GetUserByUsernameUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.user.GetUserByUsernameUI
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
    fun `success path prints user details`() = runTest {
        // Given
        every { reader.readInput() } returns "alice"
        coEvery { getUserByUsernameUseCase.invoke("alice") } returns sampleUser
        // When
        getUserInterface.run()
        // Then
        verify { viewer.logMessage("=== Find User ===") }
        verify { viewer.logMessage("Please enter the username:") }
        verify { viewer.logMessage("Found: alice  Name: Alice  Role: Mate") }
    }

    @Test
    fun `blank input throws invalid username error`() = runTest {
        // Given
        every { reader.readInput() } returns ""
        coEvery { getUserByUsernameUseCase.invoke("") } throws InvalidUsernameException()
        // When
        getUserInterface.run()
        // Then
        verify { viewer.logError("Invalid username") }
    }

    @Test
    fun `input with spaces is trimmed`() = runTest {
        // Given
        every { reader.readInput() } returns "  alice  "
        coEvery { getUserByUsernameUseCase.invoke("alice") } returns sampleUser
        // When
        getUserInterface.run()
        // Then
        verify { viewer.logMessage("Found: alice  Name: Alice  Role: Mate") }
    }

    @Test
    fun `user not found error with message`() = runTest {
        // Given
        every { reader.readInput() } returns "bob"
        coEvery { getUserByUsernameUseCase.invoke("bob") } throws UserNotFoundException()
        // When
        getUserInterface.run()
        // Then
        verify { viewer.logError("User not found.") }
    }

    @Test
    fun `user not found with empty message uses default`() = runTest {
        // Given
        every { reader.readInput() } returns null
        coEvery { getUserByUsernameUseCase.invoke("") } throws UserNotFoundException()
        // When
        getUserInterface.run()
        // Then
        verify { viewer.logError("User not found.") }
    }


}
