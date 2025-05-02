package presentation.user

import org.baghdad.presentation.user.GetUserUI
import org.baghdad.presentation.Console
import org.baghdad.logic.usecase.user.GetUserByUsernameUseCase
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import test.helpers.FakeConsole
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertTrue

class GetUserUITest {
    private val getUC = mockk<GetUserByUsernameUseCase>()
    private lateinit var console: FakeConsole
    private lateinit var ui: GetUserUI

    @Test
    fun `prints not found`() {
        every { getUC.invoke("x") } returns null
        console = FakeConsole("x")
        ui = GetUserUI(console, getUC)
        ui.run()
        assertTrue(console.outputs.any { it.contains("not found") })
    }

    @Test
    fun `prints found`() {
        val u = UserEntity("N","n","","",UserType.Mate)
        every { getUC.invoke("n") } returns u
        console = FakeConsole("n")
        ui = GetUserUI(console, getUC)
        ui.run()
        assertTrue(console.outputs.any { it.contains("Found: n") })
    }
}
