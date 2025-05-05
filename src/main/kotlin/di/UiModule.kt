package org.baghdad.di

import org.baghdad.presentation.project.CreateProjectUi
import org.baghdad.presentation.project.DeleteProjectUi
import org.baghdad.presentation.project.ListProjectUi
import org.baghdad.presentation.task.*
import org.koin.dsl.module

val uiModule = module {

    single { CreateTaskUI(get(), get(), get(), get()) }
    single { DeleteTaskUI(get(), get(), get(), get()) }
    single { GetTasksByStateIdUI(get(), get(), get()) }
    single { GetTasksByStateIdUI(get(), get(), get()) }
    single { UpdateTaskUI(get(), get(), get(), get()) }
    single { GetTaskByIdUI(get(), get(), get()) }

    // --------------------ProjectUi----------------------
    single { CreateProjectUi(get()) }
    single { DeleteProjectUi(get()) }
    single { ListProjectUi(get()) }
}