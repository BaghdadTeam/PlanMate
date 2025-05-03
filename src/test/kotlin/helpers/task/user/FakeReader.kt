package helpers.task.user

import org.baghdad.presentation.input.Reader


class FakeReader(vararg inputs: String?) : Reader {
    private val iterator = inputs.iterator()
    override fun readInput(): String? = if (iterator.hasNext()) iterator.next() else null
}

