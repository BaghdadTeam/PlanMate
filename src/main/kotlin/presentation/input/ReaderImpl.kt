package org.baghdad.presentation.input

class ReaderImpl: Reader {
    override fun readInput(): String? {
       return readlnOrNull()
    }
}