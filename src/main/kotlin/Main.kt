package org.baghdad

import org.baghdad.di.appModule
import org.baghdad.di.domainModule
import org.baghdad.di.presentationModule
import org.baghdad.presentation.PlanMateCLI
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin

fun main() {
  startKoin {
      //        modules(appModule,)
//    }

//    val test = ProjectDataSource(
//        getKoin().get(qualifier = named(Entities.Project.name))
//    )
//    test.createProject()

      modules(listOf(
            appModule,
            domainModule,
            presentationModule
        ))
    }

    val cli: PlanMateCLI = getKoin().get()
    cli.start()
}