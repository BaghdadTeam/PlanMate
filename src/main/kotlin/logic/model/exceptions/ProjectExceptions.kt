package org.baghdad.logic.model.exceptions

sealed class ProjectExceptions: PlanMateException()

class ProjectNotFoundException(message: String) : ProjectExceptions()
class EmptyProjectNameException(message: String) : ProjectExceptions()
class AccessDeniedException(message: String) : ProjectExceptions()