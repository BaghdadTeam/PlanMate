package org.baghdad.presentation

interface Feature {
    val id: Int
    val name: String
    fun execute()
}