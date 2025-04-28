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
            CsvWriter(nonCsvFile) // This should throw the exception on initialization
        }

        assertThat(exception.message).isEqualTo("The file must have a .csv extension")
    }

    @Test
    fun `test appendLine throws CsvFileExceptions if file is not CSV`() {
        val nonCsvFile = File("test.txt")

        val exception = assertThrows<CsvFileExceptions> {
            CsvWriter(nonCsvFile) // CsvWriter initialization throws exception for non-CSV file
            CsvWriter(nonCsvFile).appendLine("Some record") // This line won't be executed
        }

        assertThat(exception.message).isEqualTo("The file must have a .csv extension")
    }

    @Test
    fun `test overwriteLines throws CsvFileExceptions if file is not CSV`() {
        val nonCsvFile = File("test.txt")

        val exception = assertThrows<CsvFileExceptions> {
            CsvWriter(nonCsvFile) // CsvWriter initialization throws exception for non-CSV file
            CsvWriter(nonCsvFile).overwriteLines(listOf("Header", "Row 1", "Row 2")) // This line won't be executed
        }

        assertThat(exception.message).isEqualTo("The file must have a .csv extension")
    }

    @Test
    fun `test WriteHeader when file does not exist creates file with header`() {
        mockFile.delete() // Delete the file to simulate it doesn't exist

        csvWriter.writeHeader("Name, Age, Gender") // Write header

        assertThat(mockFile.exists()).isTrue() // Ensure the file is created
        assertThat(mockFile.readText()).isEqualTo("Name, Age, Gender\n") // Check that the header is written
    }

    @Test
    fun `test write header when file exists does not overwrite`() {
        val existingHeader = "Name, Age, Gender\n"
        mockFile.writeText(existingHeader) // Write existing header to the file

        csvWriter.writeHeader("Name, Age, Gender") // Try writing again (shouldn't overwrite)

        assertThat(mockFile.exists()).isTrue() // File should still exist
        assertThat(mockFile.readText()).isEqualTo(existingHeader) // Header should be unchanged
    }

    @Test
    fun `test append line when file exists appends line`() {
        csvWriter.appendLine("John, 30, Male") // Append line

        assertThat(mockFile.readText()).isEqualTo("John, 30, Male\n") // Check the file content
    }

    @Test
    fun `test overwrite lines overwrites content`() {
        csvWriter.overwriteLines(listOf("Name, Age, Gender", "John, 30, Male")) // Overwrite lines

        assertThat(mockFile.readText()).isEqualTo("Name, Age, Gender\nJohn, 30, Male\n") // Verify new content
    }

    @Test
    fun `test overwrite lines when list is empty creates empty file`() {
        csvWriter.overwriteLines(emptyList()) // Overwrite with empty list (file should be empty)

        assertThat(mockFile.readText()).isEqualTo("\n") // Verify empty file
    }


    @Test
    fun `test writeHeader throws EmptyHeaderException if header is empty`() {
        val exception = assertThrows<EmptyHeaderException> {
            csvWriter.writeHeader("") // Try writing empty header, should throw exception
        }

        assertThat(exception.message).isEqualTo("Header cannot be empty")
    }

}