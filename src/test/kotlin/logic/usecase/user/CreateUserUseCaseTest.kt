package logic.usecase.user


import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.usecase.user.CreateUserResult
import org.baghdad.logic.usecase.user.CreateUserUseCase
import org.baghdad.logic.repositories.UserRepository
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CreateUserUseCaseTest {
    private val repo = mockk<UserRepository>(relaxed = true)
    private val uc = CreateUserUseCase(repo)

    private val admin = UserEntity(name="A", username="a", hashedPassword="h", type=UserType.Admin)
    private val mate  = UserEntity(name="M", username="m", hashedPassword="h", type=UserType.Mate)

    @Test
    fun `returns Success when admin and unique username`() {
        every { repo.existsByUsername("u") } returns false

        val result = uc("u","p","Name", admin)
        assertIs<CreateUserResult.Success>(result)
        val created = (result as CreateUserResult.Success).user
        assertTrue(created.username == "u" && created.type == UserType.Mate)
        verify { repo.createUser(created) }
    }

    @Test
    fun `returns Failure when non-admin`() {
        val result = uc("u","p","Name", mate)
        assertIs<CreateUserResult.Failure>(result)
        assertTrue((result as CreateUserResult.Failure).error.contains("Only admins"))
    }

    @Test
    fun `returns Failure when username exists`() {
        every { repo.existsByUsername("u") } returns true

        val result = uc("u","p","Name", admin)
        assertIs<CreateUserResult.Failure>(result)
        assertTrue((result as CreateUserResult.Failure).error.contains("already exists"))
    }
}