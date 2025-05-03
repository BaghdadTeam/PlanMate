// File: src/test/kotlin/org/baghdad/presentation/PlanMateCLITest.kt
package org.baghdad.presentation

import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertTrue
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.user.CreateUserUI
import org.baghdad.presentation.user.GetUserUI
import org.baghdad.logic.model.entities.UserType

// --- Fake Implementations ---
class FakeReader(vararg inputs: String?) : Reader {
    private val queue = inputs.iterator()
    override fun readInput(): String? =
        if (queue.hasNext()) queue.next() else null
}

class FakeViewer : Viewer {
    val messages = mutableListOf<String>()
    override fun logMessage(msg: String) { messages += msg }
    override fun logError(msg: String)   { messages += msg }
}

class PlanMateCLITest {

    private fun makeCli(
        inputs: List<String?>,
        createUi: CreateUserUI,
        getUi: GetUserUI
    ): Pair<PlanMateCLI, FakeViewer> {
        val reader = FakeReader(*inputs.toTypedArray())
        val viewer = FakeViewer()
        val cli = PlanMateCLI(reader, viewer, createUi, getUi)
        return cli to viewer
    }

    @Test
    fun `branch 1 invokes CreateUserUI`() {
        // Given
        val createUi = mockk<CreateUserUI>(relaxed = true)
        val getUi = mockk<GetUserUI>(relaxed = true)

        // When
        val (cli, _) = makeCli(listOf("1", "3"), createUi, getUi)
        cli.start()

        // Then
        verify { createUi.run(match { it?.type == UserType.Admin }) }
    }

    @Test
    fun `branch 2 invokes GetUserUI`() {
        // Given
        val createUi = mockk<CreateUserUI>(relaxed = true)
        val getUi = mockk<GetUserUI>(relaxed = true)

        // When
        val (cli, _) = makeCli(listOf("2", "3"), createUi, getUi)
        cli.start()

        // Then
        verify { getUi.run() }
    }

    @Test
    fun `branch 3 prints Goodbye`() {
        // Given
        val createUi = mockk<CreateUserUI>(relaxed = true)
        val getUi = mockk<GetUserUI>(relaxed = true)

        // When: user selects option 3
        val (cli, viewer) = makeCli(listOf("3"), createUi, getUi)
        cli.start()

        // Then
        assertTrue(viewer.messages.any { it.contains("Goodbye!") })
    }

    @Test
    fun `null input is invalid`() {
        // Given
        val createUi = mockk<CreateUserUI>(relaxed = true)
        val getUi = mockk<GetUserUI>(relaxed = true)

        // When
        val (cli, viewer) = makeCli(listOf(null, "3"), createUi, getUi)
        cli.start()

        // Then
        assertTrue(viewer.messages.any { it.contains("Invalid choice.") })
    }

    @Test
    fun `empty input is invalid`() {
        // Given
        val createUi = mockk<CreateUserUI>(relaxed = true)
        val getUi = mockk<GetUserUI>(relaxed = true)

        // When
        val (cli, viewer) = makeCli(listOf("", "3"), createUi, getUi)
        cli.start()

        // Then
        assertTrue(viewer.messages.any { it.contains("Invalid choice.") })
    }

    @Test
    fun `other invalid input is invalid`() {
        // Given
        val createUi = mockk<CreateUserUI>(relaxed = true)
        val getUi = mockk<GetUserUI>(relaxed = true)

        // When
        val (cli, viewer) = makeCli(listOf("foo", "3"), createUi, getUi)
        cli.start()

        // Then
        assertTrue(viewer.messages.any { it.contains("Invalid choice.") })
    }
}
