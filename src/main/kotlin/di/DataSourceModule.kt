package org.baghdad.di

import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.runBlocking
import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.datasource.csv.CsvDataSourceImpl
import org.baghdad.data.datasource.csv.StorageFileNames
import org.baghdad.data.datasource.mapper.session.SessionMapper
import org.baghdad.data.datasource.mongodb.CollectionNames
import org.baghdad.data.datasource.mongodb.MongoDataSourceImpl
import org.baghdad.data.datasource.mongodb.MongoSetup
import org.baghdad.data.local.*
import org.baghdad.logic.model.entities.*
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File


inline fun <reified T : Any> Module.registerCsvDataSource(
    name: Entities,
    file: File,
    mapper: CsvMapper<T>
) {
    single<File>(named(name)) { file }
    single<CsvMapper<T>>(named(name)) { mapper }
    single<DataSource<T>>(named(name)) {
        CsvDataSourceImpl(
            get(named(name)),
            get(named(name))
        )
    }
}

inline fun <reified T : Identifiable> Module.registerMongoDataSource(
    name: Entities,
    collectionName: String
) {
    single<MongoCollection<T>>(named(name)) {
        get<MongoDatabase>().getCollection<T>(collectionName)
    }
    single<DataSource<T>>(named(name)) {
        MongoDataSourceImpl(get(named(name)))
    }
}

val dataSourceModule = module {

    // region  ::  CSV Data Sources  ::

//    registerCsvDataSource<AuditEntity>(Entities.Audit, StorageFileNames.auditFile, AuditMapper())
//    registerCsvDataSource<ProjectEntity>(Entities.Project, StorageFileNames.projectFile, ProjectMapper())
//    registerCsvDataSource<StateEntity>(Entities.State, StorageFileNames.stateFile, StateMapper())
//    registerCsvDataSource<UserEntity>(Entities.User, StorageFileNames.userFile, UserMapper())
    registerCsvDataSource<SessionEntity>(Entities.Session, StorageFileNames.sessionFile, SessionMapper())
//    registerCsvDataSource<TaskEntity>(Entities.Task, StorageFileNames.taskFile, TaskMapper())

    // endregion  ::  CSV Data Sources  ::

    // region  ::  Mongo Data Sources  ::
    // MongoDB connection
    single<MongoDatabase> {
        runBlocking {
            MongoSetup.setupConnection()
        }
    }

    registerMongoDataSource<UserEntity>(Entities.User, CollectionNames.USERS_COLLECTION)
    registerMongoDataSource<ProjectEntity>(Entities.Project, CollectionNames.PROJECTS_COLLECTION)
    registerMongoDataSource<StateEntity>(Entities.State, CollectionNames.PROJECT_STATES_COLLECTION)
    registerMongoDataSource<TaskEntity>(Entities.Task, CollectionNames.TASKS_COLLECTION)
    registerMongoDataSource<AuditLogEntity>(Entities.Audit, CollectionNames.AUDIT_COLLECTION)

    // endregion  ::  Mongo Data Sources  ::

    single { UserDataSource(get(named(Entities.User))) }
    single {
        SessionDataSource(get(named(Entities.Session)))
    }
    single {
        ProjectDataSource(
            get(named(Entities.Project)),
            get(named(Entities.State)),
            get(named(Entities.Task))
        )
    }
    single {
        ProjectStatesDataSource(
            get(named(Entities.State)),
            get(named(Entities.Task)),
        )
    }
    single { TaskDataSource(get(named(Entities.Task))) }
}