package org.baghdad.di

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.datasource.local.csv.CsvDataSourceImpl
import org.baghdad.data.datasource.local.csv.StorageFileNames
import org.baghdad.data.datasource.mapper.session.SessionMapper
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.enums.Entities
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

val localDataSourceModule = module {

    // region  ::  CSV Data Sources  ::

//    registerCsvDataSource<AuditLogDto>(Entities.Audit, StorageFileNames.auditFile, AuditMapper())
//    registerCsvDataSource<ProjectDto>(Entities.Project, StorageFileNames.projectFile, ProjectMapper())
//    registerCsvDataSource<StateEntity>(Entities.State, StorageFileNames.stateFile, StateMapper())
//    registerCsvDataSource<UserEntity>(Entities.User, StorageFileNames.userFile, UserMapper())
    registerCsvDataSource<SessionEntity>(Entities.Session, StorageFileNames.sessionFile, SessionMapper())
//    registerCsvDataSource<TaskEntity>(Entities.Task, StorageFileNames.taskFile, TaskMapper())

}