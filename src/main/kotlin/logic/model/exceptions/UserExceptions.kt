package org.baghdad.logic.model.exceptions


class UserNotFoundException(message: String): Exception(message)
class UserAlreadyExistsException(message: String): RuntimeException(message)
class InvalidUsernameException(message: String): Exception(message)
class InvalidNameException(message: String): Exception(message)