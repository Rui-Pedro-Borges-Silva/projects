import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.io.*;
import java.lang.Thread;

public class Game extends Thread {
    private int gameId;
    private List<Player> players;
    private final Map<String, String> capitals;
    private final Map<Player, Integer> scores;
    private final CountDownLatch latch;

    public Game(int gameId, List<Player> players) {
        this.gameId = gameId;
        this.players = players;

        // Initialize the capitals
        this.capitals = new HashMap<>();
        capitals.put("Portugal", "Lisbon");
        capitals.put("France", "Paris");
        capitals.put("Spain", "Madrid");
        capitals.put("Germany", "Berlin");
        capitals.put("Italy", "Rome");
        capitals.put("Switzerland", "Bern");
        capitals.put("Sweden", "Stockholm");
        capitals.put("Singapore", "Singapore");
        capitals.put("Slovenia", "Ljubljana");

        // Initialize the scores
        this.scores = new HashMap<>();
        for(Player player : players) {
            this.scores.put(player, 0);
        }

        this.latch = new CountDownLatch(players.size());
    }

    private static final int NUM_QUESTIONS = 3;

    public void startGame() {
        System.out.println("Starting game with " + players.size() + " players");

        for (Player player: players) {
            new Thread(() -> {
                // Send game starting message to player
                try {
                    player.writeToClient("Game Started!");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                int numQuestions = 0;
                while (numQuestions < NUM_QUESTIONS) {
                    try {
                        // Randomly select a country
                        String country = (String) capitals.keySet().toArray()[new Random().nextInt(capitals.size())];
                        // Ask the player for the capital of the selected country
                        player.writeToClient("What is the capital of " + country + "?");

                        String messageReceived;

                        while (true) {
                            messageReceived = player.readFromClient();
                            if (messageReceived != "") break;
                        }

                        // If the answer is correct, increase the player's score
                        if (messageReceived.equalsIgnoreCase(capitals.get(country))) {
                            scores.put(player, scores.get(player) + 1);
                            player.writeToClient("Correct!");
                        } else {
                            player.writeToClient("Wrong answer. The correct answer is " + capitals.get(country));
                        }

                        numQuestions++;
                    } catch (IOException e) {
                        System.out.println("Client " + player.getUserName() + " disconnected");
                        break;
                    }

                    if (numQuestions == NUM_QUESTIONS) {
                        latch.countDown();
                    }
                }
                // End of game
                try {
                    player.writeToClient("Game Ended.");
                } catch (IOException e) {}

                // Wait until all players have finished before determining the winner
                try {
                    latch.await();
                } catch (InterruptedException e) {}

                // Determine the player with the highest score
                if(latch.getCount() == 0) {
                    Player winner = scores.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .get()
                            .getKey();
                    try {
                        player.writeToClient("The winner is " + winner.getUserName());
                    } catch (IOException e) {}

                    System.out.println("Game " + this.gameId + " finished, the winner is" + winner.getUserName());
                }
            }).start();
        }
    }

    @Override
    public void run() {
        this.startGame();
    }
}
