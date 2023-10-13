package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ReadHandler implements Runnable {
    private final Socket socket;

    public ReadHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        DataInputStream dataInputStream = null;

        try {
            dataInputStream = new DataInputStream(socket.getInputStream());

            String message;
            while (true) {
                message = dataInputStream.readUTF();
                System.out.println("Server: " + message);
            }
        } catch (IOException e) {
            handleReadError("Connection to the server is lost.");
        } finally {
            closeResources(dataInputStream);
        }
    }

    private void handleReadError(String errorMessage) {
        System.err.println(errorMessage);
    }

    private void closeResources(DataInputStream dataInputStream) {
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
