package org.baghdad.logic.model.exceptions

sealed class ProjectExceptions: PlanMateException()

class ProjectNotFoundException() : ProjectExceptions()
class EmptyProjectNameException() : ProjectExceptions()
class AccessDeniedException() : ProjectExceptions()