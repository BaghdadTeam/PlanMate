package org.baghdad.data.datasource.mongodb

import com.mongodb.MongoClientSettings
import com.mongodb.MongoException
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.bson.BsonInt64
import org.bson.Document
import org.bson.UuidRepresentation

object MongoSetup {

    suspend fun setupConnection(
        databaseName: String = "PlanMate",
        connectString: String = "mongodb+srv://youssefmusaber:Pixelise2001@planmate-cluster.z97gz0e.mongodb.net/?retryWrites=true&w=majority&appName=PlanMate-Cluster"
    ): MongoDatabase {

        val settings = MongoClientSettings.builder()
            .applyConnectionString(com.mongodb.ConnectionString(connectString))
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .build()

        val client = MongoClient.create(settings)
        val database = client.getDatabase(databaseName = databaseName)
        return try {
            // Send a ping to confirm a successful connection
            val command = Document("ping", BsonInt64(1))
            database.runCommand(command)
            println("Pinged your deployment. You successfully connected to MongoDB!")
            database
        } catch (me: MongoException) {
            System.err.println(me)
            throw Exception("Failed to connect to MongoDB: ${me.message}")
        }
    }
}