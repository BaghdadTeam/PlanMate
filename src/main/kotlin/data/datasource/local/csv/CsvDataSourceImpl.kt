package org.baghdad.data.datasource.local.csv

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.exceptions.CsvWriteException
import java.io.File


class CsvDataSourceImpl<T>(
    private val parser: CsvMapper<T>,
    file: File
) : DataSource<T> {

    private val reader = CsvReader(file)
    private val writer = CsvWriter(file)

    override suspend fun loadAll(): List<T> {

        val lines = reader.readCsv()
        if (lines.size <= 1) return emptyList()

        return lines.drop(1).map(parser::deserializer)
    }

    override suspend fun append(item: T) {
        try {
            val rawLines = reader.readCsv()
            if (rawLines.isEmpty()) {
                writer.appendLine(parser.header())
            }

            writer.appendLine(parser.serializer(item))
        } catch (e: Exception) {
            throw CsvWriteException("Error writing to CSV file: ${e.message}")
        }
    }

    override suspend fun update(item: T) {
        try {
            val allLines = reader.readCsv()
            if (allLines.isEmpty()) throw CsvWriteException("CSV is empty")

            val updatedId = parser.getId(item)
            val updatedLine = parser.serializer(item)

            val updatedLines = allLines.mapIndexed { index, line ->
                if (index == 0) line // keep header
                else {
                    val existingItem = parser.deserializer(line)
                    if (parser.getId(existingItem) == updatedId) updatedLine else line
                }
            }

            writer.updateLines(updatedLines)
        } catch (_: Exception) {
            throw CsvWriteException("Error updating CSV file")
        }
    }

    override suspend fun delete(item: T) {
        try {
            val allLines = reader.readCsv()
            if (allLines.size <= 1) return // only header

            val header = allLines.first()
            val idToDelete = parser.getId(item)

            val filteredLines = allLines.filterIndexed { index, line ->
                if (index == 0) true
                else parser.getId(parser.deserializer(line)) != idToDelete
            }

            writer.updateLines(listOf(header) + filteredLines.drop(1))
        } catch (e: Exception) {
            throw CsvWriteException("Error deleting from CSV file: ${e.message}")
        }
    }
}