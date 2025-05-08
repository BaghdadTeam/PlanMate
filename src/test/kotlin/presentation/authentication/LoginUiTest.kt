package presentation.authentication

import com.google.common.truth.Truth.assertThat
import helpers.authentication.SessionTestData
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.exceptions.InvalidCredentialsException
import org.baghdad.logic.usecase.authentication.LoginUseCase
import org.baghdad.presentation.authentication.LoginUi
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class LoginUiTest {

    private lateinit var useCase: LoginUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var loginUi: LoginUi

    @BeforeEach
    fun setUp() {
        useCase = mockk()
        viewer = mockk(relaxed = true)
        reader = mockk()
        loginUi = LoginUi(useCase, viewer, reader)
    }

    @ParameterizedTest
    @CsvSource("admin,1234", "user,pass123", "john,doe")
    fun `execute() returns SessionEntity on successful login`(username: String, password: String) {
        // Given
        every { reader.readInput() } returnsMany listOf(username, password)
        coEvery { useCase(username, password) } returns SessionTestData.baseSession
        // When
        val result = loginUi.execute()
        // Then
        assertThat(result.id).isEqualTo(SessionTestData.baseSession.id)
        verify { viewer.logMessage("Successfully logged in as $username") }
    }

    @Test
    fun `execute() loops on failed login and succeeds later`() {
        // Given
        every { reader.readInput() } returnsMany listOf("wrong", "IncorrectPass", "admin", "1234")
        coEvery { useCase("wrong", "IncorrectPass") } throws InvalidCredentialsException("Invalid login")
        coEvery { useCase("admin", "1234") } returns  SessionTestData.baseSession
        // When
        val result = loginUi.execute()
        // Then
        assertThat(result).isEqualTo(SessionTestData.baseSession)
        verify { viewer.logMessage("Successfully logged in as admin") }
    }

    @ParameterizedTest
    @CsvSource(
        "null, password123",
        "admin, null",
        "null, null"
    )
    fun `execute() handles null inputs for username or password`(rawUsername: String?, rawPassword: String?) {
        val username = if (rawUsername == "null") null else rawUsername
        val password = if (rawPassword == "null") null else rawPassword
        // Given
        every { reader.readInput() } returnsMany listOf(username, password, "admin", "1234")
        coEvery { useCase("admin", "1234") } returns SessionTestData.baseSession
        // When
        val result = loginUi.execute()
        // Then
        verify { viewer.logMessage("Username and password cannot be null") }
        assertThat(result).isEqualTo(SessionTestData.baseSession)
    }

    @ParameterizedTest
    @CsvSource(
        "wrong1, IncorrectPass1, Invalid 1",
        "wrong2, IncorrectPass2, Invalid 2"
    )
    fun `execute() handles failed login with retry`(
        firstUsername: String,
        firstPassword: String,
        errorMessage: String
    ) {
        // Given
        every { reader.readInput() } returnsMany listOf(firstUsername, firstPassword, "admin", "1234")
        coEvery { useCase(firstUsername, firstPassword) } throws InvalidCredentialsException(errorMessage)
        coEvery { useCase("admin", "1234") } returns SessionTestData.baseSession
        // When
        val result = loginUi.execute()
        // Then
        verify { viewer.logMessage("Login failed: $errorMessage") }
        verify { viewer.logMessage("Successfully logged in as admin") }
        assertThat(result).isEqualTo(SessionTestData.baseSession)
    }
}