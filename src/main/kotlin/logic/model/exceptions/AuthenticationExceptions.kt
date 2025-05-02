package org.baghdad.logic.model.exceptions

class InvalidPasswordException(message: String) : IllegalArgumentException(message)
class UserCanNotBeFoundException(message: String) : Exception(message)
class SessionEndedException(message: String) : Exception(message)
class InvalidSessionException(message: String) : Exception(message)
class InvalidCredentialsException(message: String) : Exception(message)
class SessionNotFoundException(message: String) : Exception(message)
class LogoutFailedException(message: String) : Exception(message)
class SessionDeletedException(message: String) : Exception(message)
class InvalidTokenException(message: String) : Exception(message)
class SessionNotDeletedException(message: String) : Exception(message)