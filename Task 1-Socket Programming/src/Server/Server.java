package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int MAX_THREADS = 10;
    private static final List<Socket> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        System.out.println("Server Started");
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (true) {
                Socket client = serverSocket.accept();
                clients.add(client);

                threadPool.execute(() -> {
                    new ServerReadHandler(client, clients).run();
                    new ServerWriteHandler(client, clients).run();
                });
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            threadPool.shutdownNow();
        }
    }
}
