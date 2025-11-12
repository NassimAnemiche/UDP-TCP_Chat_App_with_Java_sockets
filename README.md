# UDP-TCP_Chat_App_with_Java_sockets

This repository contains a small example UDP server implemented in Java (`UDPServer.java`) and
a placeholder `Main.java` to keep the project compiling. The UDP server listens for UTF-8 encoded
datagrams (up to 1024 bytes) and prints received messages prefixed with the sender's address.

## Prerequisites

- Java JDK 8 or later installed and available on your PATH.
- (Optional) `nc` / `ncat` / `netcat` for sending UDP test messages from another terminal.

## Compile

Open PowerShell and run (from the repository root):

```powershell
cd C:\Users\nanem\UDP-TCP_Chat_App_with_Java_sockets
javac UDPServer.java Main.java
```

This produces `.class` files that you can run with the `java` command.

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

From another terminal (can be the same machine) send a UDP datagram to the server. Examples:

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

## Next steps (optional)

- Add a small UDP client class for automated testing.
- Add a README section describing integration with a TCP chat component if you extend this example.

If you want me to add a client or automated tests, tell me which you'd prefer and I'll add it.