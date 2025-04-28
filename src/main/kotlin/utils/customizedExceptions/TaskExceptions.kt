package org.baghdad.utils.customizedExceptions

class TaskMissingTitleException(message: String) : Exception(message)
class TaskMissingDescriptionException(message: String) : Exception(message)
class TaskMissingStateIdException(message: String) : Exception(message)
class TaskMissingProjectIdException(message: String) : Exception(message)
class TaskMissingCreatorIdException(message: String) : Exception(message)
class TaskNotFoundException(message: String) : Exception(message)
class InvalidTaskIdException(message: String) : IllegalArgumentException(message)
class TaskWithEmptyIDException(message: String) : Exception(message)