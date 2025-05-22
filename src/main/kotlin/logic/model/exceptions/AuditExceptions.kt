package org.baghdad.logic.model.exceptions

sealed class AuditExceptions: PlanMateException()

class NoAuditForTaskException() : AuditExceptions()
class NoAuditForProjectException() : AuditExceptions()
class EmptyActionInAuditEntityException() : AuditExceptions()
class UnSupportedTimeStampFormatException() : AuditExceptions()