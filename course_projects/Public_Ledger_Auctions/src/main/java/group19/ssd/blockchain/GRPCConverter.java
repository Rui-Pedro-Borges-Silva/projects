package group19.ssd.blockchain;

import com.google.protobuf.ByteString;
import group19.ssd.p2p.KademliaClient;
import group19.ssd.p2p.grpc.TransactionsList;
import group19.ssd.p2p.grpc.BlockChain;
import group19.ssd.p2p.grpc.Transaction;

import java.util.ArrayList;

public class GRPCConverter {

    public static BlockChain mkBlockChain(Blockchain bChain) {
        ArrayList<group19.ssd.p2p.grpc.Block> Chain_of_blocks = new ArrayList<>();
        for(Block b : bChain.chain){
            Chain_of_blocks.add(mkBlock(b));
        }
        return BlockChain.newBuilder().addAllChain(Chain_of_blocks).setPendingTransactions(mkTransactionList(bChain.getPendingList())).build();
    }

    //convert block
    public static group19.ssd.p2p.grpc.Block mkBlock(Block block) {

        return group19.ssd.p2p.grpc.Block.newBuilder()
                .setHashId(block.hashId)
                .setHash(block.hash)
                .setPreviousHash(block.previousHash)
                .setTransactionsList(mkTransactionList(block.data))
                .setNonce(block.nonce)
                .setTimestamp(block.timestamp)
                .setPublicKey(block.publicKey)
                .setNodeId(KademliaClient.id)
                .build();
    }

    //convert list of transactions
    public static TransactionsList mkTransactionList(ArrayList<group19.ssd.blockchain.transactions.Transaction> transactions) {
        ArrayList<Transaction> tlist = new ArrayList<>();
        for (group19.ssd.blockchain.transactions.Transaction t : transactions) {
            tlist.add(Transaction.newBuilder()
                    .setHash(t.hash)
                    .setSender(t.sender)
                    .setReceiver(t.receiver)
                    .setSignature(ByteString.copyFrom(t.signature))
                    .setTimestamp(t.timestamp)
                    .setAmount(t.amount)
                    .setMisc(t.misc)
                    .build());
        }
        return TransactionsList.newBuilder().addAllTransactionList(tlist).build();
    }
    // convert transaction
    public static Transaction mkTransaction(group19.ssd.blockchain.transactions.Transaction t) {
        Transaction new_t = Transaction.newBuilder()
                .setHash(t.hash)
                .setSender(t.sender)
                .setReceiver(t.receiver)
                .setSignature(ByteString.copyFrom(t.signature))
                .setTimestamp(t.timestamp)
                .setAmount(t.amount)
                .setMisc(t.misc)
                .setNodeId(KademliaClient.id)
                .build();
        return new_t;
    }
}
