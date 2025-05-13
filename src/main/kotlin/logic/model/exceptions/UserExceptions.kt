package org.baghdad.logic.model.exceptions


class UserNotFoundException(message: String): Exception(message)
class UserAlreadyExistsException(message: String): RuntimeException(message)
class UnauthorizedException(): Exception("User is not authorized to perform this action")
class InvalidUsernameException(message: String): Exception(message)
class InvalidNameException(message: String): Exception(message)