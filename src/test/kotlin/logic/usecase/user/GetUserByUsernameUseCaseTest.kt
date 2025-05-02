package logic.usecase.user


import io.mockk.every
import io.mockk.mockk
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.user.GetUserByUsernameUseCase
import org.junit.jupiter.api.Test
import kotlin.test.assertNull
import kotlin.test.assertSame

class GetUserByUsernameUseCaseTest {
    private val repo = mockk<UserRepository>()
    private val uc = GetUserByUsernameUseCase(repo)

    @Test
    fun `returns user when found`() {
        val u = UserEntity(name="N",username="u",hashedPassword="",type=org.baghdad.logic.model.entities.UserType.Mate)
        every { repo.findByUsername("u") } returns u
        assertSame(u, uc("u"))
    }

    @Test
    fun `returns null when not found`() {
        every { repo.findByUsername("u") } returns null
        assertNull(uc("u"))
    }
}