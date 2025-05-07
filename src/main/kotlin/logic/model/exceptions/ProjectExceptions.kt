package org.baghdad.logic.model.exceptions

class ProjectNotFoundException(message: String) : Exception(message)
class EmptyProjectNameException(message: String) : IllegalArgumentException(message)
class AccessDeniedException(message: String) : Exception(message)