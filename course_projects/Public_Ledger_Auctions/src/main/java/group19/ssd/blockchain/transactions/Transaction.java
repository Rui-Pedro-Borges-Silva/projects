package group19.ssd.blockchain.transactions;

import group19.ssd.blockchain.utils.StringUtil;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Date;

public class Transaction {


    public Wallet source;
    public Wallet destination;
    public String sender;
    public String receiver;
    public int amount;
    public byte[] signature; // This will store our signature;
    public String hash; // This will store the hash of the transaction
    public String misc = ""; // additional info
    public long timestamp;
    private TransactionType type; // Include transaction type

    public enum TransactionType {
        AUCTION_START,
        AUCTION_END,
        BID_START,
        BID_END
    }

    public Transaction(Wallet source, Wallet destination, int amount) {
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        validateTransaction();
        this.misc = misc;
    }

    public Transaction(Wallet source, TransactionType type) {
        this.source = source;
        this.type = type;
        this.timestamp = new Date().getTime();
    }

    public Transaction(String sender, String receiver, int amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        validateTransaction();
        this.misc = misc;
    }

    public Transaction(String sender, String receiver, byte[] byteArray, long timestamp, int amount, String misc) {
        this.sender = sender;
        this.receiver = receiver;
        this.signature = byteArray;
        this.timestamp = timestamp;
        this.amount = amount;
        this.misc = misc;
    }

    public void validateTransaction() {
        this.timestamp = new Date().getTime();
        this.hash = calculateHash(); // Calculate hash when the transaction is created
    }

    // Method to generate a digital signature for the transaction
    public void generateSignature(PrivateKey senderprivateKey) {
        String srcEncoded = Base64.getEncoder().encodeToString(source.getPublicKey().getEncoded());
        String destEncoded = Base64.getEncoder().encodeToString(destination.getPublicKey().getEncoded());
        String data = srcEncoded + destEncoded + amount;
        signature = StringUtil.applyECDSASig(senderprivateKey, data);
    }

    public boolean signTransaction(Wallet signer) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException, UnsupportedEncodingException {
        if (!this.hash.equals(this.calculateHash())) { //garantir que transação não foi modificada
            System.out.println("ERRO");
            return false;
        }
        System.out.println(signer.getPublicKey());
        System.out.println(this.sender);

        this.signature = signer.sign(hash)[1];
        return true;
    }

    // Calculate the hash of the transaction which serves as its ID
    public String calculateHash() {
        String srcEncoded = Base64.getEncoder().encodeToString(source.getPublicKey().getEncoded());
        String destEncoded = Base64.getEncoder().encodeToString(destination.getPublicKey().getEncoded());
        String data = srcEncoded + destEncoded + amount + signature; // Include the signature to ensure integrity
        return StringUtil.applySha256(data);
    }


    // Method to verify the signature
    public boolean verifySignature(PublicKey publicKey) {
        String srcEncoded = Base64.getEncoder().encodeToString(source.getPublicKey().getEncoded());
        String destEncoded = Base64.getEncoder().encodeToString(destination.getPublicKey().getEncoded());
        String data = srcEncoded + destEncoded + amount;
        return StringUtil.verifyECDSASig(publicKey, data, signature);
    }

    // Validates the transaction data integrity and signature
    public boolean isValid(PublicKey senderPublicKey) {
        // Check for a non-negative amount
        if (amount < 0) {
            System.out.println("Transaction amount cannot be negative.");
            return false;
        }

        // Verify that the transaction signature is correct
        if (!verifySignature(senderPublicKey)) {
            System.out.println("Transaction signature is invalid.");
            return false;
        }

        return true;
    }

    public boolean isSigned(){
        return this.signature !=null;
    }

    public void printTransaction() {
        System.out.println("hash:" + this.hash);
        System.out.println("from:" + this.source);
        System.out.println("to:" + this.destination);
        System.out.println("amount:" + this.amount);
        if(this.isSigned()){
            System.out.println("Is signed.");
        } else{
            System.out.println("Is not signed.");
        }
        System.out.println("Time:" + this.timestamp);
        System.out.println("Information: " + (((this.misc == null)) ? "''" : this.misc + "\n"));
    }
    public String getFormattedData() {
        return source + ":" + destination + ":" + amount;
    }

    public Wallet getSource() {
        return source;
    }

    public Wallet getDestination() {
        return destination;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public String getTransactionId() {
        return hash;
    }
}

