package org.baghdad.presentation

//todo: uncomment lines when viewer and reader merged
//todo: use test cases (same as the last one), when finished this class only small things will be edited there
class ConsoleUI(
    private val features: Map<Int, Feature>,
//    private val viewer: Viewer, private val reader: Reader
) {
    fun start() {
//        showWelcome()
//        presentFeatures()
    }

//    private fun showWelcome() = viewer.log("Welcome to Food Change Mood!")

//    private fun getUserInput(): Int? = reader.readInput()?.toIntOrNull()

//    private fun presentFeatures() {
//        while (true) {
//            showOptions()
//            val option = getUserInput() ?: 0
//
//            if (option == 0) {
//                viewer.log("Exiting... Goodbye!")
//                break
//            }
//
//            features[option]?.execute() ?: viewer.log("Invalid input, please try again.")
//
//        }
//    }

//    private fun showOptions() {
//        viewer.log("\n=== Please enter one of the following numbers ===")
//        features.values.sortedBy { it.id }.forEach {
//            viewer.log("${it.id}. ${it.name}")
//        }
//        viewer.log("0. Exit")
//        viewer.log("Enter your choice: ")
//    }

}