package io.github.jeadyx.jserialport

/**
 * Factory object for creating platform-specific SerialPort implementations.
 * This is an expect declaration that must be implemented for each platform.
 */
expect object SerialPortFactory {
    /**
     * Creates a new instance of a platform-specific SerialPort implementation.
     *
     * @return A new SerialPort instance
     */
    fun create(): SerialPort
} 