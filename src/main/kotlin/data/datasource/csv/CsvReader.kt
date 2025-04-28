package org.baghdad.data.datasource.csv

import java.io.File

class CsvReader(private val file: File) {


    fun readCsv(): List<String> {
        val content = file.readText()
        return recordsSplitter(content).drop(1)
    }


    private fun recordsSplitter(content: String): List<String> {
        val records = mutableListOf<String>()
        var currentRecord = StringBuilder()
        var inQuotes = false

        for (char in content) {
            when {
                char == '"' -> {
                    inQuotes = !inQuotes
                    currentRecord.append(char)
                }

                char == '\n' && !inQuotes -> {
                    records.add(currentRecord.toString())
                    currentRecord = StringBuilder()
                }

                char == '\r' -> {} // Ignore CR
                else -> currentRecord.append(char)
            }
        }
        if (currentRecord.isNotEmpty()) records.add(currentRecord.toString())
        return records
    }
}