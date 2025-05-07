package io.github.jeadyx.jserialport

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import com.fazecast.jSerialComm.SerialPort as JSerialCommPort

/**
 * Windows implementation of the SerialPort interface using jSerialComm library.
 * This class provides serial port communication functionality for Windows platforms.
 */
class DesktopSerialPort : SerialPort {
    private var serialPort: JSerialCommPort? = null
    
    /**
     * Indicates whether the serial port is currently open.
     * @return true if the port is open, false otherwise
     */
    override val isOpen: Boolean
        get() = serialPort?.isOpen ?: false
    
    /**
     * Opens a serial port with the specified parameters.
     * Uses jSerialComm library to configure and open the port.
     *
     * @param portName The name of the serial port to open (e.g., "COM1")
     * @param baudRate The baud rate for the connection
     * @param dataBits The number of data bits (default: 8)
     * @param stopBits The number of stop bits (default: 1)
     * @param parity The parity setting (default: PARITY_NONE)
     * @throws SerialPortException if the port cannot be opened
     */
    override suspend fun open(
        portName: String,
        baudRate: Int,
        dataBits: Int,
        stopBits: Int,
        parity: Int
    ) = withContext(Dispatchers.IO) {
        try {
            serialPort = JSerialCommPort.getCommPort(portName).apply {
                setBaudRate(baudRate)
                setNumDataBits(dataBits)
                setNumStopBits(stopBits)
                setParity(parity)
                setComPortTimeouts(JSerialCommPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0)
                
                if (!openPort()) {
                    throw SerialPortException("Failed to open port")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw SerialPortException("Failed: ${e.message}", e)
        }
    }
    
    /**
     * Closes the serial port connection.
     * This method should be called when the serial port is no longer needed.
     *
     * @throws SerialPortException if the port cannot be closed
     */
    override suspend fun close() = withContext(Dispatchers.IO) {
        try {
            serialPort?.closePort()
            serialPort = null
        } catch (e: Exception) {
            e.printStackTrace()
            throw SerialPortException("Failed to close serial port: ${e.message}", e)
        }
    }
    
    /**
     * Writes data to the serial port.
     *
     * @param data The byte array to write to the port
     * @throws SerialPortException if the write operation fails or if not all data is written
     */
    override suspend fun write(data: ByteArray) {
        withContext(Dispatchers.IO) {
            try {
                val bytesWritten = serialPort?.writeBytes(data, data.size)
                if (bytesWritten != data.size) {
                    throw SerialPortException("Failed to write all data: wrote $bytesWritten of ${data.size} bytes")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw SerialPortException("Failed to write data: ${e.message}", e)
            }
        }
    }
    
    /**
     * Returns a Flow of data read from the serial port.
     * The Flow will emit ByteArrays containing the received data.
     * Reading continues as long as the port is open.
     *
     * @return A Flow of ByteArrays containing the received data
     * @throws SerialPortException if the read operation fails
     */
    override fun read(): Flow<ByteArray> = flow {
        val buffer = ByteArray(1024)
        while (isOpen) {
            try {
                if((serialPort?.bytesAvailable()?:0) > 0) {
//                    Thread.sleep(100)
                    val bytesRead = serialPort?.readBytes(buffer, buffer.size) ?: 0
                    if (bytesRead > 0) {
                        emit(buffer.copyOf(bytesRead))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw SerialPortException("Failed to read data: ${e.message}", e)
            }
        }
    }.flowOn(Dispatchers.IO)
}

/**
 * Exception thrown when a serial port operation fails.
 *
 * @param message A description of the error
 * @param cause The underlying cause of the error
 */
class SerialPortException(message: String, cause: Throwable? = null) : Exception(message, cause) 