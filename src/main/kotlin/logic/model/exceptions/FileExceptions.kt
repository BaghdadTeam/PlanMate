package org.baghdad.logic.model.exceptions

class FileExceptions(message: String) : IllegalArgumentException()
class EmptyHeaderException(message: String) : IllegalArgumentException()
class WritingFileException(message: String) : RuntimeException()