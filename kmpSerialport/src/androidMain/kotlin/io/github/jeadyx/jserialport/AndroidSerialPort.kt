package io.github.jeadyx.jserialport

import android.serialport.SerialPort
import android.util.Log
import android.util.Log.e
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Files.size

/**
 * Android implementation of the SerialPort interface using Android's SerialPort library.
 * This class provides serial port communication functionality for Android devices.
 */
class AndroidSerialPort : io.github.jeadyx.jserialport.SerialPort {
    private var serialPort: SerialPort? = null
    private var inputStream: java.io.InputStream? = null
    private var outputStream: java.io.OutputStream? = null
    
    /**
     * Indicates whether the serial port is currently open.
     * @return true if the port is open, false otherwise
     */
    override val isOpen: Boolean
        get() = serialPort != null
    
    /**
     * Opens a serial port with the specified parameters.
     * Uses Android's SerialPort library to configure and open the port.
     *
     * @param portName The path to the serial port device (e.g., "/dev/ttyUSB0")
     * @param baudRate The baud rate for the connection
     * @param dataBits The number of data bits (not used in Android implementation)
     * @param stopBits The number of stop bits (not used in Android implementation)
     * @param parity The parity setting (not used in Android implementation)
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
            val device = File(portName)
            if(!device.exists()) throw FileNotFoundException("Device $portName not found")
            serialPort = SerialPort.newBuilder(device, baudRate)
                .dataBits(dataBits)
                .stopBits(stopBits)
                .parity(parity)
                .build()
            serialPort?:run{
                throw SerialPortException("Failed to open port")
            }
            inputStream = serialPort?.inputStream
            outputStream = serialPort?.outputStream
        } catch (e: IOException) {
            throw SerialPortException("Failed: ${e.message}", e)
        }
    }
    
    /**
     * Closes the serial port connection and its associated streams.
     * This method should be called when the serial port is no longer needed.
     *
     * @throws SerialPortException if the port cannot be closed
     */
    override suspend fun close() = withContext(Dispatchers.IO) {
        try {
            inputStream?.close()
            outputStream?.close()
            serialPort?.close()
            serialPort = null
            inputStream = null
            outputStream = null
        } catch (e: IOException) {
            throw SerialPortException("Failed to close serial port: ${e.message}", e)
        }
    }
    
    /**
     * Writes data to the serial port.
     *
     * @param data The byte array to write to the port
     * @throws SerialPortException if the write operation fails
     */
    override suspend fun write(data: ByteArray): Unit = withContext(Dispatchers.IO) {
        try {
            outputStream?.write(data)
            outputStream?.flush()
        } catch (e: IOException) {
            throw SerialPortException("Failed to write data: ${e.message}", e)
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
        while (isOpen) {
            try {
                val buffer = ByteArray(256)
                val size = inputStream?.read(buffer) ?: -1
                if (size > 0) {
                    emit(buffer.copyOf(size))
                }
            } catch (e: IOException) {
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