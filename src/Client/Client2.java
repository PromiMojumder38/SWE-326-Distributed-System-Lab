package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client2{
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            System.out.println("Connected to Server");

            ExecutorService threadPool = Executors.newFixedThreadPool(2);

            threadPool.execute(new ReadHandler(socket));

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("You: ");
                String message = scanner.nextLine();
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();

                if ("exit".equalsIgnoreCase(message) || "quit".equalsIgnoreCase(message)) {
                    break;
                }
            }

            threadPool.shutdown();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
