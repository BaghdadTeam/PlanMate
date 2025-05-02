package logic.usecase.user
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.UserAlreadyExistsException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.user.CreateUserUseCase
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class CreateUserUseCaseTest {
    private val repo = mockk<UserRepository>()
    private val uc = CreateUserUseCase(repo)

    private val admin = UserEntity(
        name = "A", username = "a", hashedPassword = "h", type = UserType.Admin
    )
    private val mate = UserEntity(
        name = "M", username = "m", hashedPassword = "h", type = UserType.Mate
    )

    @Test
    fun `success when admin and unique username`() {
        every { repo.existsByUsername("u") } returns false
        every { repo.createUser(any()) } returns Unit

        val created = uc("u", "p", "Name", admin)
        assert(created.username == "u")
        verify { repo.createUser(created) }
    }

    @Test
    fun `throws when non-admin`() {
        assertFailsWith<UnauthorizedException> {
            uc("u","p","Name", mate)
        }
    }

    @Test
    fun `throws when username exists`() {
        every { repo.existsByUsername("u") } returns true
        assertFailsWith<UserAlreadyExistsException> {
            uc("u","p","Name", admin)
        }
    }
}