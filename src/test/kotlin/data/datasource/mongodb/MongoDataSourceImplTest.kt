package data.datasource.mongodb

import com.google.common.truth.Truth.assertThat
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoCollection
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.runTest
import org.baghdad.data.datasource.mongodb.MongoDataSourceImpl
import org.baghdad.logic.model.entities.Identifiable
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test

data class FakeIdentifiable(
    override val id: UUID = UUID.randomUUID(),
    val name: String,
) : Identifiable

class MongoDataSourceImplTest {

    private lateinit var fakeCollection: MongoCollection<FakeIdentifiable>
    private lateinit var fakeFindFlow: FindFlow<FakeIdentifiable>
    private lateinit var mongoDataSource: MongoDataSourceImpl<FakeIdentifiable>

    @BeforeEach
    fun setUp() {
        fakeCollection = mockk(relaxed = true)
        fakeFindFlow = mockk(relaxed = true)
        mongoDataSource = MongoDataSourceImpl(fakeCollection)
    }

    @Test
    fun `should return the data from the collection`() = runTest {
        // Given
        val expectedData = listOf(
            FakeIdentifiable(name = "Item 1"),
            FakeIdentifiable(name = "Item 2"),
        )

        // Stub collect() to emit test data
        coEvery { fakeFindFlow.collect(any()) } coAnswers {
            val collector = arg<FlowCollector<FakeIdentifiable>>(0)
            expectedData.forEach { item ->
                collector.emit(item)
            }
        }

        // Stub find() to return the mocked FindFlow
        coEvery { fakeCollection.find() } returns fakeFindFlow

        // When
        val result = mongoDataSource.loadAll()

        // Then
        assertThat(result).isEqualTo(expectedData)
    }

    @Test
    fun `update calls replaceOne with correct filter and item`() = runTest {
        // Given
        val item = FakeIdentifiable(name = "Test Item")
        val expectedFilter = Filters.eq("_id", item.id)

        // Mock replaceOne with all 4 parameters
        coEvery {
            fakeCollection.replaceOne(
                filter = eq(expectedFilter),
                replacement = eq(item),
                options = any(),
            )
        } returns mockk()

        // When
        mongoDataSource.update(item)

        // Then
        coVerify {
            fakeCollection.replaceOne(
                eq(expectedFilter),
                eq(item),
                any(),
            )
        }
    }

    @Test
    fun `append calls insertOne with item`() = runTest {
        // Given
        val item = FakeIdentifiable(name = "Test Item")

        // Mock insertOne with relaxed argument matching
        coEvery {
            fakeCollection.insertOne(eq(item), any(),)
        } returns mockk()

        // When
        mongoDataSource.append(item)

        // Then
        coVerify {
            fakeCollection.insertOne(eq(item), any(),)
        }
    }

    @Test
    fun `delete calls findOneAndDelete with correct filter`() = runTest {
        // Given
        val item = FakeIdentifiable(name = "Test Item")
        val expectedFilter = Filters.eq("_id", item.id)

        // Mock findOneAndDelete with relaxed argument matching
        coEvery {
            fakeCollection.findOneAndDelete(eq(expectedFilter), any(),)
        } returns mockk()

        // When
        mongoDataSource.delete(item)

        // Then
        coVerify {
            fakeCollection.findOneAndDelete(eq(expectedFilter), any(), )
        }
    }


}
