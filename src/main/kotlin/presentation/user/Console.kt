package org.baghdad.presentation
//org.baghdad.presentation.Console.kt
//package org.baghdad.presentation

interface Console {
    /** يطبع الـprompt ثم يقرأ سطر من المستخدم */
    fun readLine(prompt: String = ""): String

    /** يطبع نص مع سطر جديد */
    fun writeLine(text: String = "")
}
