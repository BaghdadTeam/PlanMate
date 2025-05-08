package org.baghdad.presentation.output

interface Viewer {
    fun logMessage(msg: String)
    fun logError(msg: String)
    fun logAuth(msg: String)

}