package org.baghdad.logic.model.exceptions

sealed class ProjectStatesExceptions : PlanMateException()

class StateNotFoundException(message: String) : ProjectStatesExceptions()
class StateNotAccessedException(message: String) : ProjectStatesExceptions()
class CantAddStateWithNoNameException(message: String) : ProjectStatesExceptions()