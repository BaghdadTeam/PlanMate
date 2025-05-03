package org.baghdad.di

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.datasource.csv.CsvDataSourceImpl
import org.baghdad.data.datasource.csv.CsvReader
import org.baghdad.data.datasource.csv.CsvWriter
import org.baghdad.data.datasource.csv.StorageFileNames
import org.baghdad.data.datasource.mapper.audit.AuditMapper
import org.baghdad.data.datasource.mapper.project.ProjectMapper
import org.baghdad.data.datasource.mapper.session.SessionMapper
import org.baghdad.data.datasource.mapper.state.StateMapper
import org.baghdad.data.datasource.mapper.task.TaskMapper
import org.baghdad.data.datasource.mapper.user.UserMapper
import org.baghdad.logic.model.entities.*
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

inline fun <reified T : Any> Module.bindCsvDataSource(name: Entities) {
    single<DataSource<T>>(named(name.name)) {
        CsvDataSourceImpl(
            get(named(name)),
            get(named(name)),
            get(named(name))
        )
    }
}

val dataSourceModule = module {

    val entitiesInfo = listOf(
        Triple(Entities.Audit, StorageFileNames.auditFile, AuditMapper()),
        Triple(Entities.User, StorageFileNames.userFile, UserMapper()),
        Triple(Entities.Project, StorageFileNames.projectFile, ProjectMapper()),
        Triple(Entities.State, StorageFileNames.stateFile, StateMapper()),
        Triple(Entities.Task, StorageFileNames.taskFile, TaskMapper()),
        Triple(Entities.Session, StorageFileNames.sessionFile, SessionMapper())
    )

    entitiesInfo.forEach { (name, file, parser) ->
        single<File>(named(name)) { file }
        single(named(name)) { CsvWriter(get(named(name))) }
        single(named(name)) { CsvReader(get(named(name))) }
        single<CsvMapper<*>>(named(name)) { parser }
    }

    bindCsvDataSource<AuditEntity>(Entities.Audit)
    single<DataSource<AuditEntity>> {get(named(Entities.Audit.name)) }

    bindCsvDataSource<ProjectEntity>(Entities.Project)
    single<DataSource<ProjectEntity>> {get(named(Entities.Project.name)) }

    bindCsvDataSource<StateEntity>(Entities.State)
    single<DataSource<StateEntity>> {get(named(Entities.State.name)) }

    bindCsvDataSource<UserEntity>(Entities.User)
    single<DataSource<UserEntity>> { get(named(Entities.User.name)) }

    bindCsvDataSource<SessionEntity>(Entities.Session)
    single<DataSource<SessionEntity>> {get(named(Entities.Session.name)) }

    bindCsvDataSource<TaskEntity>(Entities.Task)
    single<DataSource<TaskEntity>> {get(named(Entities.Task.name)) }


}

