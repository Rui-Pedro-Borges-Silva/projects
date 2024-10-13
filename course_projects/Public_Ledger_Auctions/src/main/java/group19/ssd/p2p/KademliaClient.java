package group19.ssd.p2p;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import group19.ssd.blockchain.Block;
import group19.ssd.blockchain.Blockchain;
import group19.ssd.blockchain.transactions.Transaction;
import group19.ssd.blockchain.transactions.Wallet;
import group19.ssd.miscellaneous.Configuration;
import group19.ssd.miscellaneous.Miscellaneous;

public class KademliaClient {
    public static String id;
    public static String ip;
    public static int port;
    public static int proof;
    public static String publicKey;
    public static KBucket kbucket = new KBucket();
    public static Wallet wallet = new Wallet();
    public static Blockchain blockchain;
    public static Ledger ledger;
    private static boolean alreadyRunningMineBlockThread = false;
    private static final MineBlockThread mineBlockThread = new MineBlockThread();
    public static final KeepAliveThread keepAliveThread = new KeepAliveThread();

    public String getHash() {
        return id;
    }

    public KademliaClient(){

    }

    public void setup(int node_port, String node_ip) {
        KademliaClient.proof = 0;
        KademliaClient.port = node_port;
        KademliaClient.ip = node_ip;
        KademliaClient.publicKey = String.valueOf(KademliaClient.wallet.getPublicKey());
        KademliaClient.ledger = new Ledger();
        KademliaClient.blockchain = new Blockchain();

        id = getNodeId();

        System.out.println(Configuration.knownNode);

        if (Configuration.knownNode.isEmpty()) {
            Configuration.knownNode = KademliaClient.id;
        } else {
            KademliaClient.kbucket.addNode(new Node(Configuration.knownNode, node_ip, 8080));
            Kademlia.pingNode(new Node(Configuration.knownNode, node_ip, 8080));
        }
    }

    private static String getNodeId(){
        return calculateHash(KademliaClient.ip, KademliaClient.proof,KademliaClient.port,KademliaClient.publicKey);
    }

    private static String calculateHash(String ip, int proof,int port, String publicKey){
        return Miscellaneous.applyEncryption(ip + port + proof + publicKey);
    }

    public static String calculateNeighbourHash(String ip, int port, int proof, String publicKey){
        return Miscellaneous.applyEncryption(ip + port + proof + publicKey);
    }

    public static void evaluateTrust(){
        ArrayList<Node> bucket = kbucket.getCloneNodesList();
        for (Node node : bucket) {
            Blockchain newBlockchain = null;

            try {
                newBlockchain = new PeerOperations(node.ip, node.port).getBlockchain();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (newBlockchain != null) {
                if (newBlockchain.isChainValid())
                    KademliaClient.kbucket.getNode(node.id).addSuccessfullInterations();
                else {
                    KademliaClient.kbucket.getNode(node.id).addNonSuccessfullInteractions();
                }
            }
        }
    }

    public static void shareBlock(Block block, String sender){
        ArrayList<Node> destinations = KademliaClient.kbucket.getCloneNodesList();

        for (Node destination : destinations) {
            if (!destination.id.equals(sender))
                try {
                    System.out.println("Send Block to " + destination.ip + ":" + destination.port);
                    new PeerOperations(destination.ip, destination.port).sendBlock(block);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    public static void shareTransaction(Transaction transaction, String sender){
        ArrayList<Node> destinations = KademliaClient.kbucket.getCloneNodesList();

        try {
            transaction.signTransaction(KademliaClient.wallet);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException |
                 SignatureException | InvalidKeySpecException | InvalidKeyException e) {
            e.printStackTrace();
        }

        Blockchain.pendingList.add(transaction);

        for (Node destination : destinations) {
            System.out.println(destination.ip + ":" + destination.port);
            if (!(destination.id.equals(sender)))
                try {
                    new PeerOperations(destination.ip, destination.port).sendTransaction(transaction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

     public static void startMining() {
         if (!alreadyRunningMineBlockThread) {
             System.out.println("Started Mining");
             alreadyRunningMineBlockThread = true;
             KademliaClient.mineBlockThread.start();
         } else {
             System.out.println("Already Mining");
         }
     }

     public static void startPinging(){
         KademliaClient.keepAliveThread.start();
     }

     private static class MineBlockThread extends Thread{
         public MineBlockThread(){}

         @Override
         public void run() {

             while (true) {
                 try {

                     TimeUnit.MICROSECONDS.sleep(500);

                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }

                 if (!KademliaClient.blockchain.getPendingList().isEmpty()) {

                     String blockHashId = Miscellaneous.applyEncryption(String.valueOf(new Date().getTime()));
                     System.out.println("mining: " + blockHashId);

                     KademliaClient.blockchain.minePendingTransaction(KademliaClient.wallet);
                     KademliaClient.ledger.updateLedger(KademliaClient.blockchain.getLatestBlock());

                     KademliaClient.shareBlock(KademliaClient.blockchain.getLatestBlock(), KademliaClient.id);
                     KademliaClient.blockchain.printBlockChain();
                 }
             }
         }
     }

     private static class KeepAliveThread extends Thread{
         public KeepAliveThread(){}

         @Override
         public void run() {

             while (true) {
                 try {

                     TimeUnit.MILLISECONDS.sleep(20000);

                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }

                 KademliaClient.evaluateTrust();

                 for (int i = 0; i < KademliaClient.kbucket.identifiedSize(); i++) {
                     if (!new PeerOperations(KademliaClient.kbucket.identifiedLast.get(i).ip, KademliaClient.kbucket.identifiedLast.get(i).port).ping()) {
                         KademliaClient.kbucket.identifiedLast.remove(i);
                         i--;
                     }
                 }

             }
         }
     }
}
