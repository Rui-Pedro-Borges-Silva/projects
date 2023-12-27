import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    private static Scanner scanner = new Scanner(System.in);
    private static SocketChannel clientChannel;

    public static void createChannel(String hostname, int port) {
        try {
            // Connect to the server
            clientChannel = SocketChannel.open();
            clientChannel.configureBlocking(false);
            clientChannel.connect(new InetSocketAddress(hostname, port));

            // Wait for the connection to be established
            while (!clientChannel.finishConnect()) {}

            handleAuthentication();
            handleGame();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    public static String readFromServer() throws IOException {
        ByteBuffer responseBuffer = ByteBuffer.allocate(1024);
        clientChannel.read(responseBuffer);
        String data = new String(responseBuffer.array()).trim();

        if (data.length() > 0)
            return data;

        return "";
    }

    public static void writeToServer(String messageToServer) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(messageToServer.getBytes());
        clientChannel.write(buffer);
    }

    public static void handleAuthentication() throws IOException {
        // Registration menu
        String messageToServer = "", messageFromServer = "";

        while(true) {
            // Check if its valid input
            System.out.println("0. Login | 1. Register ");
            messageToServer = scanner.nextLine();
            if (messageToServer.matches("[0-1]")) break;
            else System.out.println("Wrong input!");
        }

        // Send the message to the server
        writeToServer(messageToServer);

        boolean isAuthenticated = false;

        while (!isAuthenticated) {
            // Send username to server
            System.out.println("Enter username");
            messageToServer = scanner.nextLine();
            writeToServer(messageToServer);

            // Send password to server
            System.out.println("Enter password");
            messageToServer = scanner.nextLine();
            writeToServer(messageToServer);

            // Wait for response from the server
            while (true) {
                if ((messageFromServer = readFromServer()) != "") {
                    System.out.println(messageFromServer);
                    if (!messageFromServer.equals("Wrong Credentials")) isAuthenticated = true;
                    break;
                }
            }
        }
    }

    public static void handleGame() throws IOException {
        boolean gameRunning = true;
        String messageToServer, messageFromServer;

        // Game running
        while (gameRunning) {
            if ((messageFromServer = readFromServer()) != "") {
                System.out.println(messageFromServer);

                while (true) {
                    if ((messageFromServer = readFromServer()) != "") {
                        System.out.println(messageFromServer);

                        if (messageFromServer.equals("Game Ended."))
                            gameRunning = false;

                        break;
                    }
                }

                if (gameRunning && (messageToServer = scanner.nextLine()) != null)
                    writeToServer(messageToServer);
            }
        }

        // Print winner
        while (true) {
            if ((messageFromServer = readFromServer()) != "") {
                System.out.println(messageFromServer);
                break;
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) return;

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        createChannel(hostname, port);
    }
}