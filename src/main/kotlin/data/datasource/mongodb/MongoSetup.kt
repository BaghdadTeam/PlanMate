package org.baghdad.data.datasource.mongodb

import com.mongodb.MongoClientSettings
import com.mongodb.MongoException
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.github.cdimascio.dotenv.dotenv
import org.bson.BsonInt64
import org.bson.Document
import org.bson.UuidRepresentation

object MongoSetup {

    suspend fun setupConnection(
        databaseName: String = "PlanMate",
    ): MongoDatabase {
        val dotenv = dotenv() // Loads from .env by default
        val connectionString = dotenv["MONGO_CONNECTION_STRING"]
            ?: throw IllegalStateException("MONGO_CONNECTION_STRING not found in .env")

        val settings = MongoClientSettings.builder()
            .applyConnectionString(com.mongodb.ConnectionString(connectionString))
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .build()

        val client = MongoClient.create(settings)
        val database = client.getDatabase(databaseName = databaseName)
        return try {
            val command = Document("ping", BsonInt64(1))
            database.runCommand(command)
            database
        } catch (me: MongoException) {
            System.err.println(me)
            throw Exception("Failed to connect to MongoDB: ${me.message}")
        }
    }
}