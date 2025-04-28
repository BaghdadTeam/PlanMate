package data.datasource.csv

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.baghdad.data.datasource.CsvParser
import org.baghdad.data.datasource.csv.CsvDataSourceImpl
import org.baghdad.data.datasource.csv.CsvReader
import org.baghdad.data.datasource.csv.CsvWriter
import org.baghdad.utils.customizedExceptions.CsvReadException
import org.baghdad.utils.customizedExceptions.CsvWriteException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CsvDataSourceImplTest {

    private lateinit var reader: CsvReader
    private lateinit var writer: CsvWriter
    private lateinit var parser: CsvParser<MyData>
    private lateinit var csvDataSource: CsvDataSourceImpl<MyData>

    @BeforeEach
    fun setUp() {
        reader = mockk(relaxed = true)
        writer = mockk(relaxed = true)
        parser = mockk(relaxed = true)

        csvDataSource = CsvDataSourceImpl(reader, writer, parser)
    }

    @Test
    fun `test loadAll when csv is empty returns empty list`() {
        // Given
        every { reader.readCsv() } returns emptyList()

        // When
        val result = csvDataSource.loadAll()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `test loadAll when csv has data returns parsed data`() {
        // Given
        val lines = listOf("Header", "John, 25, Male")
        every { reader.readCsv() } returns lines

        val parsedData = MyData("John", 25, "Male")
        every { parser.deserializer("John, 25, Male") } returns parsedData

        // When
        val result = csvDataSource.loadAll()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("John")
    }

    @Test
    fun `test loadAll throws CsvReadException when reader fails`() {
        // Given
        every { reader.readCsv() } throws Exception("File read error")

        // When & Then
        val exception = assertThrows<CsvReadException> {
            csvDataSource.loadAll()
        }

        assertThat(exception.message).isEqualTo("Error reading CSV file: File read error")
    }

    @Test
    fun `test append successfully appends data`() {
        val item = MyData("John", 25, "Male")

        // Given
        every { parser.serializer(item) } returns "John, 25, Male"

        // When
        csvDataSource.append(item)

        // Then
        verify { writer.appendLine("John, 25, Male") }
    }

    @Test
    fun `test append throws CsvWriteException when writer fails`() {
        val item = MyData("John", 25, "Male")

        // Given
        every { parser.serializer(item) } returns "John, 25, Male"

        every { writer.appendLine(any()) } throws Exception("Write error")

        // When & Then
        val exception = assertThrows<CsvWriteException> {
            csvDataSource.append(item)
        }

        assertThat(exception.message).isEqualTo("Error writing to CSV file: Write error")
    }

    @Test
    fun `test saveAll successfully saves all items`() {
        val items = listOf(MyData("John", 25, "Male"), MyData("Jane", 30, "Female"))

        // Given
        every { parser.serializer(any()) } returns "Serialized Data"
        every { parser.header() } returns "Name, Age, Gender"

        // When
        csvDataSource.saveAll(items)

        // Then
        verify { writer.overwriteLines(listOf("Name, Age, Gender", "Serialized Data", "Serialized Data")) }
    }

    @Test
    fun `test saveAll throws CsvWriteException when writer fails`() {
        val items = listOf(MyData("John", 25, "Male"))
        // Given
        every { parser.serializer(any()) } returns "Serialized Data"
        every { parser.header() } returns "Name, Age, Gender"

        every { writer.overwriteLines(any()) } throws Exception("Save error")

        // When & Then
        val exception = assertThrows<CsvWriteException> {
            csvDataSource.saveAll(items)
        }

        assertThat(exception.message).isEqualTo("Error saving to CSV file: Save error")
    }
}

// Sample data class for testing purposes
data class MyData(val name: String, val age: Int, val gender: String)
