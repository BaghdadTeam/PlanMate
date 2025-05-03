package org.baghdad.presentation.helper

class ReaderImpl: Reader {
    override fun readInput(): String? {
        return readlnOrNull()
    }
}