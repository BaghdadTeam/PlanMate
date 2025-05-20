package org.baghdad.di

import data.datasource.local.csv.files.*
import org.baghdad.logic.model.enums.Entities
import org.koin.core.qualifier.named
import org.koin.dsl.module


val dataSourceModule = module {

    includes(remoteDataSourceModule, localDataSourceModule)

//    single { UserDataSource(get(named(Entities.User))) }
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
    single { AuditDataSource(get(named(Entities.Audit))) }
}
