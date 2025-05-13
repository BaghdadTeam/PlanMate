package presentation.user

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.*
import org.baghdad.logic.usecase.user.CreateUserUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.user.CreateUserUI
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class CreateUserUITest {

    private lateinit var reader: Reader
    private lateinit var viewer: Viewer
    private lateinit var createUserUseCase: CreateUserUseCase
    private lateinit var createUserInterface: CreateUserUI
    private lateinit var session: SessionManager

    private val administrator = UserEntity(
        id = UUID.randomUUID(),
        name = "Administrator",
        username = "admin",
        type = UserType.Admin
    )
    private val regularMate = UserEntity(
        id = UUID.randomUUID(),
        name = "Regular Mate",
        username = "mate",
        type = UserType.Mate
    )

    @BeforeTest
    fun setup() {
        reader = mockk()
        viewer = mockk(relaxed = true)
        session = mockk()
        createUserUseCase = mockk(relaxed = true)
        createUserInterface = CreateUserUI(reader, viewer, createUserUseCase , session)
    }

    private fun setReaderInputs(vararg inputs: String?) {
        every { session.currentSession.userId } returns regularMate.id
        every { reader.readInput() } returnsMany inputs.toList()
    }

    private fun configureUseCaseSuccess() {
        coEvery {
            createUserUseCase.invoke(any(), any(), any(), any())
        } returns administrator
    }

    private fun configureUseCaseToThrow(exception: Exception) {
        every { session.currentSession.userId } returns regularMate.id
        coEvery {
            createUserUseCase.invoke(any(), any(), any(), any())
        } throws exception
    }


    @Test
    fun `successful creation prints confirmation`() = runTest {
        // Given
        every { session.currentSession.userId } returns regularMate.id
        setReaderInputs("user123", "Full Name", "securePass")
        configureUseCaseSuccess()
        // When
        createUserInterface()
        // Then
        verify { viewer.logMessage("User 'admin' created successfully.") }
    }

    @Test
    fun `invalid username error is shown`() = runTest {
        // Given
        every { session.currentSession.userId } returns regularMate.id
        setReaderInputs("bad user", "Name", "password")
        configureUseCaseToThrow(InvalidUsernameException("must match pattern"))
        // When
        createUserInterface()
        // Then
        verify { viewer.logError("Invalid username: must match pattern") }
    }

    @Test
    fun `invalid name error is shown`() = runTest {
        // Given
        every { session.currentSession.userId } returns regularMate.id
        setReaderInputs("user123", "", "password")
        configureUseCaseToThrow(InvalidNameException("cannot be blank"))
        // When
        createUserInterface()
        // Then
        verify { viewer.logError("Invalid name: cannot be blank") }
    }

    @Test
    fun `invalid password error is shown`() = runTest {
        // Given
        every { session.currentSession.userId } returns regularMate.id
        setReaderInputs("user123", "Name", "short")
        configureUseCaseToThrow(InvalidPasswordException("too short"))
        // When
        createUserInterface()
        // Then
        verify { viewer.logError("Invalid password: too short") }
    }

    @Test
    fun `duplicate username error is shown`() = runTest {
        // Given
        every { session.currentSession.userId } returns regularMate.id
        setReaderInputs("user123", "Name", "password")
        configureUseCaseToThrow(UserAlreadyExistsException("username exists"))
        // When
        createUserInterface()
        // Then
        verify { viewer.logError("username exists") }
    }
}
