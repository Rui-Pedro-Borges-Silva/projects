package group19.ssd.p2p;

public class Node {
    String id;
    String ip;
    int port;
    int interactions;
    int interactionsSuccessfull;

    public Node(String id, String ip, int port){
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.interactions = 0;
        this.interactionsSuccessfull = 0;
    }

    public void addSuccessfullInterations(){
        this.interactions++;
        this.interactionsSuccessfull++;
    }

    public void addNonSuccessfullInteractions(){
        this.interactions++;
    }

    @Override
    public String toString() {
        return "{\n id: " + id + "\n ip: " + ip + "\n port: " + port + "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            return id.equals(((Node) o).id) && ip.equals(((Node) o).ip) && port == ((Node) o).port;
        }
        return false;
    }
}
