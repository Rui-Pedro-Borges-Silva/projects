package group19.ssd.blockchain;

import group19.ssd.p2p.grpc.Transaction;
import group19.ssd.p2p.grpc.BlockChain;
import group19.ssd.p2p.grpc.TransactionsList;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class BCConverter {
    public static Blockchain mkBlockchain(BlockChain bc) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Blockchain blockchain = new Blockchain();
        for(group19.ssd.p2p.grpc.Block block : bc.getChainList()){
            Blockchain.chain.add(mkBlock(block));
        }
        Blockchain.pendingList = mkTransactionList(bc.getPendingTransactions());
        return blockchain;
    }

    public static Block mkBlock(group19.ssd.p2p.grpc.Block block){
        return new Block(
                block.getHashId(),
                block.getHash(),
                block.getPreviousHash(),
                mkTransactionList(block.getTransactionsList()),
                block.getNonce(),
                block.getTimestamp(),
                block.getPublicKey());
    }

    private static ArrayList<group19.ssd.blockchain.transactions.Transaction> mkTransactionList(TransactionsList transactions){
        ArrayList<group19.ssd.blockchain.transactions.Transaction> transactionList = new ArrayList<>();
        for(Transaction t : transactions.getTransactionListList()){
            transactionList.add(mkTransaction(t));
        }
        return transactionList;
    }

    public static group19.ssd.blockchain.transactions.Transaction mkTransaction(Transaction transaction){
        return new group19.ssd.blockchain.transactions.Transaction(
                transaction.getSender(),
                transaction.getReceiver(),
                transaction.getSignature().toByteArray(),
                transaction.getTimestamp(),
                transaction.getAmount(),
                transaction.getMisc());
    }
}
