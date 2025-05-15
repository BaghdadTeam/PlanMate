package org.baghdad.logic.model.exceptions

sealed class TaskExceptions: PlanMateException()

class TasksNotFoundException(message: String) : TaskExceptions()
class TaskWithMissingTitleException(message: String) : TaskExceptions()
class TaskWithMissingDescriptionException(message: String) : TaskExceptions()