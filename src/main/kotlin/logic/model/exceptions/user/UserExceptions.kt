package org.baghdad.logic.model.exceptions.user


class UserNotFoundException(message: String): Exception(message)
class UserAlreadyExistsException(message: String): RuntimeException(message)
class UnauthorizedException(message: String): RuntimeException(message)