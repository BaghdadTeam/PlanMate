package org.baghdad.di

import org.baghdad.presentation.ConsoleUI
import org.baghdad.presentation.Feature
import org.koin.dsl.module


val uiModule = module {
//todo: add ui classes here..

//    factory { ExampleUI(get(), get(), get()) } bind Feature::class

    factory<Map<Int, Feature>> {
        getAll<Feature>().associateBy { it.id }
    }

//    single { ConsoleUI(get(), get(), get()) }
}