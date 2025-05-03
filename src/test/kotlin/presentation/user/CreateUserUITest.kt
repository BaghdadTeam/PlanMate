package org.baghdad.presentation.user

import io.mockk.verify
import io.mockk.every
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.usecase.user.CreateUserUseCase
import org.baghdad.presentation.input.Reader
import helpers.task.user.FakeReader
import helpers.task.user.FakeViewer

class CreateUserUITest {
    private lateinit var reader: Reader
    private lateinit var viewer: FakeViewer
    private lateinit var useCase: CreateUserUseCase
    private lateinit var userUI: CreateUserUI

    private val admin = UserEntity(
        name = "Admin",
        username = "admin",
        hashedPassword = "h",
        type = UserType.Admin
    )
    private val mate = UserEntity(
        name = "Mate",
        username = "mate",
        hashedPassword = "h",
        type = UserType.Mate
    )

    @BeforeTest
    fun setup() {
        useCase = mockk(relaxed = true)
    }

    @Test
    fun `denies null currentUser`() {
        reader = FakeReader()
        viewer = FakeViewer()
        userUI = CreateUserUI(reader, viewer, useCase)

        userUI.run(null)

        assertEquals(1, viewer.errors.size)
        assertTrue(viewer.errors[0].contains("Only administrators"))
        assertTrue(viewer.messages.isEmpty())
    }

    @Test
    fun `denies non-admin access`() {
        reader = FakeReader()
        viewer = FakeViewer()
        userUI = CreateUserUI(reader, viewer, useCase)

        userUI.run(mate)

        assertEquals(1, viewer.errors.size)
        assertTrue(viewer.errors[0].contains("Only administrators"))
        assertTrue(viewer.messages.isEmpty())
    }

    @Test
    fun `success path logs messages`() {
        reader = FakeReader("u1", "Name1", "pass1")
        viewer = FakeViewer()
        every { useCase.invoke("u1", "pass1", "Name1", admin) } returns Result.success(
            UserEntity(name = "Name1", username = "u1", hashedPassword = "xx", type = UserType.Mate)
        )
        userUI = CreateUserUI(reader, viewer, useCase)

        userUI.run(admin)

        assertTrue(viewer.messages[0].contains("=== Create New Mate ==="))
        assertTrue(viewer.messages.contains("Username: "))
        assertTrue(viewer.messages.contains("Name: "))
        assertTrue(viewer.messages.contains("Password: "))
        assertTrue(viewer.messages.any { it.contains("User 'u1' created successfully.") })
        assertTrue(viewer.errors.isEmpty())
    }

    @Test
    fun `failure path logs error`() {
        reader = FakeReader("dup", "NameX", "passX")
        viewer = FakeViewer()
        every { useCase.invoke("dup", "passX", "NameX", admin) } returns Result.failure(
            Exception("duplicate username")
        )
        userUI = CreateUserUI(reader, viewer, useCase)

        userUI.run(admin)

        assertTrue(viewer.messages.contains("=== Create New Mate ==="))
        assertTrue(viewer.messages.contains("Username: "))
        assertTrue(viewer.messages.contains("Name: "))
        assertTrue(viewer.messages.contains("Password: "))
        assertEquals(1, viewer.errors.size)
        assertTrue(viewer.errors[0].contains("duplicate username"))
    }

    @Test
    fun `prompt handles null input as empty string`() {
        reader = FakeReader(null, "NameX", "passX")
        viewer = FakeViewer()

        every { useCase.invoke("", "passX", "NameX", admin) } returns Result.success(
            UserEntity(name = "NameX", username = "", hashedPassword = "x", type = UserType.Mate)
        )

        userUI = CreateUserUI(reader, viewer, useCase)
        userUI.run(admin)


        verify {
            useCase.invoke("", "passX", "NameX", admin)
            Unit
        }
    }


}
