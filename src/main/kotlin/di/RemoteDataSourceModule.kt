package org.baghdad.di

import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.github.cdimascio.dotenv.dotenv
import org.baghdad.data.datasource.remote.mongodb.MongoDBNames
import org.baghdad.data.datasource.remote.mongodb.collection.ProjectCollection
import org.baghdad.data.dto.AuditLogDto
import org.baghdad.data.dto.ProjectDto
import org.baghdad.data.dto.TaskStateDto
import org.baghdad.data.dto.UserDto
import org.baghdad.logic.model.entities.Identifiable
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.enums.Entities
import org.bson.UuidRepresentation
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module


inline fun <reified T : Identifiable> Module.registerMongoDataSource(
    name: Entities,
    collectionName: String
) {
    single<MongoCollection<T>>(named(name)) {
        get<MongoDatabase>().getCollection<T>(collectionName)
    }
}

val remoteDataSourceModule = module {

    single {
        val databaseName = MongoDBNames.DATABASE_NAME
        val dotenv = dotenv() // Loads from .env by default
        val connectionString = dotenv["MONGO_CONNECTION_STRING"]
            ?: throw IllegalStateException("MONGO_CONNECTION_STRING not found in .env")

        val settings = MongoClientSettings.builder()
            .applyConnectionString(com.mongodb.ConnectionString(connectionString))
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .build()

        val client = MongoClient.create(settings)
        val database = client.getDatabase(databaseName = databaseName)
        database
    }

    registerMongoDataSource<UserDto>(Entities.User, MongoDBNames.USERS_COLLECTION)
    registerMongoDataSource<ProjectDto>(Entities.Project, MongoDBNames.PROJECTS_COLLECTION)
    registerMongoDataSource<TaskStateDto>(Entities.State, MongoDBNames.PROJECT_STATES_COLLECTION)
    registerMongoDataSource<TaskEntity>(Entities.Task, MongoDBNames.TASKS_COLLECTION)
    registerMongoDataSource<AuditLogDto>(Entities.Audit, MongoDBNames.AUDIT_COLLECTION)

    single {
        get<MongoCollection<UserDto>>(named(Entities.User))
    }

    single(named(Entities.Project)) {
        ProjectCollection(get(named(Entities.State)), get(named(Entities.Task)), get<MongoCollection<ProjectDto>>())
    }
}