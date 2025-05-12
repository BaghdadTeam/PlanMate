package logic.usecase


import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.UnauthorizedException
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdminPermissionCheckerUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val isAdminUseCase = AdminPermissionCheckerUseCase(userRepository)

    @Test
    fun `should return true when the use is admin`() = runTest {
        // Given
        val adminUserId = UUID.randomUUID()
        val adminUser = UserEntity(
            id = adminUserId,
            type = UserType.Admin,
            username = "admin",
            hashedPassword = "hash",
            name = "Admin User"
        )

        coEvery { userRepository.getUserById(adminUserId) } returns adminUser

        // When
        val result = isAdminUseCase(adminUserId)

        // Then
        assertTrue(result)
    }

    @Test
    fun `should return false when the user is not admin`() = runTest {
        // Given
        val regularUserId = UUID.randomUUID()
        val regularUser = UserEntity(
            id = regularUserId,
            type = UserType.Mate,
            username = "regular",
            hashedPassword = "hash",
            name = "Regular User"
        )

        coEvery { userRepository.getUserById(regularUserId) } returns regularUser

        // When
        val result = isAdminUseCase(regularUserId)

        // Then
        assertFalse(result)
    }

    @Test
    fun `should return false when the user is not existed`() = runTest {
        // Given
        val nonExistentUserId = UUID.randomUUID()
        coEvery { userRepository?.getUserById(nonExistentUserId) } returns null

        // When
        val result = isAdminUseCase(nonExistentUserId)

        // Then
        assertFalse(result)
    }

    @Test
    fun `should not throw UnauthorizedException for admin user when ensureAdmin is called`() = runTest {
        // Given
        val adminUserId = UUID.randomUUID()
        val adminUser = UserEntity(
            id = adminUserId,
            type = UserType.Admin,
            username = "admin",
            hashedPassword = "hash",
            name = "Admin User"
        )

        coEvery { userRepository.getUserById(adminUserId) } returns adminUser

        // When & Then (should not throw)
        isAdminUseCase.validateAdminPermission(adminUserId)
    }

    @Test
    fun `should throw UbAuthorizeException for non-admin user when ensureAdmin is called`() = runTest {
        // Given
        val regularUserId = UUID.randomUUID()
        val regularUser = UserEntity(
            id = regularUserId,
            type = UserType.Mate,
            username = "regular",
            hashedPassword = "hash",
            name = "Regular User"
        )

        coEvery { userRepository.getUserById(regularUserId) } returns regularUser

        // When & Then
        assertThrows<UnauthorizedException> {
            isAdminUseCase.validateAdminPermission(regularUserId)
        }
    }

    @Test
    fun `ensureAdmin should throw for non-existent user`() = runTest {
        // Given
        val nonExistentUserId = UUID.randomUUID()
        coEvery { userRepository?.getUserById(nonExistentUserId) } returns null

        // When & Then
        assertThrows<UnauthorizedException> {
            isAdminUseCase.validateAdminPermission(nonExistentUserId)
        }
    }
}