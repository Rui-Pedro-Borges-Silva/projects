package group19.ssd.p2p;

import java.util.ArrayList;

import group19.ssd.miscellaneous.Configuration;

public class KBucket {
    public ArrayList<Node> identifiedLast = new ArrayList<>();

    public int identifiedSize(){
        return identifiedLast.size();
    }

    public void removeNode(Node node){
        identifiedLast.remove(node);
    }

    public Node oldestNode(){
        return identifiedLast.get(0);
    }

    public void sendToLast(Node node){
        identifiedLast.remove(node);
        identifiedLast.add(node);
    }

    public void addNode(Node node){
        identifiedLast.add(node);
    }
    
    public Node getNode(String nodeId){
        for (Node node : this.identifiedLast){
            if (node.id.equals(nodeId))
                return node;
        }
        return null;
    }

    public ArrayList<Node> getNCloseNodes(String id, ArrayList<Node> nodes){
        if (nodes.size() > Configuration.ALPHA){
            return (ArrayList<Node>) getNeighboursByDistance(id, nodes).subList(0, Configuration.ALPHA - 1);
        } else {
            return getNeighboursByDistance(id, nodes);
        }
    }

    public ArrayList<Node> getNeighboursByDistance(String id, ArrayList<Node> list){
        @SuppressWarnings("unchecked")
        ArrayList<Node> result = (ArrayList<Node>) list.clone();

        if (list.size() == 1){
            return list;
        }

        result.sort((r1, r2) ->{
            String xorDist = Kademlia.xorDistance(r1.id, id);
            String xorDist2 = Kademlia.xorDistance(r2.id, id);

            return Kademlia.compareDistance(xorDist, xorDist2);
        });

        return result;
    }

    public void checkNodeList(ArrayList<Node> list){
        for(Node node : list){
            checkNode(node, -1, "");
        }
    }

    public boolean checkNode(Node node, int proof, String publicKey){
        if(node.id.equals(KademliaClient.id)){
            return true;
        }

        if(proof != -1){
            if (!Kademlia.checkNodeValidity(node.id, node.ip, node.port, proof, publicKey))
                return false;
        }

        if(identifiedLast.contains(node)){
            sendToLast(node);
            return true;
        }

        if(identifiedSize() < Configuration.MAX_NODES_BUCKET){
            addNode(node);
        } else {
            if(Kademlia.pingNode(oldestNode())){
                sendToLast(oldestNode());
            } else {
                removeNode(oldestNode());
                addNode(node);
            }
        }

        return true;
    }

    public ArrayList<Node> getCloneNodesList(){
        return (ArrayList) identifiedLast.clone();
    }

    public void print() {
        System.out.println("Bucket:");
        for (Node n : identifiedLast) {
            System.out.print(n.toString() + " ");
        }
    }
}
