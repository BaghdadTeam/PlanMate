package org.baghdad

import org.baghdad.data.local.ProjectDataSource
import org.baghdad.di.appModule
import org.baghdad.di.uiModule
import org.baghdad.di.useCaseModule
import org.baghdad.logic.model.entities.Entities
import org.baghdad.presentation.ConsoleUI
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.mp.KoinPlatform.getKoin

fun main() {
    startKoin {
        modules(appModule, useCaseModule, uiModule)
    }

    val test = ProjectDataSource(
        getKoin().get(qualifier = named(Entities.Project.name))
    )
    test.createProject()

    val ui: ConsoleUI = getKoin().get()
    ui.start()
}