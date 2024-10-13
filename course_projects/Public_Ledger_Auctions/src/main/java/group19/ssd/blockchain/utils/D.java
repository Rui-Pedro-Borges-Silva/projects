package group19.ssd.blockchain.utils;

import group19.ssd.blockchain.Block;
import group19.ssd.blockchain.Blockchain;
import group19.ssd.blockchain.transactions.Transaction;
import group19.ssd.blockchain.transactions.Wallet;
import group19.ssd.p2p.KademliaClient;

import java.util.ArrayList;

public class D {
    public static KademliaClient client1 = new KademliaClient();
    public static Wallet Wc1 = new Wallet();
    public static KademliaClient client2 = new KademliaClient();
    public static Wallet Wc2 = new Wallet();
    public static KademliaClient client3 = new KademliaClient();
    public static Wallet Wc3 = new Wallet();
    public static ArrayList<Transaction> transactions = new ArrayList<>();

    /*public void CreateCWallets(){
        client1.wallet = Wc1;
        client2.wallet = Wc2;
        client3.wallet = Wc3;
    }*/

    public static ArrayList<Transaction> createLT(KademliaClient term_client) {
        String sender = String.valueOf(term_client.wallet.getPublicKey());
        String receiver = String.valueOf(client1.wallet.getPublicKey());
        int amount = 200;
        Transaction t1 = new Transaction(sender, receiver, amount);

        receiver = String.valueOf(client2.wallet.getPublicKey());
        amount = 300;
        Transaction t2 = new Transaction(sender, receiver, amount);

        receiver = String.valueOf(client2.wallet.getPublicKey());
        amount = 400;
        Transaction t3 = new Transaction(sender, receiver, amount);

        receiver = String.valueOf(client3.wallet.getPublicKey());
        amount = 500;
        Transaction t4 = new Transaction(sender, receiver, amount);


        transactions.add(t1);
        transactions.add(t2);
        transactions.add(t3);
        transactions.add(t4);
        return transactions;
    }

    public static Blockchain AddBC() {
        Blockchain bchain = new Blockchain();
        bchain.addTransaction(transactions.get(0));
        bchain.addTransaction(transactions.get(1));
        bchain.addTransaction(transactions.get(2));
        bchain.addTransaction(transactions.get(3));
        return bchain;
    }
}
