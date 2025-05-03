package org.baghdad.di

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.datasource.csv.*
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.datasource.mapper.project.ProjectMapper
import org.baghdad.data.datasource.mapper.audit.AuditMapper
import org.baghdad.data.datasource.mapper.session.SessionMapper
import org.baghdad.data.datasource.mapper.state.StateMapper
import org.baghdad.data.datasource.mapper.task.TaskMapper
import org.baghdad.data.datasource.mapper.user.UserMapper
import org.baghdad.data.local.UserDataSource
import org.baghdad.data.repository.user.UserRepositoryImpl
import org.baghdad.logic.model.entities.*
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.presentation.PlanMateCLI
import org.baghdad.presentation.user.CreateUserUI
import org.baghdad.presentation.user.GetUserUI
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
        Triple(Entities.Session, StorageFileNames.sessionFile, SessionMapper())
    )

    entitiesInfo.forEach { (name, file, parser) ->
        single<File>(named(name)) { file }
        single(named(name)) { CsvWriter(get(named(name))) }
        single(named(name)) { CsvReader(get(named(name))) }
        single<CsvMapper<*>>(named(name)) { parser }
        //  bindCsvDataSource<Any>(entity) // تصحيح النوع لـ T الفعلي

    }

    bindCsvDataSource<AuditEntity>(Entities.Audit)
    bindCsvDataSource<ProjectEntity>(Entities.Project)
    bindCsvDataSource<StateEntity>(Entities.State)
    bindCsvDataSource<UserEntity>(Entities.User)
    bindCsvDataSource<SessionEntity>(Entities.Session)
    bindCsvDataSource<TaskEntity>(Entities.Task)

    // Data Sources & Repositories
    single { UserDataSource(get(named(Entities.User.name))) }
    single<UserRepository> { UserRepositoryImpl(get()) }

    single { CreateUserUI(get(), get(),get()) }
    single { GetUserUI(get(), get(),get()) }
    single { PlanMateCLI(get(),get(),get(),get()) }
}


