package org.baghdad.presentation.helper

import org.example.presentation.Viewer

class ViewerImpl : Viewer {
    override fun log(text: String) {
        println(text)
    }
}