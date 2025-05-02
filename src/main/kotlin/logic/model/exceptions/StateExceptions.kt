package org.baghdad.logic.model.exceptions

class StateExceptions {
    class NotFoundException(message: String = "") : NoSuchElementException(message)
}