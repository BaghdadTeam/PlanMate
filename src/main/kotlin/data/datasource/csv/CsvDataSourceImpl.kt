package org.baghdad.data.datasource.csv

import org.baghdad.data.datasource.CsvParser
import org.baghdad.data.storage.DataSource
import org.baghdad.utils.customizedExceptions.CsvReadException
import org.baghdad.utils.customizedExceptions.CsvWriteException

class CsvDataSourceImpl<T>(
    private val reader: CsvReader,
    private val writer: CsvWriter,
    private val parser: CsvParser<T>
) : DataSource<T> {

    override fun loadAll(): List<T> {
        return try {
            val lines = reader.readCsv()
            if (lines.isEmpty()) return emptyList()

            lines.drop(1).map(parser::deserializer)
        } catch (e: Exception) {
            throw CsvReadException("Error reading CSV file: ${e.message}")
        }
    }

    override fun append(item: T) {
        try {
            writer.appendLine(parser.serializer(item))
        } catch (e: Exception) {
            throw CsvWriteException("Error writing to CSV file: ${e.message}")
        }
    }

    override fun saveAll(items: List<T>) {
        try {
            val serializedLines = items.map(parser::serializer)
            writer.overwriteLines(listOf(parser.header()) + serializedLines)
        } catch (e: Exception) {
            throw CsvWriteException("Error saving to CSV file: ${e.message}")
        }
    }
}
