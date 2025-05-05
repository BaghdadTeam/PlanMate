// File: src/test/kotlin/org/baghdad/presentation/user/GetUserByUsernameUITest.kt
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
    private lateinit var uc: GetUserByUsernameUseCase
    private lateinit var ui: GetUserByUsernameUI

    private val sample = UserEntity(name = "Alice", username = "alice", hashedPassword = "h", type = UserType.Mate)

    @BeforeTest fun setup() {
        reader = mockk()
        viewer = mockk(relaxed = true)
        uc = mockk()
        ui = GetUserByUsernameUI(reader, viewer, uc)
    }

    @Test fun `shows success`() {
        every { reader.readInput() } returns "alice"
        every { uc.invoke("alice") } returns sample
        ui.run()
        verify { viewer.logMessage("Found: alice  Name: Alice  Role: Mate") }
    }

    @Test fun `empty input treated as invalid username`() {
        every { reader.readInput() } returns ""
        every { uc.invoke("") } throws InvalidUsernameException("blank")
        ui.run()
        verify { viewer.logError("Invalid username: blank") }
    }

    @Test fun `invalid username exception`() {
        every { reader.readInput() } returns "x!"
        every { uc.invoke("x!") } throws InvalidUsernameException("bad char")
        ui.run()
        verify { viewer.logError("Invalid username: bad char") }
    }

    @Test fun `user not found exception`() {
        every { reader.readInput() } returns "bob"
        every { uc.invoke("bob") } throws UserNotFoundException("bob not found")
        ui.run()
        verify { viewer.logError("bob not found") }
    }

    @Test fun `prompt and header always shown`() {
        every { reader.readInput() } returns "u"
        every { uc.invoke("u") } returns sample
        ui.run()
        verify { viewer.logMessage("=== Find User ===") }
        verify { viewer.logMessage("Please enter the username:") }
    }
}
