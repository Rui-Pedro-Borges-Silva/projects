import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class Player {
    private String userName;
    private int gameId;
    private Selector selector;
    private SelectionKey key;

    public Player(Selector selector, SelectionKey key, int gameId) {
        this.selector = selector;
        this.key = key;
        this.gameId = gameId;
    }

    public String readFromClient() throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        key.interestOps(SelectionKey.OP_READ);

        // Buffer to read data
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytes_read;
        if ((bytes_read = client.read(buffer)) > 0);
        else if (bytes_read == -1) return "Lost Connection";

        String data = new String(buffer.array()).trim();

        if (data.length() > 0) {
            return data;
        }
        return "";
    }

    public void writeToClient(String message) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        client.register(selector, SelectionKey.OP_WRITE);

        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        client.write(buffer);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(getUserName(), player.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName());
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public SelectionKey getKey() {
        return key;
    }

    public void setKey(SelectionKey key) {
        this.key = key;
    }
}
