package fr.utc.sr03.ServerPackage;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Class representing a thread that listens for incoming messages from a client and broadcasts them to other clients.
 */
class MessageReceptor extends Thread {

    /**
     * Socket associated with the client.
     */
    private final Socket clientSocket;

    /**
     * Flag indicating whether the thread is running or not.
     */
    private boolean isRunning = true;

    /**
     * Username of the client.
     */
    private String userName;

    /**
     * Message received from the client.
     */
    private String message;

    /**
     * Constructor for the MessageReceptor class.
     *
     * @param clientSocket the socket associated with the client
     */
    public MessageReceptor(Socket clientSocket) {
        this.clientSocket = clientSocket;
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
     * Getter for the username of the client.
     *
     * @return the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter for the username of the client.
     *
     * @param userName the new username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter for the client's socket.
     *
     * @return the client's socket
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * Getter for the message received from the client.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for the message received from the client.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Main method for the MessageReceptor thread.
     * Listens for incoming messages from the client, adds the client's username to the list if not already present, and broadcasts the messages to other clients.
     */
    @Override
    public void run() {
        boolean isAdded = false;
        int length;
        while (isRunning()) {
            try {
                byte[] buffer = new byte[1024];
                length = clientSocket.getInputStream().read(buffer);
                // If the first message, send the username to all clients
                if (!isAdded) {
                    setUserName(new String(buffer, 0, length));
                    isAdded = Server.addPseudo(getUserName(), clientSocket);
                    if (isAdded) {
                        System.out.println("Nom du nouveau client en ligne : " + getUserName());
                        Server.broadcastMessage((getUserName() + " a rejoint la conversation\n"));
                        Server.broadcastMessage("------------------------\n");
                    } else {
                        clientSocket.getOutputStream().write("Le pseudo est déjà utilisé. Veuillez choisir un autre pseudo :".getBytes());
                    }
                    continue;
                }

                // If another message
                if (length > 0) {
                    setMessage(new String(buffer, 0, length));
                    System.out.println("Message reçu de " + getUserName() + " : " + getMessage());
                    if (getMessage().equalsIgnoreCase("exit")) {
                        System.out.println("Client déconnecté : " + clientSocket.getInetAddress().getHostAddress());
                        Server.broadcastMessage("L'utilisateur " + getUserName() + " a quitté la conversation.");
                        Server.removeClient(clientSocket, getUserName());
                        this.stopThread();
                        break;
                    } else {
                        Server.broadcastMessage(getUserName() + " a dit : " + getMessage());
                    }
                }
                // If the client exit without sending the exit message
            } catch (SocketException s) {
                System.out.println("Connexion rénitialisé par le client");
                try {
                    Server.broadcastMessage("Le client " + getUserName() + " a été déconnecté", this.clientSocket);
                    Server.removeClient(clientSocket, getUserName());
                    clientSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the MessageReceptor thread by setting the isRunning flag to false and waiting for the thread to finish its execution.
     */
    public void stopThread() {
        setRunning(false);
        try {
            this.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
