package org.baghdad.presentation.app

import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer


class ConsoleUI(
    private val features: Map<Int, Feature>,
    private val viewer: Viewer, private val reader: Reader
) {
    fun start() {
        showWelcome()
        presentFeatures()
    }

    private fun showWelcome() = viewer.logMessage("Welcome!")

    private fun getUserInput(): Int? = reader.readInput()?.toIntOrNull()

    private fun presentFeatures() {
        while (true) {
            showOptions()
            val option = getUserInput() ?: 0

            if (option == 0) {
                viewer.logMessage("Exiting... Goodbye!")
                break
            }

            features[option]?.execute() ?: viewer.logError("Invalid input, please try again.")

        }
    }

    private fun showOptions() {
        viewer.logMessage("\n=== Please enter one of the following numbers ===")
        features.values.sortedBy { it.id }.forEach {
            viewer.logMessage("${it.id}. ${it.name}")
        }
        viewer.logMessage("0. Exit")
        viewer.logMessage("Enter your choice: ")
    }

}