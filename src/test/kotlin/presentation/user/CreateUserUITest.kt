package presentation.user

import io.mockk.every
import io.mockk.mockk
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.usecase.user.CreateUserResult
import org.baghdad.logic.usecase.user.CreateUserUseCase
import org.baghdad.presentation.user.CreateUserUI
import test.helpers.FakeConsole
import kotlin.test.Test
import kotlin.test.assertTrue

class CreateUserUITest {
    private val uc = mockk<CreateUserUseCase>()
    private lateinit var console: FakeConsole
    private lateinit var ui: CreateUserUI
    private val admin = UserEntity(name="A", username="a", hashedPassword="", type=UserType.Admin)
    private val mate  = UserEntity(name="M", username="m", hashedPassword="", type=UserType.Mate)

    @Test
    fun `denies non-admin access`() {
        console = FakeConsole()
        ui = CreateUserUI(console, uc)
        ui.run(mate)
        assertTrue(console.outputs.contains("🚫 Only administrators can create new users."))
    }

    @Test
    fun `shows error on duplicate username`() {
        every { uc.invoke("u","p","n", admin) } returns CreateUserResult.Failure("dup")
        console = FakeConsole("u","n","p")
        ui = CreateUserUI(console, uc)
        ui.run(admin)
        assertTrue(console.outputs.any { it.contains("dup") })
    }

    @Test
    fun `shows success message on create`() {
        val created = UserEntity(name="C", username="c", hashedPassword="", type=UserType.Mate)
        every { uc.invoke("c","pass","Name", admin) } returns CreateUserResult.Success(created)
        console = FakeConsole("c","Name","pass")
        ui = CreateUserUI(console, uc)
        ui.run(admin)
        assertTrue(console.outputs.any { it.contains("c created successfully") })
    }
}
