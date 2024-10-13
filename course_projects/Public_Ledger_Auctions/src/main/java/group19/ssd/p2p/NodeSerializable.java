package group19.ssd.p2p;

import group19.ssd.p2p.grpc.KBucket_GRPC;
import group19.ssd.p2p.grpc.Node_GRPC;

import java.util.ArrayList;

public class NodeSerializable {
    public static ArrayList<Node> GRPC_to_KBucket(KBucket_GRPC grpc) {
        ArrayList<Node_GRPC> List_of_Nodes = new ArrayList<>(grpc.getKbucketList());
        ArrayList<Node> nodes = new ArrayList<>();
        for(Node_GRPC node: List_of_Nodes) {
            nodes.add(GRPC_To_Node(node));
        }
        return nodes;
    }

    public static KBucket_GRPC KBucket_to_GRPC(ArrayList<Node> List_of_nodes){
        ArrayList<Node_GRPC> List_of_GRPCNodes = new ArrayList<>();
        for(Node node : List_of_nodes){
            List_of_GRPCNodes.add(Node_to_GRPC(node));
        }
        return KBucket_GRPC.newBuilder().addAllKbucket(List_of_GRPCNodes).build();
    }


    public static Node GRPC_To_Node(Node_GRPC node) {
        return new Node(node.getId(), node.getIp(), node.getPort());
    }

    public static Node_GRPC Node_to_GRPC(Node node) {
        return Node_GRPC.newBuilder().setId(node.id).setIp(node.ip).setPort(node.port).build();
    }
}
