package org.baghdad

import org.baghdad.data.local.ProjectDataSource
import org.baghdad.di.appModule
import org.baghdad.logic.model.entities.Entities
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.mp.KoinPlatform.getKoin

fun main() {
    startKoin {
        modules(appModule)
    }

    val test = ProjectDataSource(
        getKoin().get(qualifier = named(Entities.Project.name))
    )
    test.createProject()
}