package group19.ssd.blockchain.auctions;

import group19.ssd.blockchain.transactions.Wallet;

public class Bid {
    private final byte[] transactionId;
    private final byte[] auctionHash;
    private long timestamp;

    private static AuctionManager auctionManager;  // Reference to the AuctionManager

    public Bid(byte[] transactionId, byte[] auctionHash, long timestamp) {
        this.transactionId = transactionId;
        this.auctionHash = auctionHash;
        this.timestamp = timestamp;
    }

    public Bid(byte[] transactionId, byte[] auctionHash, Wallet sender) {
        this.transactionId = transactionId;
        this.auctionHash = auctionHash;
    }

    public static void setAuctionManager(AuctionManager auctionManager) {
        Bid.auctionManager = auctionManager;
    }

    public boolean isValidBid() {
        Auction auction = auctionManager.findAuctionByHash(auctionHash);
        if (auction == null) {
            System.out.println("No auction found for given hash.");
            return false;
        }
        if (this.timestamp >= auction.getTimeout()) {
            System.out.println("Bid timestamp is invalid or auction has expired.");
            System.out.println("Bid Time: " + timestamp + " Auction Timeout: " + auction.getTimeout());
            return false;
        }
        return true;
    }


    public byte[] getTransactionId() {
        return transactionId;
    }

    public byte[] getAuctionHash() {
        return auctionHash;
    }


    public long getTimestamp() {
        return timestamp;
    }

}
