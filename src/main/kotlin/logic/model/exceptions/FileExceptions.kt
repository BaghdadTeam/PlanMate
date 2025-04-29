package org.baghdad.logic.model.exceptions

class CsvFileExceptions(message: String) : IllegalArgumentException(message)
class EmptyHeaderException(message: String) : IllegalArgumentException(message)
class CsvReadException(message: String) : RuntimeException(message)
class CsvWriteException(message: String) : RuntimeException(message)