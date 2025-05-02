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
    fun `exits on choice 3`() {
        // تحضير فاكَّكونسول يعيد "3"
        val console = FakeConsole("3")
        val createUI = FakeCreateUserUI()
        val getUI = FakeGetUserUI()

        val cli = PlanMateCLI(console, createUI, getUI)
        cli.start()

        // بعد run، يجب أن نرى "Goodbye!"
        assertTrue(console.outputs.any { it.contains("Goodbye!") })
    }

    @Test
    fun `delegates to createUserUI on choice 1`() {
        val console = FakeConsole("1","3")  // أولاً اختيار 1، ثم خروج
        val createUI = FakeCreateUserUI()
        val getUI = FakeGetUserUI()

        val cli = PlanMateCLI(console, createUI, getUI)
        cli.start()

        // يجب أن يتم استدعاء createUI مع Admin
        assertTrue(createUI.invokedWith?.type?.name == "Admin")
    }

    @Test
    fun `delegates to getUserUI on choice 2`() {
        val console = FakeConsole("2","3")
        val createUI = FakeCreateUserUI()
        val getUI = FakeGetUserUI()

        val cli = PlanMateCLI(console, createUI, getUI)
        cli.start()

        assertTrue(getUI.invoked)
    }
}