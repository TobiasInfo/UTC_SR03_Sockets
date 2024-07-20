package fr.utc.sr03.ServerPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a chat server that accepts client connections, manages usernames, and broadcasts messages.
 */
public class Server {

    /**
     * Port on which the server listens for incoming connections.
     */
    private static final int port = 8080;

    /**
     * List of clients connected to the server.
     */
    private static final List<Socket> clients = new ArrayList<>();

    /**
     * List of usernames of connected clients.
     */
    private static final List<String> pseudo = new ArrayList<>();

    /**
     * Adds a username and its associated socket to their respective lists if the username is not already taken.
     *
     * @param userName the username to add
     * @param socket   the socket associated with the username
     * @return 1 if the username was successfully added, 0 otherwise
     */
    public static boolean addPseudo(String userName, Socket socket) {
        if (pseudo.contains(userName)) {
            return false;
        } else {
            clients.add(socket);
            pseudo.add(userName);
            return true;
        }
    }

    /**
     * Broadcasts a message to all connected clients.
     *
     * @param message the message to broadcast
     * @throws IOException in case of input/output error
     */
    public static void broadcastMessage(String message) throws IOException {
        for (Socket client : clients) {
            client.getOutputStream().write(message.getBytes());
        }
    }

    /**
     * Broadcasts a message to all connected clients exept for the disconnected client. Use only to broadcast a disconnected message for the other clients
     *
     * @param message      the message to broadcast
     * @param clientSocket the socket object link to the client disconnected
     * @throws IOException in case of input/output error
     */
    public static void broadcastMessage(String message, Socket clientSocket) throws IOException {
        for (Socket client : clients) {
            if (clientSocket != client) {
                client.getOutputStream().write(message.getBytes());
            }
        }
    }

    /**
     * Removes a client and its associated username from their respective lists and stops the corresponding thread.
     *
     * @param clientSocket the socket of the client to remove
     * @param userName     the username of the client to remove
     */
    public static void removeClient(Socket clientSocket, String userName) {
        clients.remove(clientSocket);
        pseudo.remove(userName);
        // Find the thread corresponding to the clientSocket and stop it
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread instanceof MessageReceptor && ((MessageReceptor) thread).getClientSocket() == clientSocket) {
                ((MessageReceptor) thread).stopThread();
                break;
            }
        }
    }

    /**
     * Main method that starts the server and listens for incoming connections.
     *
     * @param args command line arguments (not used in this case)
     * @throws IOException in case of input/output error
     */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Serveur démarré sur le port " + port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Nouveau client connecté : " + clientSocket.getInetAddress().getHostAddress());
            new MessageReceptor(clientSocket).start();
        }
    }
}
