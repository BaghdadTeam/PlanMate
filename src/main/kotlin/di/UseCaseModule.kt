package org.baghdad.di

import org.baghdad.logic.usecase.StateTransitionUseCase
import org.baghdad.logic.usecase.ViewServiceUseCase
import org.baghdad.logic.usecase.audit.AddAuditUseCase
import org.baghdad.logic.usecase.audit.GetAuditByProjectIdUseCase
import org.baghdad.logic.usecase.audit.GetAuditByTaskIdUseCase
import org.baghdad.logic.usecase.authentication.LoginUseCase
import org.baghdad.logic.usecase.authentication.LogoutUseCase
import org.baghdad.logic.usecase.project.CreateProjectUseCase
import org.baghdad.logic.usecase.project.DeleteProjectUseCase
import org.baghdad.logic.usecase.project.EditProjectUseCase
import org.baghdad.logic.usecase.project.GetAllProjectsUseCase
import org.baghdad.logic.usecase.projectstates.AddStateToProjectUseCase
import org.baghdad.logic.usecase.projectstates.DeleteStateForProjectUseCase
import org.baghdad.logic.usecase.projectstates.EditProjectStatesUseCase
import org.baghdad.logic.usecase.projectstates.GetAllStatesPerProjectUseCase
import org.baghdad.logic.usecase.projectstates.GetStateByIdUseCase
import org.baghdad.logic.usecase.task.CreateTaskUseCase
import org.baghdad.logic.usecase.task.DeleteTaskUseCase
import org.baghdad.logic.usecase.task.GetAllTasksUseCase
import org.baghdad.logic.usecase.task.GetTasksByProjectIdUseCase
import org.baghdad.logic.usecase.task.GetTasksByStateIdUseCase
import org.baghdad.logic.usecase.task.UpdateTaskUseCase
import org.baghdad.logic.usecase.user.CreateUserUseCase
import org.baghdad.logic.usecase.user.UserValidatorUseCase
import org.koin.dsl.module

val useCaseModule = module {

    // region  ::  Task Use Cases  ::

    single { StateTransitionUseCase(get(), get(), get(), get()) }
    single { CreateTaskUseCase(get(), get(), get()) }
    single { DeleteTaskUseCase(get(), get(), get()) }
    single { UpdateTaskUseCase(get(), get(), get()) }
    single { GetTasksByStateIdUseCase(get()) }
    single { GetAllTasksUseCase(get()) }
    single { GetTasksByProjectIdUseCase(get()) }

    // endregion

    // region  ::  Project States Use Cases  ::

    single { AddStateToProjectUseCase(get(), get(), get()) }
    single { DeleteStateForProjectUseCase(get(), get(), get()) }
    single { GetAllStatesPerProjectUseCase(get()) }
    single { GetStateByIdUseCase(get()) }
    single { EditProjectStatesUseCase(get(), get(), get()) }

    // endregion

    // region  ::  Project Use Cases  ::

    single { CreateProjectUseCase(get(), get(), get()) }
    single { DeleteProjectUseCase(get(), get(), get()) }
    single { EditProjectUseCase(get(), get(), get()) }
    single { GetAllProjectsUseCase(get()) }
    // endregion

    // region  ::  Auth Use Cases  ::
    single { LoginUseCase(get(), get(), get()) }
    single{ LogoutUseCase(get())}
    // endregion

    // region :: User ::
    single { CreateUserUseCase(get(), get()) }
    single { UserValidatorUseCase(get()) }
    // endregion

    // region :: Audit ::
    single { AddAuditUseCase(get()) }
    single { GetAuditByTaskIdUseCase(get()) }
    single { GetAuditByProjectIdUseCase(get() , get() , get()) }
    // endregion

    // region :: Swimlane ::
    single { ViewServiceUseCase(get(), get()) }

}