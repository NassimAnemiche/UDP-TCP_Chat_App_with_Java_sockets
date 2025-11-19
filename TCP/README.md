# TCP Chat

Simple TCP client–server chat using sockets.

## Files

- `TCPServer.java` — listens for one client, echoes messages back, keeps message history (last 10).
- `TCPClient.java` — connects to server, sends lines from stdin, receives and prints responses.
- `ConnectionThread.java` — utility for handling connections in threads (optional).

## Compile

```powershell
cd TCP
javac TCPServer.java TCPClient.java
```

## Run

Start server (Terminal 1):
```powershell
java TCPServer 8080
```

Start client (Terminal 2):
```powershell
java TCPClient localhost 8080
```

Type messages and press Enter. Server echoes back each message with client ID and IP. Type `quit` or `exit` to disconnect.

## Features

- UTF-8 encoding
- Client connection logging (timestamp, client ID, IP)
- Message history (last 10 messages)
- Automatic reconnect with 2-second timeout
- Input validation (ignores empty lines)

## Notes

- Server accepts one client at a time (simple version). For multiple concurrent clients, use threads (see `ConnectionThread.java`).
- TCP guarantees reliable, ordered delivery (unlike UDP).
