package org.baghdad.logic.model.exceptions

class InvalidPasswordException(message: String) : IllegalArgumentException(message)
class UserCanNotBeFoundException(message: String) : Exception(message)
class SessionEndedException(message: String) : Exception(message)
class InvalidCredentialsException(message: String) : Exception(message)
class SessionNotFoundException(message: String) : Exception(message)
class LogoutFailedException(message: String) : Exception(message)
class UnauthorizedException(): Exception("User is not authorized to perform this action")