package group19.ssd.p2p;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Kademlia {
    public static boolean pingNode(Node oldestNode){
        return new PeerOperations(oldestNode.ip, oldestNode.port).ping();
    }

    public static boolean checkNodeValidity(String id, String ip, int port, int proof, String publicKey){
        return KademliaClient.calculateNeighbourHash(ip, port, proof, publicKey).equals(id);
    }

    public static void findNode(String id){
        ArrayList<Node> closestNodes = KademliaClient.kbucket.getNCloseNodes(id, KademliaClient.kbucket.identifiedLast);
        ArrayList<Node> foundedNodes = new ArrayList<>();
        ArrayList<Node> lastfounded = null;

        while(true){
            for(Node user : closestNodes){
                ArrayList<Node> n = new PeerOperations(user.id, user.port).findNodePeer(id);
                
                foundedNodes.addAll(n);
            }

            removeRepeated(foundedNodes);

            for(Node node : foundedNodes){
                if(node.id.equals(id)){
                    foundedNodes.remove(node);
                    break;
                }
            }

            removeRepeated(foundedNodes);
            KademliaClient.kbucket.checkNodeList(foundedNodes);

            if(lastfounded == null){
                lastfounded = new ArrayList<>();
            } else if(lastfounded.containsAll(lastfounded)){
                return;
            }

            lastfounded.clear();
            lastfounded.addAll(foundedNodes);

            closestNodes = KademliaClient.kbucket.getNeighboursByDistance(id, foundedNodes);
            foundedNodes.clear();
        }
    }

    private static void removeRepeated(ArrayList<Node> users){
        Set<Node> user = new HashSet<>(users);

        user.clear();
        user.addAll(users);
    }

    //the distance between two nodes
    public static String xorDistance(String hashNode1, String hashNode2){
        hashNode1 = new BigInteger(hashNode1, 16).toString(2);
        hashNode2 = new BigInteger(hashNode2, 16).toString(2);

        int sizeNode1 = hashNode1.length();
        int sizeNode2 = hashNode2.length();
        StringBuilder xorResult = new StringBuilder();

        boolean isSizeDifferent = sizeNode1 != sizeNode2;
        int maxSize = Math.max(sizeNode1,sizeNode2);

        if(isSizeDifferent){
            if(sizeNode1 > sizeNode2){
                hashNode2 = addPadding(sizeNode1-sizeNode2) + hashNode2;
            } else {
                hashNode1 = addPadding(sizeNode2-sizeNode1) + hashNode1;
            }
        }

        for(int i = maxSize-1; i >= 0; i--){
            String temp = hashNode1.charAt(i) != hashNode2.charAt(i) ? "1" : "0";
            xorResult.insert(0, temp);
        }

        return xorResult.toString();
    }

    //comparing distance between nodes
    public static int compareDistance(String dist1, String dist2){
        int n;
        String padding;

        if(dist2.length() > dist1.length()){
            n = dist2.length() - dist1.length();
            padding = addPadding(n);
            dist1 = padding + dist1;
        } else {
            n = dist1.length() - dist2.length();
            padding = addPadding(n);
            dist2 = padding + dist2;
        }

        for(int i = 0; i < dist1.length(); i++){
            if(dist1.length() > dist2.length()){
                return 1;
            } else if(dist1.length() < dist2.length()){
                return -1;
            }
        }

        return 0;
    }

    public static String addPadding(int n){
        return "0".repeat(Math.max(0, n));
    }
}
