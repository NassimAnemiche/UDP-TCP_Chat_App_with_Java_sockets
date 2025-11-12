# UDP-TCP_Chat_App_with_Java_sockets


## Prerequisites

- Java JDK 8 or later installed and available on your PATH.
- (Optional) `nc` / `ncat` / `netcat` for sending UDP test messages from another terminal.


## Run the server

Start the UDP server. By default it listens on port 9876. You can optionally pass a port as the
first argument.

Default port (9876):

```powershell
java UDPServer
```

Specify a port (example: 8080):

```powershell
java UDPServer 8080
```

The server prints a startup message such as:

```
UDPServer listening on port 9876
```

To stop the server, press Ctrl+C in the terminal where it is running.

## Testing with netcat / ncat

From another terminal send a UDP datagram to the server. Examples:

- Linux / macOS (netcat with `-u`):

```bash
echo -n "Hello from netcat" | nc -u 127.0.0.1 9876
```

- Windows with `ncat` (from Nmap) or similar:

```powershell
"Hello from ncat" | ncat -u 127.0.0.1 9876
```

After sending, the server should print a line like:

```
127.0.0.1:53045 - Hello from netcat
```

Notes:

- The server uses a fixed 1024-byte buffer; messages larger than 1024 bytes (when encoded in UTF-8)
	will be truncated.
- The server runs single-threaded and blocks inside the receive loop. For production use you may
	want to add threading, graceful shutdown hooks, or more robust logging.

## UDPClient



```powershell
java UDPClient <host> <port>
# Example:
java UDPClient localhost 9876
```

Type lines and press Enter to send each one. To exit:
- On Windows PowerShell: press Ctrl+Z then Enter to signal EOF.
- On Linux/macOS: press Ctrl+D.

Send a single message non-interactively (piped):

```powershell
"Hello from UDPClient" | java UDPClient localhost 9876
```
    