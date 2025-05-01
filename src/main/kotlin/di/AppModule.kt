package org.baghdad.di

import logic.presentation.ReportUI
import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.datasource.csv.*
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.datasource.mapper.project.ProjectMapper
import org.baghdad.data.datasource.mapper.audit.AuditMapper
import org.baghdad.data.datasource.mapper.state.StateMapper
import org.baghdad.data.datasource.mapper.task.TaskMapper
import org.baghdad.data.datasource.mapper.user.UserMapper
import org.baghdad.logic.model.entities.*
import org.baghdad.logic.usecase.report.ReportService
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

val appModule = module {


    val entitiesInfo = listOf(
        Triple(Entities.Audit, StorageFileNames.auditFile, AuditMapper()),
        Triple(Entities.User, StorageFileNames.userFile, UserMapper()),
        Triple(Entities.Project, StorageFileNames.projectFile, ProjectMapper()),
        Triple(Entities.State, StorageFileNames.stateFile, StateMapper()),
        Triple(Entities.Task, StorageFileNames.taskFile, TaskMapper()),
    )

    entitiesInfo.forEach { (name, file, parser) ->
        single<File>(named(name)) { file }
        single(named(name)) { CsvWriter(get(named(name))) }
        single(named(name)) { CsvReader(get(named(name))) }
        single<CsvMapper<*>>(named(name)) { parser }
    }

    bindCsvDataSource<AuditEntity>(Entities.Audit)
    bindCsvDataSource<UserEntity>(Entities.User)
    bindCsvDataSource<ProjectEntity>(Entities.Project)
    bindCsvDataSource<StateEntity>(Entities.State)
    bindCsvDataSource<TaskEntity>(Entities.Task)

    single { ReportService( get(), get(), get() ) }
    single { ReportUI( get() ) }
}

