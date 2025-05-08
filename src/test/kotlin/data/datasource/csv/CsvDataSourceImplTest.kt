package data.datasource.csv

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.datasource.csv.CsvDataSourceImpl
import org.baghdad.logic.model.entities.Identifiable
import org.baghdad.logic.model.exceptions.CsvWriteException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.util.*

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
    fun `test loadAll when csv is empty returns empty list`() = runTest {
        tempFile.writeText("") // empty file

        val result = csvDataSource.loadAll()

        assertThat(result).isEmpty()
    }

    @Test
    fun `test loadAll when csv has data returns parsed data`() = runTest {
        val uuid = UUID.randomUUID()
        val line = "$uuid,John,25,Male"
        tempFile.writeText("id,Name,Age,Gender\n$line")

        val parsedData = MyData(uuid, "John", 25, "Male")
        every { parser.deserializer(line) } returns parsedData

        val result = csvDataSource.loadAll()

        assertThat(result).containsExactly(parsedData)
    }

    @Test
    fun `append writes header when file is completely empty`() = runTest {
        val item = MyData(UUID.randomUUID(), "Alice", 30, "Female")

        every { parser.header() } returns "id,Name,Age,Gender"
        every { parser.serializer(item) } returns "${item.id},Alice,30,Female"

        csvDataSource.append(item)

        val lines = tempFile.readLines()
        assertThat(lines).containsExactly("id,Name,Age,Gender", "${item.id},Alice,30,Female")
    }

    @Test
    fun `append does not write header when file already contains header`() = runTest {
        tempFile.writeText("id,Name,Age,Gender\n")

        val item = MyData(UUID.randomUUID(), "Bob", 40, "Male")
        every { parser.serializer(item) } returns "${item.id},Bob,40,Male"

        csvDataSource.append(item)

        val lines = tempFile.readLines()
        assertThat(lines).containsExactly("id,Name,Age,Gender", "${item.id},Bob,40,Male")
    }

    @Test
    fun `test append successfully appends data`() = runTest {
        val item = MyData(UUID.randomUUID(), "John", 25, "Male")

        every { parser.header() } returns "id,Name,Age,Gender"
        every { parser.serializer(item) } returns "${item.id},John,25,Male"

        csvDataSource.append(item)

        val lines = tempFile.readLines()
        assertThat(lines).containsExactly("id,Name,Age,Gender", "${item.id},John,25,Male")
    }

    @Test
    fun `test append throws CsvWriteException when writer fails`() = runTest {
        tempFile.setReadOnly()

        val item = MyData(UUID.randomUUID(), "John", 25, "Male")
        every { parser.serializer(item) } returns "${item.id},John,25,Male"

        val exception = assertThrows<CsvWriteException> {
            csvDataSource.append(item)
        }

        assertThat(exception.message).startsWith("Error writing to CSV file")

        tempFile.setWritable(true) // cleanup
    }

    @Test
    fun `test update successfully updates item`() = runTest {
        val id = UUID.randomUUID()
        val original = MyData(id, "John", 25, "Male")
        val updated = MyData(id, "John", 26, "Male")

        val lines = listOf("id,Name,Age,Gender", "${original.id},John,25,Male")
        tempFile.writeText(lines.joinToString("\n"))

        every { parser.deserializer("${original.id},John,25,Male") } returns original
        every { parser.getId(original) } returns original.id.toString()
        every { parser.getId(updated) } returns updated.id.toString()
        every { parser.serializer(updated) } returns "${updated.id},John,26,Male"

        csvDataSource.update(updated)

        val result = tempFile.readLines()
        assertThat(result).containsExactly("id,Name,Age,Gender", "${updated.id},John,26,Male")
    }

    @Test
    fun `test delete successfully deletes item`() = runTest {
        val idToDelete = UUID.randomUUID()
        val otherId = UUID.randomUUID()
        val itemToDelete = MyData(idToDelete, "John", 25, "Male")
        val otherItem = MyData(otherId, "Jane", 30, "Female")

        tempFile.writeText("id,Name,Age,Gender\n${idToDelete},John,25,Male\n${otherId},Jane,30,Female")

        every { parser.deserializer("${idToDelete},John,25,Male") } returns itemToDelete
        every { parser.deserializer("${otherId},Jane,30,Female") } returns otherItem
        every { parser.getId(itemToDelete) } returns idToDelete.toString()
        every { parser.getId(otherItem) } returns otherId.toString()

        csvDataSource.delete(itemToDelete)

        val result = tempFile.readLines()
        assertThat(result).containsExactly("id,Name,Age,Gender", "${otherId},Jane,30,Female")
    }


    @Test
    fun `test saveAll throws CsvWriteException when writer fails`() = runTest {
        tempFile.setReadOnly()

        val item = MyData(UUID.randomUUID(), "John", 25, "Male")
        every { parser.header() } returns "id,Name,Age,Gender"
        every { parser.serializer(item) } returns "${item.id},John,25,Male"

        val exception = assertThrows<CsvWriteException> {
            csvDataSource.update(item)
        }

        assertThat(exception.message).startsWith("Error updating CSV file")

        tempFile.setWritable(true)
    }
}

// Updated data class with Identifiable
data class MyData(
    override val id: UUID = UUID.randomUUID(),
    val name: String,
    val age: Int,
    val gender: String
) : Identifiable
