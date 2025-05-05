package org.baghdad.di

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.datasource.csv.CsvDataSourceImpl
import org.baghdad.data.datasource.csv.StorageFileNames
import org.baghdad.data.datasource.mapper.audit.AuditMapper
import org.baghdad.data.datasource.mapper.project.ProjectMapper
import org.baghdad.data.datasource.mapper.session.SessionMapper
import org.baghdad.data.datasource.mapper.state.StateMapper
import org.baghdad.data.datasource.mapper.task.TaskMapper
import org.baghdad.data.datasource.mapper.user.UserMapper
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

val dataSourceModule = module {

    registerCsvDataSource<AuditEntity>(Entities.Audit, StorageFileNames.auditFile, AuditMapper())
    registerCsvDataSource<ProjectEntity>(Entities.Project, StorageFileNames.projectFile, ProjectMapper())
    registerCsvDataSource<StateEntity>(Entities.State, StorageFileNames.stateFile, StateMapper())
    registerCsvDataSource<UserEntity>(Entities.User, StorageFileNames.userFile, UserMapper())
    registerCsvDataSource<SessionEntity>(Entities.Session, StorageFileNames.sessionFile, SessionMapper())
    registerCsvDataSource<TaskEntity>(Entities.Task, StorageFileNames.taskFile, TaskMapper())

    single { UserDataSource(get(named(Entities.User))) }
    single { SessionDataSource(get(named(Entities.Session)))
    }
    single { ProjectDataSource(
            get(named(Entities.Project)),
            get(named(Entities.State)),
            get(named(Entities.Task))
        )
    }
    single { ProjectStatesDataSource(
            get(named(Entities.State)),
            get(named(Entities.Task)),
        )
    }
    single { TaskDataSource(get(named(Entities.Task))) }
}