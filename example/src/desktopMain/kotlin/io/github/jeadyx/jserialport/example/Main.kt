package io.github.jeadyx.jserialport.example

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "JSerialPort Example") {
        val osName = System.getProperty("os.name").lowercase()
        val portName = when {
            osName.contains("windows") -> "COM4"
            osName.contains("linux") -> "/dev/ttyWK0"
            else -> throw UnsupportedOperationException("Unsupported operating system: $osName")
        }
        App(portName)
    }
} 