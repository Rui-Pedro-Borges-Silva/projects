package group19.ssd.p2p;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import group19.ssd.blockchain.BCConverter;
import group19.ssd.blockchain.GRPCConverter;
import group19.ssd.blockchain.Block;
import group19.ssd.blockchain.Blockchain;
import group19.ssd.blockchain.transactions.Transaction;
import group19.ssd.p2p.grpc.*;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class PeerOperations {
    public PeerGrpc.PeerBlockingStub blockingStub;
    public int port;
    private final ManagedChannel channel;

    public PeerOperations(String ip, int port){
        this(ManagedChannelBuilder.forAddress(ip,port)
                .usePlaintext()
                .build());
        this.port = port;
    }

    PeerOperations(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = PeerGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException{
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public ArrayList<Node> findNodePeer(String id){
        try{
            try{
                KBucket_GRPC NodesFound = 
                    blockingStub.findNodes(FindNode.newBuilder()
                    .setId(KademliaClient.id)
                    .setIp(KademliaClient.ip)
                    .setPort(KademliaClient.port)
                    .setProof(KademliaClient.proof)
                    .setPubKey(KademliaClient.publicKey)
                    .setTargetId(id)
                    .build());  
                return NodeSerializable.GRPC_to_KBucket(NodesFound);
            } catch(StatusRuntimeException e){
                e.printStackTrace();
                return null;
            }
        }finally{
            try{
                PeerOperations.this.shutdown();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void sendBlock(Block block){
        try{
            group19.ssd.p2p.grpc.Block request = GRPCConverter.mkBlock(block);
            blockingStub.broadcastBlock(request);
        }finally{
            try{
                PeerOperations.this.shutdown();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void sendTransaction(Transaction transaction){
        try{
            group19.ssd.p2p.grpc.Transaction request = GRPCConverter.mkTransaction(transaction);
            blockingStub. broadcastTransaction(request);
        }finally{
            try{
                PeerOperations.this.shutdown();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public boolean ping(){
        try{
            try{
                Ping request = Ping.newBuilder()
                    .setId(KademliaClient.id)
                    .setIp(KademliaClient.ip)
                    .setPort(KademliaClient.port)
                    .setProof(KademliaClient.proof)
                    .setPubKey(KademliaClient.publicKey)
                    .build();
                Pong response = blockingStub.ping(request);
                if(response.getPong()){
                    Blockchain blockchain_received = BCConverter.mkBlockchain(response.getBlockchain());
                    if(blockchain_received.getChain().size() > KademliaClient.blockchain.getChain().size()){
                        KademliaClient.blockchain = blockchain_received;
                        KademliaClient.ledger.restartLedger();
                    }
                }
                return response.getPong();
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e){
                e.printStackTrace();
                return false;
            }
        } finally{
            try{
                PeerOperations.this.shutdown();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public Blockchain getBlockchain(){
        try{
            Ping request = Ping.newBuilder()
                .setId(KademliaClient.id)
                .setIp(KademliaClient.ip)
                .setPort(KademliaClient.port)
                .setProof(KademliaClient.proof)
                .setPubKey(KademliaClient.publicKey)
                .build();
            Pong response = blockingStub.ping(request);
            if(response.getPong()){
                Blockchain blockchain_received = null;
                try{
                    blockchain_received = BCConverter.mkBlockchain(response.getBlockchain());
                } catch (InvalidKeySpecException | NoSuchAlgorithmException e){
                    e.printStackTrace();
                }
                return blockchain_received; 
            } else {
                return null;
            }
        } finally{
            try{
                PeerOperations.this.shutdown();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}