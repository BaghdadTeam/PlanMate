package presentation.user

import org.baghdad.presentation.PlanMateCLI
import org.baghdad.presentation.Console
import org.baghdad.presentation.user.CreateUserUI
import org.baghdad.presentation.user.GetUserUI
import test.helpers.FakeConsole
import test.helpers.FakeCreateUserUI
import test.helpers.FakeGetUserUI
import kotlin.test.Test
import kotlin.test.assertTrue

class PlanMateCLITest {
    @Test
    fun `exit on 3`() {
        val console = FakeConsole("3")
        val createUI = FakeCreateUserUI()
        val getUI    = FakeGetUserUI()
        val cli = PlanMateCLI(console, createUI, getUI)
        cli.start()
        assertTrue(console.outputs.any { it.contains("Goodbye!") })
    }

    @Test
    fun `option 1 invokes createUI`() {
        val console = FakeConsole("1","3")
        val createUI = FakeCreateUserUI()
        val getUI    = FakeGetUserUI()
        val cli = PlanMateCLI(console, createUI, getUI)
        cli.start()
        assertTrue(createUI.invokedWith?.type?.name == "Admin")
    }

    @Test
    fun `option 2 invokes getUI`() {
        val console = FakeConsole("2","3")
        val createUI = FakeCreateUserUI()
        val getUI    = FakeGetUserUI()
        val cli = PlanMateCLI(console, createUI, getUI)
        cli.start()
        assertTrue(getUI.invoked)
    }
}
