package org.baghdad.di

import org.baghdad.presentation.task.*
import org.baghdad.presentation.project.ProjectUi
import org.koin.dsl.module

val uiModule = module {

    single { CreateTaskUI(get(), get(), get(), get()) }
    single { DeleteTaskUI(get(), get(), get(), get()) }
    single { GetTasksByStateIdUI(get(), get(), get()) }
    single { GetTasksByStateIdUI(get(), get(), get()) }
    single { UpdateTaskUI(get(), get(), get(), get()) }
    single { GetTaskByIdUI(get(), get(), get()) }
    single { ProjectUi(get(), get(), get(), get()) }
}