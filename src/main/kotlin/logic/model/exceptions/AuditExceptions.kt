package org.baghdad.logic.model.exceptions

sealed class AuditExceptions: PlanMateException()

class NoAuditForTaskException(message: String) : AuditExceptions()
class NoAuditForProjectException(message: String) : AuditExceptions()
class EmptyActionInAuditEntityException(message: String) : AuditExceptions()
class UnSupportedTimeStampFormatException(message: String) : AuditExceptions()