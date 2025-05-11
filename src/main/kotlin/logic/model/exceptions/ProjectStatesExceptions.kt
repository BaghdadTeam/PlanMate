package org.baghdad.logic.model.exceptions

class StateNotFoundException(message: String) : NoSuchElementException(message)
class NotAccessException(message: String) : Exception(message)
class CantAddStateWithNoNameException(message: String) : Exception(message)