package fr.utc.sr03.ClientPackage;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Class representing a handler for incoming messages in the chat client.
 */
class IncomingMessageHandler implements Runnable {

    /**
     * Socket associated with the chat client.
     */
    private final Socket socket;

    /**
     * Flag indicating whether the thread is running or not.
     */
    private boolean isRunning;

    /**
     * Constructor for the IncomingMessageHandler.
     *
     * @param socket the socket associated with the chat client
     */
    public IncomingMessageHandler(Socket socket) {
        this.socket = socket;
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
     * Main method for the IncomingMessageHandler thread.
     * Reads messages from the server and prints them to the console.
     * Stops the thread if the socket is closed or the "exit" command is received.
     */
    @Override
    public void run() {
        InputStream in;
        String message;
        byte[] buffer = new byte[1024];
        int length;
        while (isRunning() && !getSocket().isClosed()) {
            try {
                in = getSocket().getInputStream();
                length = in.read(buffer);
                if (length > 0) {
                    message = new String(buffer, 0, length);
                    System.out.println(message);
                }
            } catch (SocketException s) {
                System.out.println("Le serveur a un problème. Déconnexion ...");
                try {
                    getSocket().close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.exit(1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the IncomingMessageHandler thread by setting the isRunning flag to false and shutting down the input stream.
     */
    public void stopThread() {
        setRunning(false);
        try {
            getSocket().shutdownInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
