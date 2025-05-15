package org.baghdad.logic.model.exceptions

sealed class TaskExceptions: PlanMateException()

class TasksNotFoundException() : TaskExceptions()
class TaskWithMissingTitleException() : TaskExceptions()
class TaskWithMissingDescriptionException() : TaskExceptions()