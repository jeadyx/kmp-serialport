package io.github.jeadyx.jserialport

/**
 * Desktop implementation of the SerialPortFactory.
 * Creates platform-specific SerialPort implementations based on the operating system.
 */
actual object SerialPortFactory {
    /**
     * Creates a new SerialPort instance appropriate for the current operating system.
     * Supports Windows and Linux platforms.
     *
     * @return A new SerialPort instance for the current platform
     * @throws UnsupportedOperationException if the operating system is not supported
     */
    actual fun create(): SerialPort {
        return DesktopSerialPort()
    }
} 