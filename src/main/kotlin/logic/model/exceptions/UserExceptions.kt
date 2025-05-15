package org.baghdad.logic.model.exceptions

sealed class UserExceptions : PlanMateException()

class UserNotFoundException() : UserExceptions()
class UserAlreadyExistsException() : UserExceptions()
class InvalidUsernameException() : UserExceptions()
class InvalidNameException() : UserExceptions()