package org.baghdad.presentation.user

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.InvalidNameException
import org.baghdad.logic.model.exceptions.user.InvalidPasswordException
import org.baghdad.logic.model.exceptions.user.InvalidUsernameException
import org.baghdad.logic.model.exceptions.user.UserAlreadyExistsException
import org.baghdad.logic.usecase.user.CreateUserUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class CreateUserUITest {
    private lateinit var reader: Reader
    private lateinit var viewer: Viewer
    private lateinit var uc: CreateUserUseCase
    private lateinit var ui: CreateUserUI

    private val admin = UserEntity(
        name = "Admin Name",
        username = "admin",
        hashedPassword = "h",
        type = UserType.Admin
    )
    private val mate = UserEntity(
        name = "Mate Name",
        username = "mate",
        hashedPassword = "h",
        type = UserType.Mate
    )

    @BeforeTest
    fun setup() {
        // اكتب mockk للـ use case و reader و viewer
        uc = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        ui = CreateUserUI(reader, viewer, uc)
    }

    @Test
    fun `deny non-admin`() {
        // Given
        // عند تشغيل run مع مستخدم ليس أدمن
        // When
        ui.run(mate)
        // Then
        verify(exactly = 1) {
            viewer.logError(match { it.contains("Only administrators") })
        }
    }

    @Test
    fun `happy path`() {
        // Given: الإدخالات الثلاثة العادية
        every { reader.readInput() } returnsMany listOf("u1", "Name1", "pass1")
        every { uc.invoke("u1", "pass1", "Name1", admin) } returns UserEntity(
            name = "Name1",
            username = "u1",
            hashedPassword = "h",
            type = UserType.Mate
        )
        // When
        ui.run(admin)
        // Then
        verify(atLeast = 1) {
            viewer.logMessage(match { it.contains("created successfully") })
        }
    }

    @Test
    fun `show username validation error`() {
        // Given: تحفّظ use case برمي InvalidUsernameException
        every { reader.readInput() } returnsMany listOf("", "NameX", "passX")
        every { uc.invoke(any(), any(), any(), any()) } throws InvalidUsernameException("bad username")
        // When
        ui.run(admin)
        // Then
        verify(exactly = 1) {
            viewer.logError(match { it.contains("Invalid username") })
        }
    }

    @Test
    fun `show name validation error`() {
        // Given
        every { reader.readInput() } returnsMany listOf("uX", "", "passX")
        every { uc.invoke(any(), any(), any(), any()) } throws InvalidNameException("bad name")
        // When
        ui.run(admin)
        // Then
        verify(exactly = 1) {
            viewer.logError(match { it.contains("Invalid name") })
        }
    }

    @Test
    fun `show password validation error`() {
        // Given
        every { reader.readInput() } returnsMany listOf("uX", "NameX", "123")
        every { uc.invoke(any(), any(), any(), any()) } throws InvalidPasswordException("too short")
        // When
        ui.run(admin)
        // Then
        verify(exactly = 1) {
            viewer.logError(match { it.contains("Invalid password") })
        }
    }

    @Test
    fun `show already-exists error`() {
        // Given
        every { reader.readInput() } returnsMany listOf("dup", "NameX", "passX")
        every { uc.invoke(any(), any(), any(), any()) } throws UserAlreadyExistsException("dup")
        // When
        ui.run(admin)
        // Then
        verify(exactly = 1) {
            viewer.logError(match { it.contains("dup") })
        }
    }
}
