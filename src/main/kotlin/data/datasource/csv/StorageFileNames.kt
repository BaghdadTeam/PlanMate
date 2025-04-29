package org.baghdad.data.datasource.csv

import java.io.File

object StorageFileNames {
    val auditFile = File("audit.csv")
    val userFile = File("user.csv")
    val taskFile = File("task.csv")
    val projectFile = File("project.csv")
    val stateFile = File("state.csv")
    val sessionFile = File("session.csv")
}