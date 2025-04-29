package org.baghdad.di

import org.baghdad.data.datasource.CsvParser
import org.baghdad.data.datasource.csv.CsvDataSourceImpl
import org.baghdad.data.datasource.csv.CsvReader
import org.baghdad.data.datasource.csv.CsvWriter
import org.baghdad.data.datasource.csv.StorageFileNames
import org.baghdad.data.datasource.parser.project.ProjectParser
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.datasource.parser.audit.AuditParser
import org.baghdad.data.datasource.parser.state.StateParser
import org.baghdad.data.datasource.parser.task.TaskParser
import org.baghdad.data.datasource.parser.user.UserParser
import org.baghdad.logic.model.entities.*
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

inline fun <reified T : Any> Module.bindCsvDataSource(name: Entities) {
    single<DataSource<T>> {
        CsvDataSourceImpl(
            get(named(name)),
            get(named(name)),
            get(named(name))
        )
    }
}

val appModule = module {


    val entitiesInfo = listOf(
        Triple(Entities.Audit, StorageFileNames.auditFile, AuditParser()),
        Triple(Entities.User, StorageFileNames.userFile, UserParser()),
        Triple(Entities.Project, StorageFileNames.projectFile, ProjectParser()),
        Triple(Entities.State, StorageFileNames.stateFile, StateParser()),
        Triple(Entities.Task, StorageFileNames.taskFile, TaskParser()),
    )

    entitiesInfo.forEach { (name, file, parser) ->
        single<File>(named(name)) { file }
        single(named(name)) { CsvWriter(get(named(name))) }
        single(named(name)) { CsvReader(get(named(name))) }
        single<CsvParser<*>>(named(name)) { parser }
    }

    bindCsvDataSource<AuditEntity>(Entities.Audit)
    bindCsvDataSource<UserEntity>(Entities.User)
    bindCsvDataSource<ProjectEntity>(Entities.Project)
    bindCsvDataSource<StateEntity>(Entities.State)
    bindCsvDataSource<UserEntity>(Entities.User)
}

