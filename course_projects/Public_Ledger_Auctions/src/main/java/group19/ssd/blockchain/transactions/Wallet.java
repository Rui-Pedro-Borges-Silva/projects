package group19.ssd.blockchain.transactions;

import group19.ssd.miscellaneous.Miscellaneous;
import group19.ssd.p2p.KademliaClient;
import group19.ssd.blockchain.auctions.Auction;
import group19.ssd.blockchain.auctions.Bid;
import group19.ssd.blockchain.Blockchain;
import group19.ssd.blockchain.utils.Pair;
import group19.ssd.blockchain.utils.StringUtil;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Wallet {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    // bids done by this wallet with the auction they belong to
    private final List<Pair<Bid, Auction>> bids = new ArrayList<>();
    // auctions started by this wallet with the bids they have received
    private final List<Pair<Auction, List<Bid>>> auctions = new ArrayList<>();

    private static Blockchain blockchain;


    public Wallet(PublicKey publicKey, PrivateKey privateKey) {
        this.privateKey=privateKey;
        this.publicKey=publicKey;
    }


    public Wallet(){
        try {
            KeyPair keypair = StringUtil.generateKeyPair();
            privateKey = keypair.getPrivate();
            publicKey = keypair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to initialize Wallet: Algorithm not found", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setBlockchain(Blockchain blockchain){
        Wallet.blockchain = blockchain;
    }

    //Getters
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void printWalletBalance() {
        System.out.print("Your account balance is: ");
        System.out.println(getBalance());
    }

    public Long getBalance() {
        String pubKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        System.out.println(pubKey);
        return KademliaClient.ledger.getBalance(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
    }

    public byte[][] sign(String message) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException {

        String pubKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        byte[] msgHash = Miscellaneous.applyEncryption(pubKey.concat(String.valueOf(message))).getBytes();
        Signature signature = Signature.getInstance("RSA");

        System.out.println(privateKey);

        String privKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        byte[] keyBytes = Base64.getDecoder().decode(privKey.getBytes(StandardCharsets.UTF_8));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = fact.generatePrivate(keySpec);

        signature.initSign(privateKey);
        signature.update(msgHash);
        byte[] signatureBytes = signature.sign();
        return new byte[][]{msgHash, signatureBytes};

    }
}
