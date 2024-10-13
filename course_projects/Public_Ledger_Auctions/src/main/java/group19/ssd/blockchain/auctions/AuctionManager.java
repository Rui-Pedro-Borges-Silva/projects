package group19.ssd.blockchain.auctions;

import group19.ssd.blockchain.Blockchain;
import group19.ssd.blockchain.transactions.Wallet;
import group19.ssd.blockchain.utils.Pair;
import group19.ssd.blockchain.transactions.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuctionManager {
    private final List<Pair<Auction, List<Bid>>> auctions = new ArrayList<>();

    public Blockchain blockchain;

    public void addAuction(Auction auction) {
        auctions.add(new Pair<>(auction, new ArrayList<>()));  // Add auction with an empty list of bids
    }

    public void addBidToAuction(Bid bid, byte[] auctionHash) {
        for (Pair<Auction, List<Bid>> pair : auctions) {
            if (java.util.Arrays.equals(pair.getFirst().getHash(), auctionHash)) {
                pair.getSecond().add(bid);
                break;
            }
        }
    }

    public void placeBid(Bid bid, Wallet bidder) {
        Auction auction = findAuctionByHash(bid.getAuctionHash());
        if (auction == null) {
            System.out.println("Auction not found for given hash.");
            return;
        }
        if (System.currentTimeMillis() > auction.getTimeout()) {
            System.out.println("Auction has already expired.");
            return;
        }
        // Add the bid to the auction if valid
        if (bid.isValidBid()) {
            addBidToAuction(bid, bid.getAuctionHash());

            // Create and record the transaction for the bid
            Transaction bidTransaction = new Transaction(bidder, Transaction.TransactionType.BID_START);
            bidTransaction.generateSignature(bidder.getPrivateKey());  // Assuming you have a method to generate signatures
            blockchain.addTransaction(bidTransaction);
            System.out.println("Bid placed with transaction: " + bidTransaction.getTransactionId());
        } else {
            System.out.println("Bid is not valid.");
        }
    }

    public List<Bid> getBidsForAuction(byte[] auctionHash) {
        for (Pair<Auction, List<Bid>> pair : auctions) {
            if (Arrays.equals(pair.getFirst().getHash(), auctionHash)) {
                return pair.getSecond();
            }
        }
        return new ArrayList<>(); // Return an empty list if no auction is found
    }

    public void startAuction(Auction auction, Wallet seller) {
        if (seller == null || seller.getPublicKey() == null) {
            throw new IllegalArgumentException("Seller's wallet is not properly initialized.");
        }
        addAuction(auction);
        Transaction transaction = new Transaction(seller, Transaction.TransactionType.AUCTION_START);
        transaction.generateSignature(seller.getPrivateKey());  // Generate the signature
        blockchain.addTransaction(transaction);
        System.out.println("Auction started with transaction: " + transaction.getTransactionId());
    }

    public void endAuction(byte[] auctionHash, Wallet seller) {
        Auction auction = findAuctionByHash(auctionHash);
        if (auction == null) {
            System.out.println("No auction found for given hash.");
            return;
        }
        if (System.currentTimeMillis() < auction.getTimeout()) {
            System.out.println("Auction cannot end before its timeout.");
            return;
        }

        // Logic to determine the winner and handle the transaction could go here
        // Create and record the transaction for ending the auction
        Transaction endAuctionTransaction = new Transaction(seller, Transaction.TransactionType.AUCTION_END);
        endAuctionTransaction.generateSignature(seller.getPrivateKey());  // Sign the transaction
        blockchain.addTransaction(endAuctionTransaction);
        System.out.println("Auction ended with transaction: " + endAuctionTransaction.getTransactionId());
    }

    public Auction findAuctionByHash(byte[] hash) {
        for (Pair<Auction, List<Bid>> pair : auctions) {
            if (java.util.Arrays.equals(pair.getFirst().getHash(), hash)) {
                return pair.getFirst();
            }
        }
        System.out.println("auction not found");
        return null;  // Auction not found
    }

    public List<Auction> getAuctionsBySeller(byte[] sellerPublicKey) {
        List<Auction> sellerAuctions = new ArrayList<>();
        for (Pair<Auction, List<Bid>> pair : auctions) {
            if (Arrays.equals(pair.getFirst().getSellerPublicKey(), sellerPublicKey)) {
                sellerAuctions.add(pair.getFirst());
            }
        }
        return sellerAuctions;
    }

    public List<Pair<Auction, List<Bid>>> getAuctions() {
        return auctions;
    }
}
