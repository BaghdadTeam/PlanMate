package org.baghdad.data.datasource.csv

import org.baghdad.logic.model.exceptions.FileExceptions
import org.baghdad.logic.model.exceptions.EmptyHeaderException
import java.io.File


class CsvWriter(private val file: File) {

    // Validate the file extension and throw an exception if it is not a CSV file
    init {
        if (!file.name.endsWith(".csv", ignoreCase = true)) {
            throw FileExceptions("The file must have a .csv extension")
        }
    }

    // Write the header to the file if it's not blank and the file doesn't exist
    fun writeHeader(header: String) {
        if (header.isBlank()) {
            throw EmptyHeaderException("Header cannot be empty")
        }

        if (!file.exists()) {
            file.writeText(header + "\n")
        }
    }

    // Append a line to the file
    fun appendLine(record: String) {
        file.appendText(record + "\n")
    }

    // Overwrite the lines in the file
    fun updateLines(lines: List<String>) {

        file.writeText(lines.joinToString("\n") + "\n")
    }
}
