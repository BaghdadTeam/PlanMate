package org.baghdad

import org.baghdad.data.local.ProjectDataSource
import org.baghdad.di.appModule
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin

fun main() {
    startKoin {
        modules(appModule)
    }
}