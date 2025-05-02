package presentation.user

import io.mockk.every
import io.mockk.mockk
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.usecase.user.GetUserResult
import org.baghdad.logic.usecase.user.GetUserByUsernameUseCase
import org.baghdad.presentation.user.GetUserUI
import test.helpers.FakeConsole
import kotlin.test.Test
import kotlin.test.assertTrue

class GetUserUITest {
    private val uc = mockk<GetUserByUsernameUseCase>()
    private lateinit var console: FakeConsole
    private lateinit var ui: GetUserUI

    @Test
    fun `prints not found message`() {
        every { uc.invoke("x") } returns GetUserResult.NotFound
        console = FakeConsole("x")
        ui = GetUserUI(console, uc)
        ui.run()
        assertTrue(console.outputs.any { it.contains("not found") })
    }

    @Test
    fun `prints user details when found`() {
        val u = UserEntity(name="Name", username="u", hashedPassword="", type=UserType.Mate)
        every { uc.invoke("u") } returns GetUserResult.Success(u)
        console = FakeConsole("u")
        ui = GetUserUI(console, uc)
        ui.run()
        assertTrue(console.outputs.any { it.contains("Found: u") })
    }
}
