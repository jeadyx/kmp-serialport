# JSerialPort

JSerialPort 是一个 Kotlin Multiplatform 串口通信库，支持 Android、Windows 和 Linux 平台。

## 示例项目
[JSerialPort Example](example)

## 克隆仓库
### Github
```shell
git clone https://github.com/jeadyx/kmp-serialport.git
```
### Gitee(国内访问)
```shell
git clone https://gitee.com/jeadyx/kmp-serialport.git
```

## 特性

- 支持 Android、Windows 和 Linux 平台
- 使用 Kotlin Coroutines 和 Flow 进行异步操作
- 简单易用的 API
- 支持自定义波特率、数据位、停止位和校验位

## 安装

在项目的 `build.gradle.kts` 文件中添加以下依赖：

```kotlin
dependencies {
    implementation("io.github.jeadyx:kmp-serialport:1.0.0")
}
```

## 使用方法

### 基本用法

```kotlin
// 创建串口实例
val serialPort = SerialPortFactory.create()

// 打开串口
serialPort.open(
    portName = "/dev/ttyUSB0", // Linux
    // portName = "COM1", // Windows
    // portName = "/dev/ttyS0", // Android
    baudRate = 9600,
    dataBits = 8,
    stopBits = 1,
    parity = SerialPort.PARITY_NONE
)

// 写入数据
serialPort.write("Hello".toByteArray())

// 读取数据
serialPort.read().collect { data ->
    println("Received: ${String(data)}")
}

// 关闭串口
serialPort.close()
```

### 错误处理

```kotlin
try {
    serialPort.open("/dev/ttyUSB0", 9600)
} catch (e: SerialPortException) {
    println("Failed to open serial port: ${e.message}")
}
```

## 许可证

MIT License 


# Donate
![donate.png](imgs/donate.png)