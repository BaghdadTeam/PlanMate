package data.datasource.csv

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.datasource.csv.CsvDataSourceImpl
import org.baghdad.logic.model.exceptions.CsvWriteException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class CsvDataSourceImplTest {

    private lateinit var tempFile: File
    private lateinit var parser: CsvMapper<MyData>
    private lateinit var csvDataSource: CsvDataSourceImpl<MyData>

    @BeforeEach
    fun setUp() {
        tempFile = File.createTempFile("test", ".csv")
        parser = mockk(relaxed = true)
        csvDataSource = CsvDataSourceImpl(parser, tempFile)
    }

    @AfterEach
    fun tearDown() {
        tempFile.delete()
    }

    @Test
    fun `test loadAll when csv is empty returns empty list`() {
        tempFile.writeText("") // empty file

        val result = csvDataSource.loadAll()

        assertThat(result).isEmpty()
    }

    @Test
    fun `test loadAll when csv has data returns parsed data`() {
        tempFile.writeText("Name,Age,Gender\nJohn,25,Male")

        val parsedData = MyData("John", 25, "Male")
        every { parser.deserializer("John,25,Male") } returns parsedData

        val result = csvDataSource.loadAll()

        assertThat(result).containsExactly(parsedData)
    }

    @Test
    fun `append writes header when file is completely empty`() {
        val item = MyData("Alice", 30, "Female")

        every { parser.header() } returns "Name,Age,Gender"
        every { parser.serializer(item) } returns "Alice,30,Female"

        csvDataSource.append(item)

        val lines = tempFile.readLines()
        assertThat(lines).containsExactly("Name,Age,Gender", "Alice,30,Female")
    }

    @Test
    fun `append does not write header when file already contains header`() {
        tempFile.writeText("Name,Age,Gender\n")

        val item = MyData("Bob", 40, "Male")
        every { parser.serializer(item) } returns "Bob,40,Male"

        csvDataSource.append(item)

        val lines = tempFile.readLines()
        assertThat(lines).containsExactly("Name,Age,Gender", "Bob,40,Male")
    }

    @Test
    fun `test append successfully appends data`() {
        val item = MyData("John", 25, "Male")

        every { parser.header() } returns "Name,Age,Gender"
        every { parser.serializer(item) } returns "John,25,Male"

        csvDataSource.append(item)

        val lines = tempFile.readLines()
        assertThat(lines).containsExactly("Name,Age,Gender", "John,25,Male")
    }

    @Test
    fun `test append throws CsvWriteException when writer fails`() {
        tempFile.setReadOnly()

        val item = MyData("John", 25, "Male")
        every { parser.serializer(item) } returns "John,25,Male"

        val exception = assertThrows<CsvWriteException> {
            csvDataSource.append(item)
        }

        assertThat(exception.message).startsWith("Error writing to CSV file")

        tempFile.setWritable(true) // cleanup
    }

    @Test
    fun `test saveAll successfully saves all items`() {
        val items = listOf(MyData("John", 25, "Male"), MyData("Jane", 30, "Female"))

        every { parser.header() } returns "Name,Age,Gender"
        every { parser.serializer(any()) } returnsMany listOf("John,25,Male", "Jane,30,Female")

        csvDataSource.update(items)

        val lines = tempFile.readLines()
        assertThat(lines).containsExactly("Name,Age,Gender", "John,25,Male", "Jane,30,Female")
    }

    @Test
    fun `test saveAll throws CsvWriteException when writer fails`() {
        tempFile.setReadOnly()

        val items = listOf(MyData("John", 25, "Male"))
        every { parser.header() } returns "Name,Age,Gender"
        every { parser.serializer(any()) } returns "John,25,Male"

        val exception = assertThrows<CsvWriteException> {
            csvDataSource.update(items)
        }

        assertThat(exception.message).startsWith("Error saving to CSV file")

        tempFile.setWritable(true)
    }
}


// Sample data class for testing purposes
data class MyData(val name: String, val age: Int, val gender: String)
