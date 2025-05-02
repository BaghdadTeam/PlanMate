package org.baghdad.logic.model.exceptions

class TasksNotFoundException(message: String) : Exception(message)
class TaskWithMissingTitleException(message: String) : Exception(message)
class TaskWithMissingDescriptionException(message: String) : Exception(message)
class TaskWithMissingProjectIdException(message: String) : Exception(message)
class TaskWithMissingStateIdException(message: String) : Exception(message)
