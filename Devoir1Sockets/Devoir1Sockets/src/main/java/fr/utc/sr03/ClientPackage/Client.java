package fr.utc.sr03.ClientPackage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class representing a chat client that connects to a server, sends a username, and handles incoming and outgoing messages.
 */
public class Client extends Thread {

    /**
     * The server address to which the client connects.
     */
    private static final String serverAdress = "localhost";

    /**
     * The server port to which the client connects.
     */
    private static final int serverPort = 8080;

    /**
     * Main method that runs the chat client.
     *
     * @param args command line arguments (not used in this case)
     * @throws IOException in case of input/output error
     */
    public static void main(String[] args) throws IOException {
        // Create the socket and connect to the server
        Socket socket = new Socket(serverAdress, serverPort);
        System.out.println("Connecté au serveur sur " + serverAdress + ":" + serverPort);

        // Initialize the writing and reading variables of the socket
        OutputStream out = socket.getOutputStream();

        // Get the userName
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez votre pseudonyme : ");
        String userName = scanner.nextLine();

        // Send the userName to the server
        out.write(userName.getBytes());

        // Create and start the incoming and outgoing message handlers
        IncomingMessageHandler inMesHandl = new IncomingMessageHandler(socket);
        inMesHandl.setRunning(true); // Set the isRunning flag to true for IncomingMessageHandler
        new Thread(inMesHandl).start();

        OutgoingMessageHandler outMesHandl = new OutgoingMessageHandler(socket, inMesHandl);
        outMesHandl.setRunning(true); // Set the isRunning flag to true for OutgoingMessageHandler
        new Thread(outMesHandl).start();
    }
}
