package fr.utc.sr03.ClientPackage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class representing a handler for outgoing messages in the chat client.
 */
class OutgoingMessageHandler implements Runnable {

    /**
     * Socket associated with the chat client.
     */
    private final Socket socket;

    /**
     * Flag indicating whether the thread is running or not.
     */
    private boolean isRunning;

    /**
     * IncomingMessageHandler associated with the chat client.
     */
    private final IncomingMessageHandler incomingMessageHandler;

    /**
     * Constructor for the OutgoingMessageHandler.
     *
     * @param socket                 the socket associated with the chat client
     * @param incomingMessageHandler the incoming message handler associated with the chat client
     */
    public OutgoingMessageHandler(Socket socket, IncomingMessageHandler incomingMessageHandler) {
        this.socket = socket;
        this.incomingMessageHandler = incomingMessageHandler;
    }

    /**
     * Getter for the socket associated with the chat client.
     *
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Getter for the isRunning flag.
     *
     * @return true if the thread is running, false otherwise
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Setter for the isRunning flag.
     *
     * @param running the new value for the isRunning flag
     */
    public void setRunning(boolean running) {
        isRunning = running;
    }

    /**
     * Getter for the incoming message handler associated with the chat client.
     *
     * @return the incoming message handler
     */
    public IncomingMessageHandler getIncomingMessageHandler() {
        return incomingMessageHandler;
    }

    /**
     * Main method for the OutgoingMessageHandler thread.
     * Reads messages from the console and sends them to the server.
     * Stops the thread if the "exit" command is received.
     */
    @Override
    public void run() {
        OutputStream out;
        String message;
        try {
            out = getSocket().getOutputStream();
            while (isRunning()) {
                Scanner sc = new Scanner(System.in);
                message = sc.nextLine();
                out.write(message.getBytes());
                if (message.equalsIgnoreCase("exit")) {
                    this.stopThread();
                    getIncomingMessageHandler().stopThread();
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the OutgoingMessageHandler thread by setting the isRunning flag to false and shutting down the output stream.
     */
    public void stopThread() {
        setRunning(false);
        try {
            socket.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
