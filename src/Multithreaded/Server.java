import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {
    public Consumer<Socket> getConsumer() {
        return (clientSocket) -> {
            try (
                    PrintWriter toSocket = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
            ) {
                toSocket.println("Hello from server " + clientSocket.getInetAddress());

                String clientMessage = fromClient.readLine();
                System.out.println("Message from client: " + clientMessage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        };
    }

    public static void main(String[] args) {
        int port = 8010;
        Server server = new Server();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread thread = new Thread(() -> server.getConsumer().accept(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
