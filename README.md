# UDP Chat

Simple UDP chat with sequence numbers and acknowledgments.

## Setup

Need Java JDK 8+.

Compile:
```powershell
javac UDPServer.java UDPClient.java
```

## Run

Start server (Terminal 1):
```powershell
java UDPServer 9876
```

Start client (Terminal 2):
```powershell
java UDPClient localhost 9876
```

Type messages:
```
hello
>> message received
world
>> message received
```

Server shows:
```
[SEQ:1] 127.0.0.1:54321 - hello
[SEQ:2] 127.0.0.1:54321 - world
```

Stop: Ctrl+C

## What it does

- Each message gets a sequence number (1, 2, 3, ...)
- Client sends: SEQ|message
- Server receives and sends ACK back
- Client waits for ACK (2 second timeout)