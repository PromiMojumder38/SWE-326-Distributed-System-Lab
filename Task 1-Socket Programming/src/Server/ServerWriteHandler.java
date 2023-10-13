package Server;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerWriteHandler implements Runnable {
    private final Socket client;
    private final List<Socket> clients;

    public ServerWriteHandler(Socket client, List<Socket> clients) {
        this.client = client;
        this.clients = clients;
    }

    @Override
    public void run() {
        try (DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream())) {
            sendWelcomeMessage(dataOutputStream);

            while (true) {
                Thread.sleep(1000); // Sleep for 1 second (adjust as needed)
            }
        } catch (IOException | InterruptedException e) {
            handleWriteError(e);
        } finally {
            closeClientSocket();
        }
    }

    private void sendWelcomeMessage(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeUTF("Welcome to the chat!");
        dataOutputStream.flush();
    }

    private void handleWriteError(Exception e) {
        System.err.println("Error writing to client: " + e.getMessage());
    }

    private void closeClientSocket() {
        try {
            client.close();
        } catch (IOException e) {
            System.err.println("Error closing client socket: " + e.getMessage());
        }
    }
}
