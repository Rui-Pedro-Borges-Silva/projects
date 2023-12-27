import java.io.*;
import java.net.*;
import java.lang.Thread;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private static List<Player> clients = new ArrayList<>();
    private static int numberOfPlayers = 3;
    private static int numberOfGames = 0;
    private static ExecutorService executor = Executors.newFixedThreadPool(5); // max games = 5
    private static ReentrantLock lock = new ReentrantLock();

    public static void startServer(int port) {
        // Start server
        try (ServerSocketChannel channel = ServerSocketChannel.open()) {
            System.out.println("Server is listening on port " + port);

            // Bind channel to address to start listening to incoming connections
            channel.bind(new InetSocketAddress("localhost", port));

            // ServerSocketChannel as non-blocking
            channel.configureBlocking(false);

            // Selector used for multiplexing
            Selector selector = Selector.open();

            // Define selection key - token representing the registration of a channel with a selector
            int ops = channel.validOps();
            SelectionKey SelectionKey = channel.register(selector, ops, null);

            // Keep server running
            while (true) {
                processClient(channel, selector);
            }

        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void processClient(ServerSocketChannel channel, Selector selector) throws IOException {
        // Select a set of keys whose corresponding channels are ready for I/O operations
        selector.select();
        // Token representing the registration of a SelectableChannel with a Selector
        Set <SelectionKey> keys = selector.selectedKeys();
        Iterator <SelectionKey> i = keys.iterator();

        while (i.hasNext()) {
            SelectionKey key = i.next();
            // Key's channel is ready to accept a new socket connection
            if (key.isAcceptable()) {
                acceptClient(channel, selector);
            }
            // Key's channel is ready for reading
            else if (key.isReadable()) {
                authenticateUser(selector, key);
            }
            i.remove();
        }
    }

    public static void acceptClient(ServerSocketChannel channel, Selector selector) throws IOException {
        SocketChannel client = channel.accept();
        // Channel as non-blocking
        client.configureBlocking(false);
        // Register that client is reading this channel
        client.register(selector, SelectionKey.OP_READ);
    }

    public static void writeRegistrationData(String userData) {
        try {
            File file = new File("registration_data.txt");

            FileWriter fileWriter = new FileWriter(file,true);
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            bufferWriter.write('\n' + userData);
            bufferWriter.close();
            fileWriter.close();

        } catch(Exception e) {
            System.out.println("An error occurred writing to Registration Data.");
            e.printStackTrace();
        }
    }

    public static List<String> readRegistrationData() {
        List<String> registrationData = new ArrayList<>();

        try {
            File myObj = new File("registration_data.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                registrationData.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred reading Registration Data.");
            e.printStackTrace();
        }

        return registrationData;
    }

    public static void authenticateUser(Selector selector, SelectionKey key) throws IOException {
        List<String> registrationData = readRegistrationData();
        String loginOrRegister, username = "", password = "", data;
        boolean isAuthenticated = false;

        try {
            lock.lock();

            // Create new client
            Player client = new Player(selector, key, -1); // -1 signifies no game assigned

            while ((loginOrRegister = client.readFromClient()) == "");

            switch (loginOrRegister) {
                // login
                case "0":
                    while (!isAuthenticated) {
                        if ((data = client.readFromClient()) != "")  {
                            if (username == "") username = data;
                            else password = data;
                        }
                        if (password != "" && username != "") {
                            if (registrationData.contains(username.concat("," + password))) {
                                isAuthenticated = true;
                            }
                            else {
                                client.writeToClient("Wrong Credentials");
                                username = ""; password = "";
                            }
                        }
                    }
                    break;
                // register
                case "1":
                    while (!isAuthenticated) {
                        if ((data = client.readFromClient()) != "")  {
                            if (username == "") username = data;
                            else password = data;
                        }
                        if (password != "" && username != "") isAuthenticated = true;

                    }

                    // Write data to file
                    writeRegistrationData(username.concat("," + password));

                    break;

                default: return;
            }

            client.writeToClient("Waiting to start game...");
            client.setUserName(username);

            // Add client to queue
            if (!clients.contains(client)) {
                clients.add(client);
                System.out.println("New client connected: "+ client.getUserName());
            }

            // If the client was connected before
            else {
                Player existingPlayer = clients.stream().filter(p -> p.equals(client)).findFirst().orElse(null);

                existingPlayer.setKey(key);
                existingPlayer.setSelector(selector);

                System.out.println("Reconnecting with client: " + client.getUserName());
            }
        }

        finally {
            lock.unlock();
        }

        // Checks if its able to create a new game
        createNewGameInstance();
    }

    public static void createNewGameInstance() {
        // Get players with no game assigned
        List<Player> players = new ArrayList<>();
        clients.forEach((currPlayer) -> {
            try {
                if (currPlayer.getGameId() == -1 && currPlayer.readFromClient() != "Lost Connection") {
                    // add player
                    players.add(currPlayer);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // start game when there are 3 people in queue
        if (players.size() == 3) {
            // replace -1 with current game id
            players.forEach(player -> player.setGameId(numberOfGames));

            // Start new game in a Thread
            numberOfGames++;
            executor.execute((new Thread(new Game(numberOfGames, players))));
        }
    }
    public static void main(String[] args) {
        if (args.length < 1) return;
        int port = Integer.parseInt(args[0]);

        startServer(port);
    }
}
