package presentation.authentication

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.usecase.authentication.LogoutUseCase
import org.baghdad.presentation.authentication.LogoutUi
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class LogoutUiTest {

    private lateinit var useCase: LogoutUseCase
    private lateinit var reader: Reader
    private lateinit var viewer: Viewer
    private lateinit var logoutUi: LogoutUi

    @BeforeEach
    fun setUp() {
        useCase = mockk(relaxed = true)
        reader = mockk()
        viewer = mockk(relaxed = true)
        logoutUi = LogoutUi(useCase, reader, viewer)
    }

    @Test
    fun `execute() should call logout use case when user confirms with y`() {
        // Given
        val fakeExit = mockk<() -> Unit>(relaxed = true)
        every { reader.readInput() } returns "y"
        val logoutUi = LogoutUi(useCase, reader, viewer, fakeExit)
        // When
        logoutUi.execute()

        // Then
        coVerify { useCase.invoke() }
        verify { viewer.logMessage("Are you sure you want to logout (y/n)?") }
        verify { fakeExit.invoke() }
    }

    @ParameterizedTest
    @CsvSource(
        "n",
        "no",
        "yes",
    )
    fun `execute() should not call logout use case when user input is not contain y`(input: String) {
        // Given
        every { reader.readInput() } returns input
        // When
        logoutUi.execute()
        // Then
        coVerify(exactly = 0) { useCase.invoke() }
    }

    @Test
    fun `execute() should not call logout use case when user input is null`() {
        // Given
        every { reader.readInput() } returns null
        // When
        logoutUi.execute()

        // Then
        coVerify(exactly = 0) { useCase.invoke() }
    }
}