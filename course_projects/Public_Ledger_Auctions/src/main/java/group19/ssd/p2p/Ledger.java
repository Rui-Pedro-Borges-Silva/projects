package group19.ssd.p2p;

import group19.ssd.blockchain.Block;
import group19.ssd.blockchain.transactions.Transaction;
import group19.ssd.miscellaneous.Configuration;

import java.util.Hashtable;

public class Ledger {
    Hashtable<String, Long> users = new Hashtable<>();
    long minCoin = 15;

    public Ledger() {
        users.put(KademliaClient.publicKey, minCoin);
    }

    public Long getBalance(String pubKey) {
        return users.get(pubKey);
    }

    public void reset() {
        users.clear();
        users.put(KademliaClient.publicKey, (long) minCoin);
    }

    public void restartLedger() {
        reset();
        for (Block block : KademliaClient.blockchain.getChain()) {
            updateLedger(block);
        }
    }

    public void updateLedger(Block block) {
        for (Transaction transaction : block.getTransactions()) {
            Long senderAmount, receiverAmount;
            if (users.get(transaction.sender) != null) {        //source é o senderPK
                senderAmount = users.get(transaction.sender);
            } else {
                senderAmount = minCoin;
            }
            if (users.get(transaction.receiver) != null) {
                receiverAmount = users.get(transaction.receiver);
            } else {
                receiverAmount = minCoin;
            }
            users.put(transaction.sender, (senderAmount - transaction.amount));
            users.put(transaction.receiver, (receiverAmount + transaction.amount));
        }
        if (users.get(block.publicKey) != null) {
            users.put(block.publicKey, users.get(block.publicKey) + 1);
        } else {
            users.put(block.publicKey, minCoin + Configuration.MINING_REWARD);
        }

    }
}
