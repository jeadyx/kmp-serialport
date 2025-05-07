package io.github.jeadyx.jserialport

/**
 * Android implementation of the SerialPortFactory.
 * Creates an AndroidSerialPort instance for serial port communication on Android devices.
 */
actual object SerialPortFactory {
    /**
     * Creates a new AndroidSerialPort instance.
     *
     * @return A new SerialPort instance for Android platform
     */
    actual fun create(): SerialPort = AndroidSerialPort()
} 