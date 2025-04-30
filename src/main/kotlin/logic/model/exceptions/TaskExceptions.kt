package org.baghdad.logic.model.exceptions

class TasksNotFoundException(message: String) : Exception(message)
class InvalidUUIDException(message: String) : IllegalArgumentException(message)
class TaskWithMissingTitleException(message: String) : Exception(message)
class TaskWithMissingDescriptionException(message: String) : Exception(message)
class TaskWithMissingStateIDException(message: String) : Exception(message)
class TaskWithMissingProjectIDException(message: String) : Exception(message)
class TaskWithMissingCreatorIDException(message: String) : Exception(message)