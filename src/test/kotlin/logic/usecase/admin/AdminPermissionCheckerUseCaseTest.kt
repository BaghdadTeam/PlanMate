package logic.usecase.admin

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import org.junit.jupiter.api.Test
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
            name = "Regular User"
        )

        coEvery { userRepository.getUserById(regularUserId) } returns regularUser

        // When
        val result = isAdminUseCase(regularUserId)

        // Then
        assertFalse(result)
    }

    @Test
    fun `should return true for admin user when ensureAdmin is called`() = runTest {
        // Given
        val adminUserId = UUID.randomUUID()
        val adminUser = UserEntity(
            id = adminUserId,
            type = UserType.Admin,
            username = "admin",
            name = "Admin User"
        )

        coEvery { userRepository.getUserById(adminUserId) } returns adminUser

        // When
        val result = isAdminUseCase(adminUserId)

        // Then
        assertThat(result).isTrue()

    }

    @Test
    fun `should return false for non-admin user when ensureAdmin is called`() = runTest {
        // Given
        val regularUserId = UUID.randomUUID()
        val regularUser = UserEntity(
            id = regularUserId,
            type = UserType.Mate,
            username = "regular",
            name = "Regular User"
        )

        coEvery { userRepository.getUserById(regularUserId) } returns regularUser

        // When
        val result =  isAdminUseCase(regularUserId)

        // Then
        assertThat(result).isFalse()

    }

}