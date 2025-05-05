package org.baghdad.presentation.app

interface Feature {
    val id: Int
    val name: String
    fun execute()
}