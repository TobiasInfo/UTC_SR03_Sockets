# Chat Application using Sockets

## Project Overview

### Description
This project involves developing a client/server chat application using sockets to enable public discussion among multiple participants. The chat messages are displayed in the console.

### Key Features
- **Multi-threaded**: Uses threads to manage simultaneous clients.
- **Unique Pseudonyms**: Ensures each client has a unique pseudonym.
- **Handles Unexpected Disconnections**: Manages client and server disconnections gracefully.

### Technologies Used
- Java
- Sockets
- Multi-threading

## Project Structure

### Key Components
1. **Server**: Listens for incoming client connections and manages message broadcasting.
2. **Client**: Connects to the server and participates in the chat.

### Files
- `Server.java`: Contains the server-side logic.
- `Client.java`: Contains the client-side logic.
- `MessageReceptor.java`: Handles incoming messages for the client.
- `IncomingMessageHandler.java`: Manages incoming messages for the server.

## Usage

### Local Setup
1. Open the project in IntelliJ IDEA.
2. Run an instance of the `Server` class.
3. Run one or more instances of the `Client` class.

### Network Setup
1. Open the project in IntelliJ IDEA.
2. Modify the IP address and port in the `Client` class to match the server's details.
3. Run the `Server` class.
4. Run one or more instances of the `Client` class.

## Implementation Details

### Unique Pseudonyms
To ensure unique pseudonyms, the server maintains a list of active pseudonyms. When a new client connects, the server checks the list. If the pseudonym is taken, the client is prompted to choose another.

### Handling Disconnections
- **Client-Side**: A static `isRunning` variable in `MessageReceptor` detects unexpected disconnections.
- **Server-Side**: The server uses a similar approach with `IncomingMessageHandler` to handle its own disconnections.

## Code Examples

### Server Class

```java
private static final List<String> pseudo = new ArrayList<>();

public static boolean addPseudo(String userName, Socket socket) {
    if (pseudo.contains(userName)) {
        return false;
    } else {
        clients.add(socket);
        pseudo.add(userName);
        return true;
    }
}
```

### Client-Side Disconnection Handling

```java

private boolean isRunning = true;

public void setRunning(boolean running) {
    isRunning = running;
}

@Override
public void run() {
    while (isRunning()) {
        // code to read incoming messages
    }
}
```
### Server-Side Disconnection Handling

```java

private boolean isRunning = true;

public void setRunning(boolean running) {
    isRunning = running;
}

@Override
public void run() {
    while (isRunning() && !socket.isClosed()) {
        // code to read incoming messages
    }
}
```
## Authors

- Tobias Savary
- Marius Bureau

## University

Université de Technologie de Compiègne (UTC)

## Conclusion

This project successfully implemented a multi-threaded chat application in Java, facilitating public discussion among multiple participants. The use of sockets for communication and threads for managing messages ensured efficient handling of multiple clients.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
