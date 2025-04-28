package data.datasource.csv

import com.google.common.truth.Truth.assertThat
import org.baghdad.data.datasource.csv.CsvReader
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class CsvReaderTest {

    private lateinit var tempFile: File

    @BeforeEach
    fun setup() {
        tempFile = File.createTempFile("test", ".csv")
    }

    @AfterEach
    fun cleanup() {
        tempFile.delete()
    }

    @Test
    fun `readCsv skips header and reads simple CSV`() {
        tempFile.writeText("name,age\nJohn,30\nJane,25")
        val reader = CsvReader(tempFile)

        val result = reader.readCsv()

        assertThat(result).containsExactly("John,30", "Jane,25").inOrder()
    }

    @Test
    fun `readCsv handles quoted fields with commas`() {
        tempFile.writeText("name,quote\nJohn,\"hello, world\"\nJane,\"hi, there\"")
        val reader = CsvReader(tempFile)

        val result = reader.readCsv()

        assertThat(result).containsExactly("John,\"hello, world\"", "Jane,\"hi, there\"").inOrder()
    }

    @Test
    fun `readCsv handles quoted fields with newlines`() {
        tempFile.writeText("name,bio\nJohn,\"Hello\nWorld\"\nJane,\"Hi\nThere\"")
        val reader = CsvReader(tempFile)

        val result = reader.readCsv()

        assertThat(result).containsExactly("John,\"Hello\nWorld\"", "Jane,\"Hi\nThere\"").inOrder()
    }

    @Test
    fun `readCsv ignores carriage return characters`() {
        tempFile.writeText("name,age\r\nJohn,30\r\nJane,25\r\n")
        val reader = CsvReader(tempFile)

        val result = reader.readCsv()

        assertThat(result).containsExactly("John,30", "Jane,25").inOrder()
    }

    @Test
    fun `readCsv handles empty lines as records`() {
        tempFile.writeText("name,age\n\nJohn,30\n\n")
        val reader = CsvReader(tempFile)

        val result = reader.readCsv()

        assertThat(result).containsExactly("", "John,30", "").inOrder()
    }

    @Test
    fun `readCsv handles file with only header`() {
        tempFile.writeText("name,age")
        val reader = CsvReader(tempFile)

        val result = reader.readCsv()

        assertThat(result).isEmpty()
    }

    @Test
    fun `readCsv handles file with no content`() {
        tempFile.writeText("")
        val reader = CsvReader(tempFile)

        val result = reader.readCsv()

        assertThat(result).isEmpty()
    }
}
