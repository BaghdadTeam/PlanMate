package org.baghdad.logic.model.exceptions

sealed class ProjectStatesExceptions : PlanMateException()

class StateNotFoundException() : ProjectStatesExceptions()
class StateNotAccessedException() : ProjectStatesExceptions()
class CantAddStateWithNoNameException() : ProjectStatesExceptions()