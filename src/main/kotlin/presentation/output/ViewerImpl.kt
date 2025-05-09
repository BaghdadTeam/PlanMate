package org.baghdad.presentation.output

class ViewerImpl : Viewer {
    override fun logMessage(msg: String) {
        println(msg)
    }

    override fun logError(msg: String) {
        System.err.println("ERROR: $msg")
    }

    override fun logAuth(msg: String) {
        print(msg)
    }
}