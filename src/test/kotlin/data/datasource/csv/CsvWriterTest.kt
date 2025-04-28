package data.datasource.csv

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.baghdad.data.datasource.csv.CsvWriter
import org.baghdad.utils.customizedExceptions.CsvFileExceptions
import org.baghdad.utils.customizedExceptions.EmptyHeaderException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class CsvWriterTest {

    private lateinit var mockFile: File
    private lateinit var csvWriter: CsvWriter

    @BeforeEach
    fun setUp() {

        mockFile = File.createTempFile("test", ".csv")
        csvWriter = CsvWriter(mockFile)
    }

    @AfterEach
    fun tearDown() {

        mockFile.delete()
    }

    @Test
    fun `test CsvWriter throws CsvFileExceptions if file is not CSV on initialization`() {
        val nonCsvFile = File("test.txt")

        val exception = assertThrows<CsvFileExceptions> {
            CsvWriter(nonCsvFile)
        }

        assertThat(exception.message).isEqualTo("The file must have a .csv extension")
    }

    @Test
    fun `test appendLine throws CsvFileExceptions if file is not CSV`() {
        val nonCsvFile = File("test.txt")

        val exception = assertThrows<CsvFileExceptions> {
            CsvWriter(nonCsvFile)
            CsvWriter(nonCsvFile).appendLine("Some record")
        }

        assertThat(exception.message).isEqualTo("The file must have a .csv extension")
    }

    @Test
    fun `test overwriteLines throws CsvFileExceptions if file is not CSV`() {
        val nonCsvFile = File("test.txt")

        val exception = assertThrows<CsvFileExceptions> {
            CsvWriter(nonCsvFile)
            CsvWriter(nonCsvFile).overwriteLines(listOf("Header", "Row 1", "Row 2"))
        }

        assertThat(exception.message).isEqualTo("The file must have a .csv extension")
    }

    @Test
    fun `test WriteHeader when file does not exist creates file with header`() {
        mockFile.delete()

        csvWriter.writeHeader("Name, Age, Gender")

        assertThat(mockFile.exists()).isTrue()
        assertThat(mockFile.readText()).isEqualTo("Name, Age, Gender\n")
    }

    @Test
    fun `test write header when file exists does not overwrite`() {
        val existingHeader = "Name, Age, Gender\n"
        mockFile.writeText(existingHeader)

        csvWriter.writeHeader("Name, Age, Gender")

        assertThat(mockFile.exists()).isTrue()
        assertThat(mockFile.readText()).isEqualTo(existingHeader)
    }

    @Test
    fun `test append line when file exists appends line`() {
        csvWriter.appendLine("John, 30, Male")

        assertThat(mockFile.readText()).isEqualTo("John, 30, Male\n")
    }

    @Test
    fun `test overwrite lines overwrites content`() {
        csvWriter.overwriteLines(listOf("Name, Age, Gender", "John, 30, Male"))

        assertThat(mockFile.readText()).isEqualTo("Name, Age, Gender\nJohn, 30, Male\n")
    }

    @Test
    fun `test overwrite lines when list is empty creates empty file`() {
        csvWriter.overwriteLines(emptyList())

        assertThat(mockFile.readText()).isEqualTo("\n")
    }


    @Test
    fun `test writeHeader throws EmptyHeaderException if header is empty`() {
        val exception = assertThrows<EmptyHeaderException> {
            csvWriter.writeHeader("")
        }

        assertThat(exception.message).isEqualTo("Header cannot be empty")
    }

}