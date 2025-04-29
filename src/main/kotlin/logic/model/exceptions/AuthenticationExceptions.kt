package org.baghdad.logic.model.exceptions

class InvalidPasswordException(message: String) : IllegalArgumentException(message)
class UserCanNotBeFoundException(message: String) : Exception(message)
class SessionEndedException(message: String) : Exception(message)
class InvalidSessionException(message: String) : Exception(message)
class InvalidCredentialsException(message: String) : Exception(message)