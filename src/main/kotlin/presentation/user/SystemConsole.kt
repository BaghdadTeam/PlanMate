package org.baghdad.presentation


import java.util.Scanner

class SystemConsole : Console {
    private val scanner = Scanner(System.`in`)

    override fun readLine(prompt: String): String {
        if (prompt.isNotEmpty()) print(prompt)
        return scanner.nextLine()
    }

    override fun writeLine(text: String) = println(text)
}