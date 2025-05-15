package org.baghdad.logic.model.exceptions

sealed class UserExceptions : PlanMateException()

class UserNotFoundException(message: String) : UserExceptions()
class UserAlreadyExistsException(message: String) : UserExceptions()
class InvalidUsernameException(message: String) : UserExceptions()
class InvalidNameException(message: String) : UserExceptions()