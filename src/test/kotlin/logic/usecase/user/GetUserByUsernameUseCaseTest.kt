package logic.usecase.user



import io.mockk.every
import io.mockk.mockk
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.usecase.user.GetUserResult
import org.baghdad.logic.usecase.user.GetUserByUsernameUseCase
import org.baghdad.logic.repositories.UserRepository
import kotlin.test.Test
import kotlin.test.assertIs

class GetUserByUsernameUseCaseTest {
    private val repo = mockk<UserRepository>()
    private val uc = GetUserByUsernameUseCase(repo)

    @Test
    fun `returns Success when user exists`() {
        val u = UserEntity(name="N", username="n", hashedPassword="", type=UserType.Mate)
        every { repo.findByUsername("n") } returns u

        val result = uc("n")
        assertIs<GetUserResult.Success>(result)
        assert((result as GetUserResult.Success).user === u)
    }

    @Test
    fun `returns NotFound when user does not exist`() {
        every { repo.findByUsername("x") } returns null

        val result = uc("x")
        assertIs<GetUserResult.NotFound>(result)
    }
}