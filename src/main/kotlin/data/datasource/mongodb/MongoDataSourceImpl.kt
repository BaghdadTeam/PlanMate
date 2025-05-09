package org.baghdad.data.datasource.mongodb

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.toList
import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.Identifiable


class MongoDataSourceImpl<T : Identifiable>(
    private val collectionName: MongoCollection<T>,
) : DataSource<T> {
    override suspend fun loadAll(): List<T> {
        return collectionName.find().toList()
    }

    override suspend fun update(item: T) {
        val filter = Filters.eq("_id", item.id)
        collectionName.replaceOne(filter, item)
    }

    override suspend fun append(item: T) {
        collectionName.insertOne(item)
    }

    override suspend fun delete(item: T) {
        val filter = Filters.eq("_id", item.id)
        collectionName.findOneAndDelete(filter)
    }
}