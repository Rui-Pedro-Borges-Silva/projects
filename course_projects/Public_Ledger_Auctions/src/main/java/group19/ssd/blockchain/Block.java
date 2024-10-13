package group19.ssd.blockchain;

import group19.ssd.blockchain.transactions.Transaction;
import group19.ssd.blockchain.utils.StringUtil;
import group19.ssd.miscellaneous.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {

    public String getHash() {
        return hash;
    }

    public String hashId; //identifier
    public String hash;
    public String publicKey;

    public String getPreviousHash() {
        return previousHash;
    }

    public String previousHash;
    public ArrayList<Transaction> data = new ArrayList<>();

    //private String merkleRoot;
    public int nonce = 0;
    public long timestamp;     //as number of milliseconds since 1/1/1970..

    //new block
    public Block(String hashId, ArrayList<Transaction> transactions, String lastBlockHash, String miner) {
        this.hashId = hashId;
        this.data = transactions;
        this.previousHash = lastBlockHash;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
        this.publicKey = miner;
    }

    // new block from another block received by other node
    public Block(String hashId, String hash, String lastBlockHash, ArrayList<Transaction> transactions, int nonce, long timestamp, String miner) {
        this.hashId = hashId;
        this.data = transactions;
        this.previousHash = lastBlockHash;
        this.timestamp = timestamp;
        this.hash = hash;
        this.publicKey = miner;
        this.nonce = nonce;
    }

    public Block(ArrayList<Transaction> data, String previousHash) {
        this.previousHash = previousHash;
        this.data = data;
        this.timestamp = new Date().getTime();
        //this.merkleRoot = calculateMerkleRoot(); // Calculate the Merkle Root
        this.hash = calculateHash();
    }

    // Calculate the block hash using Merkle Root
    public String calculateHash() {
        MerkleTree merkleTree = new MerkleTree(data);
        merkleTree.calculateMerkleRoot();
        return StringUtil.applySha256(
                this.hashId +
                previousHash +
                        Long.toString(timestamp) +
                        merkleTree.getRoot() +
                        Integer.toString(nonce)
        );
    }

    //create a hash with a certain difficulty
    public void mineBlock() {
        String target = new String(new char[Configuration.MINING_DIFFICULTY]).replace('\0', '0'); //Create a string with difficulty * "0"
        System.out.println("Starting to mine with difficulty target: " + target);
        while(!hash.substring( 0, Configuration.MINING_DIFFICULTY).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash + " with nonce: " + nonce);
    }

    // Return a copy of the data array to prevent external modification.
    public ArrayList<Transaction> getTransactions() {
        return data;
    }

    public boolean verifyBlock() {
        // Verify that the block's hash is correct
        return calculateHash().equals(hash);
    }

    public void printBlock(){
        System.out.println("\nHash: " + this.hash);
        System.out.println("Previous Hash: " + this.previousHash);
        System.out.println("Nonce: " + this.nonce);
        System.out.println("Transactions:");
        for(Transaction transaction : data){
            transaction.printTransaction();
            System.out.println();
        }
    }
}