package org.baghdad

import di.presentationModuleWithReaderAndViewer
import org.baghdad.di.appModule
import org.baghdad.di.domainModule
import org.baghdad.presentation.PlanMateCLI
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin

fun main() {
    startKoin {
        modules(
            listOf(
                appModule,
                domainModule,
                presentationModuleWithReaderAndViewer
            )
        )
//        startKoin {
//            modules(appModule, useCaseModule)
//        }

        val cli: PlanMateCLI = getKoin().get()
        cli.start()
    }
}