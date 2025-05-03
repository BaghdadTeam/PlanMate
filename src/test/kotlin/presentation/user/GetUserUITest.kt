package org.baghdad.presentation.user

import io.mockk.every
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.usecase.user.GetUserByUsernameUseCase
import org.baghdad.presentation.input.Reader
import helpers.task.user.FakeReader
import helpers.task.user.FakeViewer

class GetUserUITest {
    private lateinit var reader: Reader
    private lateinit var viewer: FakeViewer
    private lateinit var useCase: GetUserByUsernameUseCase
    private lateinit var user: GetUserUI

    private val sampleUser = UserEntity(
        name = "Alice",
        username = "alice",
        hashedPassword = "hashed",
        type = UserType.Mate
    )

    @BeforeTest
    fun setup() {
        useCase = mockk(relaxed = true)
    }

    @Test
    fun `run shows success when user found`() {
        reader = FakeReader("alice")
        viewer = FakeViewer()
        every { useCase.invoke("alice") } returns Result.success(sampleUser)
        user = GetUserUI(reader, viewer, useCase)

        user.run()

        assertTrue(viewer.messages.any { it.contains("=== Find User ===") })
        assertTrue(viewer.messages.any {
            it.contains("Found: alice") &&
                    it.contains("Name: Alice") &&
                    it.contains("Role: Mate")
        })
    }

    @Test
    fun `run shows not found when user is missing`() {
        reader = FakeReader("bob")
        viewer = FakeViewer()
        every { useCase.invoke("bob") } returns Result.failure(Exception("not found"))
        user = GetUserUI(reader, viewer, useCase)

        user.run()

        assertTrue(viewer.messages.any { it.contains("=== Find User ===") })
        assertTrue(viewer.messages.any { it.contains("User 'bob' not found.") })
    }

    @Test
    fun `run handles null input as empty and shows not found`() {
        reader = FakeReader(null)
        viewer = FakeViewer()
        every { useCase.invoke("") } returns Result.failure(Exception("no user"))
        user = GetUserUI(reader, viewer, useCase)

        user.run()

        assertTrue(viewer.messages.any { it.contains("=== Find User ===") })
        assertTrue(viewer.messages.any { it.contains("User '' not found.") })
    }

    @Test
    fun `run trims whitespace and finds user`() {
        reader = FakeReader("   alice   ")
        viewer = FakeViewer()
        every { useCase.invoke("alice") } returns Result.success(sampleUser)
        user = GetUserUI(reader, viewer, useCase)

        user.run()

        assertTrue(viewer.messages.any {
            it.contains("Found: alice") &&
                    it.contains("Name: Alice") &&
                    it.contains("Role: Mate")
        })
    }

    @Test
    fun `run shows not found when input is empty string`() {
        reader = FakeReader("")
        viewer = FakeViewer()
        every { useCase.invoke("") } returns Result.failure(Exception("no user"))
        user = GetUserUI(reader, viewer, useCase)

        user.run()

        assertTrue(viewer.messages.any { it.contains("User '' not found.") })
    }

    @Test
    fun `run shows not found when input is whitespace only`() {
        reader = FakeReader("   ")
        viewer = FakeViewer()
        every { useCase.invoke("") } returns Result.failure(Exception("no user"))
        user = GetUserUI(reader, viewer, useCase)

        user.run()

        assertTrue(viewer.messages.any { it.contains("User '' not found.") })
    }

    @Test
    fun `run passes internal spaces as-is to use case`() {
        reader = FakeReader("ali ce")
        viewer = FakeViewer()
        every { useCase.invoke("ali ce") } returns Result.failure(Exception("not found"))
        user = GetUserUI(reader, viewer, useCase)

        user.run()

        assertTrue(viewer.messages.any { it.contains("User 'ali ce' not found.") })
    }
}
