package org.baghdad

import org.baghdad.data.local.ProjectDataSource
import org.baghdad.di.appModule
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.ProjectEntity
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.mp.KoinPlatform.getKoin

fun main() {
    startKoin {
        modules(appModule)
    }

    val test = ProjectDataSource(
        getKoin().get(qualifier = named(Entities.Project.name)),
        stateDataSource = getKoin().get(qualifier = named(Entities.State.name)),
        taskDataSource = getKoin().get(qualifier = named(Entities.Task.name))
    )
    test.createProject(
        project = ProjectEntity(
            name = "",
            creatorId = ""
        )
    )
}