package io.github.jeadyx.jserialport.example

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.jeadyx.jserialport.example.ui.theme.JSerialPortTheme
import kotlinx.coroutines.launch
import io.github.jeadyx.jserialport.SerialPort
import io.github.jeadyx.jserialport.SerialPortFactory

@Composable
fun App(defaultPortName: String = "") {
    var portName by remember { mutableStateOf(defaultPortName) }
    var baudRate by remember { mutableStateOf("9600") }
    var receivedData by remember { mutableStateOf("") }
    var isConnected by remember { mutableStateOf(false) }
    var serialPort by remember { mutableStateOf<SerialPort?>(null) }
    val scope = rememberCoroutineScope()
    val textFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color(0xfffafafa)
    )
    JSerialPortTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = portName,
                    onValueChange = { portName = it },
                    label = { Text("端口号(PortName)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = baudRate,
                    onValueChange = { baudRate = it },
                    label = { Text("波特率(BaudRate)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        scope.launch {
                            try {
                                if (!isConnected) {
                                    serialPort = SerialPortFactory.create()
                                    serialPort?.open(
                                        portName = portName,
                                        baudRate = baudRate.toIntOrNull() ?: 9600
                                    )
                                    isConnected = true

                                    // Start reading data
                                    serialPort?.read()?.collect { data ->
                                        receivedData += String(data) + "\n"
                                    }
                                } else {
                                    serialPort?.close()
                                    serialPort = null
                                    isConnected = false
                                }
                            } catch (e: Exception) {
                                receivedData += "Error: ${e.message}\n"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text(if (isConnected) "断开(Disconnect)" else "连接(Connect)")
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = receivedData,
                    onValueChange = { },
                    label = { Text("数据接收区(ReceivedData)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    readOnly = true,
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    var message by remember { mutableStateOf("") }

                    TextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("点击输入消息(ASCIIMessage)") },
                        modifier = Modifier.weight(1f),
                        colors = textFieldColors
                    )

                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    serialPort?.write(message.toByteArray())
                                    message = ""
                                } catch (e: Exception) {
                                    receivedData += "Error: ${e.message}\n"
                                }
                            }
                        },
                        Modifier.height(50.dp),
                        enabled = isConnected
                    ) {
                        Text("发送(Send)")
                    }
                }
            }
        }
    }
} 