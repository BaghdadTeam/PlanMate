package org.baghdad.logic.model.exceptions.user


class UserNotFoundException(message: String): Exception(message)
class UserAlreadyExistsException(message: String): RuntimeException(message)
class UnauthorizedException(message: String): RuntimeException(message)
class InvalidPasswordException(message: String): Exception(message)
class InvalidUsernameException(message: String): Exception(message)
class InvalidNameException(message: String): Exception(message)