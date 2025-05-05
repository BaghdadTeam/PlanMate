package org.baghdad.data.datasource.csv

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.exceptions.CsvReadException
import org.baghdad.logic.model.exceptions.CsvWriteException
import java.io.File


class CsvDataSourceImpl<T>(
    private val parser: CsvMapper<T>,
    file: File
) : DataSource<T> {

    private val reader = CsvReader(file)
    private val writer = CsvWriter(file)

    override fun loadAll(): List<T> {
        return try {
            val lines = reader.readCsv()
            if (lines.size <= 1) return emptyList()

            lines.drop(1).map(parser::deserializer)
        } catch (e: Exception) {
            throw CsvReadException("Error reading CSV file: ${e.message}")
        }
    }

    override fun append(item: T) {
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

    override fun update(items: List<T>) {
        try {
            val serializedLines = items.map(parser::serializer)
            writer.updateLines(listOf(parser.header()) + serializedLines)
        } catch (e: Exception) {
            throw CsvWriteException("Error saving to CSV file: ${e.message}")
        }
    }
}