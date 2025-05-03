package org.baghdad.logic.repositories

interface TokenProvider {
    fun generateToken(): String
}