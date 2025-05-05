// File: src/test/kotlin/org/baghdad/presentation/user/CreateUserUITest.kt
package org.baghdad.presentation.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.*
import org.baghdad.logic.usecase.user.CreateUserUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import kotlin.test.BeforeTest
import kotlin.test.Test

class CreateUserUITest {
    private lateinit var reader: Reader
    private lateinit var viewer: Viewer
    private lateinit var uc: CreateUserUseCase
    private lateinit var ui: CreateUserUI

    private val admin = UserEntity(name = "Admin", username = "admin", hashedPassword = "h", type = UserType.Admin)
    private val mate  = UserEntity(name = "Mate", username = "mate", hashedPassword = "h", type = UserType.Mate)

    @BeforeTest fun setup() {
        reader = mockk()
        viewer = mockk(relaxed = true)
        uc = mockk()
        ui = CreateUserUI(reader, viewer, uc)
    }

    @Test fun `non-admin is rejected`() {
        ui.run(mate)
        verify { viewer.logError("Only administrators can create new users.") }
    }

    @Test fun `happy path prints success`() {
        every { reader.readInput() } returnsMany listOf("u1","Name1","pass1")
        every { uc.invoke("u1","pass1","Name1", admin) } returns UserEntity(name = "Name1", username = "u1", hashedPassword = "h", type = UserType.Mate)
        ui.run(admin)
        verify { viewer.logMessage("User 'u1' created successfully.") }
    }

    @Test fun `invalid username error`() {
        every { reader.readInput() } returnsMany listOf("bad!","Name","pass6")
        every { uc.invoke(any(),any(),any(),any()) } throws InvalidUsernameException("must match pattern")
        ui.run(admin)
        verify { viewer.logError("Invalid username: must match pattern") }
    }

    @Test fun `invalid name error`() {
        every { reader.readInput() } returnsMany listOf("good","", "pass6")
        every { uc.invoke(any(),any(),any(),any()) } throws InvalidNameException("cannot be blank")
        ui.run(admin)
        verify { viewer.logError("Invalid name: cannot be blank") }
    }

    @Test fun `invalid password error`() {
        every { reader.readInput() } returnsMany listOf("good","Name","short")
        every { uc.invoke(any(),any(),any(),any()) } throws InvalidPasswordException("too short")
        ui.run(admin)
        verify { viewer.logError("Invalid password: too short") }
    }

    @Test fun `duplicate username error`() {
        every { reader.readInput() } returnsMany listOf("dup","Name","pass6")
        every { uc.invoke(any(),any(),any(),any()) } throws UserAlreadyExistsException("dup exists")
        ui.run(admin)
        verify { viewer.logError("dup exists") }
    }

    @Test fun `unauthorized exception on admin check`() {
        every { reader.readInput() } returnsMany listOf("u","N","password")
        every { uc.invoke(any(),any(),any(),any()) } throws UnauthorizedException("not admin")
        ui.run(admin)
        verify { viewer.logError("not admin") }
    }

    @Test fun `prompt labels always shown`() {
        every { reader.readInput() } returnsMany listOf("u","n","p")
        every { uc.invoke(any(),any(),any(),any()) } returns UserEntity(name = "n", username = "u", hashedPassword = "h", type = UserType.Mate)
        ui.run(admin)
        verify { viewer.logMessage("Username: ") }
        verify { viewer.logMessage("Name: ") }
        verify { viewer.logMessage("Password: ") }
    }
}
