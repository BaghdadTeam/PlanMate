package org.baghdad.di

import org.baghdad.logic.usecase.projectstates.DeleteStateForProjectUseCase
import org.baghdad.presentation.authentication.LoginUi
import org.baghdad.presentation.project.CreateProjectUi
import org.baghdad.presentation.project.DeleteProjectUi
import org.baghdad.presentation.project.EditProjectUi
import org.baghdad.presentation.project.GetAllProjectsUi
import org.baghdad.presentation.project.ProjectUi
import org.baghdad.presentation.projectStates.AddStateToProjectUI
import org.baghdad.presentation.projectStates.EditProjectStateUI
import org.baghdad.presentation.projectStates.GetAllStatesPerProjectUI
import org.baghdad.presentation.projectStates.GetStateByIdUI
import org.baghdad.presentation.swimlane.RenderSwimlaneUI
import org.baghdad.presentation.swimlane.SwimlaneUI
import org.baghdad.presentation.task.CreateTaskUI
import org.baghdad.presentation.task.DeleteTaskUI
import org.baghdad.presentation.task.GetTasksByStateIdUI
import org.baghdad.presentation.task.UpdateTaskUI
import org.baghdad.presentation.user.CreateUserUI
import org.koin.dsl.module

val uiModule = module {

    single { CreateTaskUI(get(), get(), get(), get()) }
    single { DeleteTaskUI(get(), get(), get(), get()) }
    single { GetTasksByStateIdUI(get(), get(), get()) }
    single { GetTasksByStateIdUI(get(), get(), get()) }
    single { UpdateTaskUI(get(), get(), get(), get()) }
    // endregion

    // region  :: PROJECT STATES ::

    single { AddStateToProjectUI(get(), get(), get(), get()) }
    single { EditProjectStateUI(get(), get(), get(), get()) }
    single { DeleteStateForProjectUseCase(get(), get(), get()) }
    single { GetAllStatesPerProjectUI(get(), get(), get()) }
    single { GetStateByIdUI(get(), get(), get()) }

    // endregion

    // region :: ProjectUi ::
    single { CreateProjectUi(get(), get(), get(), get()) }
    single { DeleteProjectUi(get(), get(), get(), get(), get()) }
    single { GetAllProjectsUi(get(), get()) }
    single { ProjectUi(get(), get(), get(), get(), get(), get()) }
    single { EditProjectUi(get(), get(), get(), get(), get()) }
    // endregion

    // region :: Auth ::
    single { LoginUi(get(), get(), get()) }

    // region :: User ::
    single { CreateUserUI(get(), get(), get(), get()) }
    // endregion

    // region :: Swimlane ::
    single { RenderSwimlaneUI(get()) }
    single { SwimlaneUI(get()) }
    // endregion
}