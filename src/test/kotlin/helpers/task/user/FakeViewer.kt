package helpers.task.user


import org.baghdad.presentation.output.Viewer

class FakeViewer : Viewer {
    val messages = mutableListOf<String>()
    val errors = mutableListOf<String>()

    override fun logMessage(msg: String) {
        messages.add(msg)
    }

    override fun logError(msg: String) {
        errors.add(msg)
    }
}
