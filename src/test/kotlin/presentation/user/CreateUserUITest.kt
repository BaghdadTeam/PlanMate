package org.baghdad.presentation.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.InvalidNameException
import org.baghdad.logic.model.exceptions.user.InvalidPasswordException
import org.baghdad.logic.model.exceptions.user.InvalidUsernameException
import org.baghdad.logic.model.exceptions.user.UnauthorizedException
import org.baghdad.logic.model.exceptions.user.UserAlreadyExistsException
import org.baghdad.logic.usecase.user.CreateUserUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test

class CreateUserUITest {

    private lateinit var reader: Reader
    private lateinit var viewer: Viewer
    private lateinit var createUserUseCase: CreateUserUseCase
    private lateinit var createUserInterface: CreateUserUI

    private val administrator = UserEntity(
        id = UUID.randomUUID(),
        name = "Administrator",
        username = "admin",
        hashedPassword = "hashed",
        type = UserType.Admin
    )
    private val regularMate = UserEntity(
        id = UUID.randomUUID(),
        name = "Regular Mate",
        username = "mate",
        hashedPassword = "hashed",
        type = UserType.Mate
    )

    @BeforeTest
    fun setup() {
        reader = mockk()
        viewer = mockk(relaxed = true)
        createUserUseCase = mockk(relaxed = true)
        createUserInterface = CreateUserUI(reader, viewer, createUserUseCase)
    }

    private fun setReaderInputs(vararg inputs: String?) {
        every { reader.readInput() } returnsMany inputs.toList()
    }

    private fun configureUseCaseSuccess() {
        every {
            createUserUseCase.invoke(any(), any(), any(), any())
        } returns administrator
    }

    private fun configureUseCaseToThrow(exception: Exception) {
        every {
            createUserUseCase.invoke(any(), any(), any(), any())
        } throws exception
    }

    @Test
    fun `non administrator is rejected`() {
        // Given nothing
        // When
        createUserInterface.run(regularMate)
        // Then
        verify { viewer.logError("Only administrators can create new users.") }
    }

    @Test
    fun `null current user is rejected`() {
        // Given nothing
        // When
        createUserInterface.run(null)
        // Then
        verify { viewer.logError("Only administrators can create new users.") }
    }

    @Test
    fun `prompt treats null input as empty`() {
        // Given
        setReaderInputs(null, null, null)
        configureUseCaseSuccess()
        // When
        createUserInterface.run(administrator)
        // Then
        verify { viewer.logMessage("Username: ") }
        verify { viewer.logMessage("Name: ") }
        verify { viewer.logMessage("Password: ") }
    }

    @Test
    fun `successful creation prints confirmation`() {
        // Given
        setReaderInputs("user123", "Full Name", "securePass")
        configureUseCaseSuccess()
        // When
        createUserInterface.run(administrator)
        // Then
        verify { viewer.logMessage("User 'admin' created successfully.") }
    }

    @Test
    fun `invalid username error is shown`() {
        // Given
        setReaderInputs("bad user", "Name", "password")
        configureUseCaseToThrow(InvalidUsernameException("must match pattern"))
        // When
        createUserInterface.run(administrator)
        // Then
        verify { viewer.logError("Invalid username: must match pattern") }
    }

    @Test
    fun `invalid name error is shown`() {
        // Given
        setReaderInputs("user123", "", "password")
        configureUseCaseToThrow(InvalidNameException("cannot be blank"))
        // When
        createUserInterface.run(administrator)
        // Then
        verify { viewer.logError("Invalid name: cannot be blank") }
    }

    @Test
    fun `invalid password error is shown`() {
        // Given
        setReaderInputs("user123", "Name", "short")
        configureUseCaseToThrow(InvalidPasswordException("too short"))
        // When
        createUserInterface.run(administrator)
        // Then
        verify { viewer.logError("Invalid password: too short") }
    }

    @Test
    fun `duplicate username error is shown`() {
        // Given
        setReaderInputs("user123", "Name", "password")
        configureUseCaseToThrow(UserAlreadyExistsException("username exists"))
        // When
        createUserInterface.run(administrator)
        // Then
        verify { viewer.logError("username exists") }
    }

    @Test
    fun `unauthorized exception during creation is shown`() {
        // Given
        setReaderInputs("user123", "Name", "password")
        configureUseCaseToThrow(UnauthorizedException("not allowed"))
        // When
        createUserInterface.run(administrator)
        // Then
        verify { viewer.logError("not allowed") }
    }
}
