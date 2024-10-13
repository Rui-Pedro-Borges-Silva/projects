package group19.ssd.blockchain;

import group19.ssd.blockchain.transactions.Transaction;
import group19.ssd.blockchain.utils.StringUtil;
import group19.ssd.miscellaneous.Miscellaneous;
import group19.ssd.miscellaneous.Configuration;
import java.util.ArrayList;
import java.util.List;

public class MerkleTree {
    ArrayList<Transaction> list;
    String root;

    public MerkleTree(ArrayList<Transaction> list) {
        this.list = list;
        this.root = "";
    }

    public void calculateMerkleRoot() {
        // empty root
        if (list.size() != Configuration.MAX_TRANSACTIONS_PER_BLOCK) return;
        //otherwise hash is calculated every transaction
        List<String> treeList = new ArrayList<>();
        for (Transaction transaction : list) {
            treeList.add(transaction.getFormattedData());
        }
        while (treeList.size() > 1) {
            List<String> newTreeList = new ArrayList<>();
            int index = 0;
            while (index < treeList.size()) {
                // Grab the left and right if available
                String left = treeList.get(index);
                String right = index+1 < treeList.size() ? treeList.get(index+1) : left;
                // Combine and hash them, then add to new list
                String combinedHash = StringUtil.applySha256(left + right);
                newTreeList.add(combinedHash);
                index += 2;
            }
            treeList = newTreeList;
        }
        this.root = treeList.get(0); // Root of the tree
    }

    public String getRoot(){
        return this.root;
    }
}
