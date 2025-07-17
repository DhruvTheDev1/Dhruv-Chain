package com.example;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;

public class TestChain {

  public static List<Block> blockchain = new ArrayList<>();
  public static int difficulty = 3;
  public static Map<String, TransactionOutput> UTXOs = new HashMap<>(); // unspent transactions
  public static Wallet walletA;
  public static Wallet walletB;

  public static void main(String[] args) {

    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

    // creating wallets
    walletA = new Wallet();
    walletB = new Wallet();

    System.out.println("Public key: " + SHA256HashingUtil.getStringFromKey(walletA.publicKey));
    System.out.println("Private key: " + SHA256HashingUtil.getStringFromKey(walletA.privateKey));

    // test -> wallet A to wallet B
    //from, to, value, inputs
    Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
    transaction.generateDigitalSignature(walletA.privateKey);

    System.out.println("Signature Verified " + transaction.verifiyDigitalSignature());

    // //data, previous hash
    // blockchain.add(new Block("First Block", "0"));
    // System.out.println("Trying to mine block 1...");
    // blockchain.get(0).mineBlock(difficulty);

    // blockchain.add(new Block("Second Block", blockchain.get(blockchain.size() - 1).hash));
    // System.out.println("Trying to mine block 2...");
    // blockchain.get(1).mineBlock(difficulty);

    // blockchain.add(new Block("Third Block", blockchain.get(blockchain.size() - 1).hash));
    // System.out.println("Trying to mine block 3...");
    // blockchain.get(2).mineBlock(difficulty);

    // System.out.println("\nBlockchain is valid: " + validate());



    // String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
    // System.out.println("\nThe blockchain: ");
    // System.out.println(blockchainJson);
  }

  public static boolean validate() {
    Block currentBlock;
    Block prevBlock;
    String hashTarget = new String(new char[difficulty]).replace('\0', '0'); 

    for (int i = 1; i < blockchain.size(); i++) {
      currentBlock = blockchain.get(i);
      prevBlock = blockchain.get(i - 1);
    
      if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
        System.out.println("Current hashes not equal");
        return false;
      }

      if (!prevBlock.hash.equals(currentBlock.previousHash)) {
        System.out.println("Previous hash not equal");
        return false;
      }

      // check if hash is solved
      if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
        System.out.println("This block hasn't been mined");
        return false;
      }
    }

    return true;
  }
}
