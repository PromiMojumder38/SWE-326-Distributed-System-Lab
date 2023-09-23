package Server;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerReadHandler implements Runnable {
    private final Socket client;
    private final List<Socket> clients;
    private static final List<ServerReadHandler> handlerList = new CopyOnWriteArrayList<>();

    public ServerReadHandler(Socket client, List<Socket> clients) {
        this.client = client;
        this.clients = clients;
        handlerList.add(this);
    }

    @Override
    public void run() {
        try (DataInputStream dataInputStream = new DataInputStream(client.getInputStream())) {
            String message;
            while (true) {
                message = dataInputStream.readUTF();
                System.out.println("Client says: " + message);

                broadcastMessage("Client says: " + message, client);
            }
        } catch (IOException e) {
            handleReadError(e);
        } finally {
            removeHandlerFromList();
            closeClientSocket();
        }
    }

    private void handleReadError(IOException e) {
        System.err.println("Error reading client input: " + e.getMessage());
    }

    private void broadcastMessage(String message, Socket sender) {
        for (ServerReadHandler handler : handlerList) {
            if (handler.client != sender) {
                try {
                    DataOutputStream dataOutputStream = new DataOutputStream(handler.client.getOutputStream());
                    dataOutputStream.writeUTF(message);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    handleBroadcastError(e);
                }
            }
        }
    }

    private void handleBroadcastError(IOException e) {
        System.err.println("Error broadcasting message: " + e.getMessage());
    }

    private void removeHandlerFromList() {
        handlerList.remove(this);
    }

    private void closeClientSocket() {
        try {
            client.close();
        } catch (IOException e) {
            System.err.println("Error closing client socket: " + e.getMessage());
        }
    }
}
