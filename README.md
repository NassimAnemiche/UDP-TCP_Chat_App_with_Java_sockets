# UDP Chat

UDP chat with sequence numbers, ACKs, and packet loss tracking.

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

Type messages. Server shows:
```
[SEQ:1] 127.0.0.1:54321 - hello
```

Client shows:
```
hello
message received
```

Stop client: Ctrl+D or Ctrl+Z. Shows stats.
Stop server: Ctrl+C.

## What it does

- Sequence numbers on messages (SEQ|message format)
- Server sends ACK back to client
- Client tracks packet loss (2 sec timeout)
- Detects duplicates
- UTF-8, max 1024 bytes