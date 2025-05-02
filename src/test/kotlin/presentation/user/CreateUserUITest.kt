package presentation.user


import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.UserAlreadyExistsException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.usecase.user.CreateUserUseCase
import org.baghdad.presentation.user.CreateUserUI
import test.helpers.FakeConsole
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertTrue

class CreateUserUITest {
    private val createUC = mockk<CreateUserUseCase>()
    private lateinit var console: FakeConsole
    private lateinit var ui: CreateUserUI

    @Test
    fun `denies non-admin`() {
        console = FakeConsole()
        ui = CreateUserUI(console, createUC)
        ui.run(null)
        assertTrue(console.outputs.contains("🚫 Only administrators can create new users."))
    }

    @Test
    fun `handles duplicate username`() {
        val admin = UserEntity("A","a","","",UserType.Admin)
        every { createUC.invoke("u","p","n",admin) } throws UserAlreadyExistsException("dup")
        console = FakeConsole("u","n","p")
        ui = CreateUserUI(console, createUC)
        ui.run(admin)
        assertTrue(console.outputs.any { it.contains("dup") })
    }

    @Test
    fun `success path`() {
        val admin = UserEntity("A","a","","",UserType.Admin)
        val created = UserEntity("C","c","","",UserType.Mate)
        every { createUC.invoke("u","p","n",admin) } returns created
        console = FakeConsole("u","n","p")
        ui = CreateUserUI(console, createUC)
        ui.run(admin)
        assertTrue(console.outputs.any { it.contains("created successfully") })
    }
}