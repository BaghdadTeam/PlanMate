package test.helpers

import org.baghdad.presentation.Console

class FakeConsole(vararg inputs: String) : Console {
    private val inputIter = inputs.iterator()
    val outputs = mutableListOf<String>()

    override fun readLine(prompt: String): String {
        outputs += prompt
        return if (inputIter.hasNext()) inputIter.next() else ""
    }

    override fun writeLine(text: String) {
        outputs += text
    }
}
