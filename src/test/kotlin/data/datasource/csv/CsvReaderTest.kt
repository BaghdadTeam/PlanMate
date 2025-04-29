package data.datasource.csv

import com.google.common.truth.Truth.assertThat
import org.baghdad.data.datasource.csv.CsvReader
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.IOException


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
    fun `readCsv returns raw lines including header and data`() {
        tempFile.writeText("name,age\nJohn,30\nJane,25")
        val reader = CsvReader(tempFile)

        val result = reader.readCsv()

        assertThat(result).containsExactly("name,age", "John,30", "Jane,25")
    }

    @Test
    fun `readCsv returns single line when only header present`() {
        tempFile.writeText("name,age")
        val reader = CsvReader(tempFile)

        val result = reader.readCsv()

        assertThat(result).containsExactly("name,age")
    }

    @Test
    fun `readCsv returns empty list when file is empty`() {
        tempFile.writeText("")
        val reader = CsvReader(tempFile)

        val result = reader.readCsv()

        assertThat(result).isEmpty()
    }

    @Test
    fun `readCsv creates new file if it does not exist`() {
        // delete the temp file to simulate "missing" file
        tempFile.delete()
        val reader = CsvReader(tempFile)

        val result = reader.readCsv()

        // file should be created and empty
        assertThat(tempFile.exists()).isTrue()
        assertThat(result).isEmpty()
    }

    @Test
    fun `readCsv throws IOException when path is a directory`() {
        // make the path a directory
        tempFile.delete()
        tempFile.mkdir()
        val reader = CsvReader(tempFile)

        assertThrows<IOException> { reader.readCsv() }
    }
}
