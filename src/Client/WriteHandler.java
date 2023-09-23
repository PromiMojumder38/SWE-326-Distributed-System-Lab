package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class WriteHandler implements Runnable {
    private final Socket socket;

    public WriteHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.print("You: ");
                String message = scanner.nextLine();
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();

                if ("exit".equalsIgnoreCase(message) || "quit".equalsIgnoreCase(message)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
