package org.baghdad

import org.baghdad.di.*
import org.baghdad.presentation.app.StartApp
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin

fun main() {

    startKoin {
        printLogger()
        modules(appModule, useCaseModule, repositoryModule, uiModule, dataSourceModule)
    }
    val startApp = getKoin().get<StartApp>()
    startApp.run()
}