package io.github.jeadyx.jserialport

import kotlinx.coroutines.flow.Flow

/**
 * Interface representing a serial port connection.
 * This interface provides methods for opening, closing, reading from, and writing to a serial port.
 */
interface SerialPort {
    /**
     * Indicates whether the serial port is currently open.
     */
    val isOpen: Boolean
    
    /**
     * Opens a serial port with the specified parameters.
     *
     * @param portName The name of the serial port to open
     * @param baudRate The baud rate for the connection
     * @param dataBits The number of data bits (default: 8)
     * @param stopBits The number of stop bits (default: 1)
     * @param parity The parity setting (default: PARITY_NONE)
     * @throws SerialPortException if the port cannot be opened
     */
    suspend fun open(
        portName: String,
        baudRate: Int,
        dataBits: Int = 8,
        stopBits: Int = 1,
        parity: Int = 0
    )
    
    /**
     * Closes the serial port connection.
     * This method should be called when the serial port is no longer needed.
     */
    suspend fun close()
    
    /**
     * Writes data to the serial port.
     *
     * @param data The byte array to write to the port
     * @throws SerialPortException if the write operation fails
     */
    suspend fun write(data: ByteArray)
    
    /**
     * Returns a Flow of data read from the serial port.
     * The Flow will emit ByteArrays containing the received data.
     *
     * @return A Flow of ByteArrays containing the received data
     */
    fun read(): Flow<ByteArray>
    
    companion object {
        /** No parity */
        const val PARITY_NONE = 0
        /** Odd parity */
        const val PARITY_ODD = 1
        /** Even parity */
        const val PARITY_EVEN = 2
        /** Mark parity */
        const val PARITY_MARK = 3
        /** Space parity */
        const val PARITY_SPACE = 4
    }
} 