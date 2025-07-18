package com.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {
  public String hash; // will hold digital signature
  public String previousHash; // holds the previous blocks hash
  public String merkleRoot; // cryptographic hash of all hashes in the tree
  public List<Transaction> transactions = new ArrayList<>(); // data will be a simple message
  public long timeStamp; // returns num of milliseconds
  public int nonce; // a number used only once in cryptographic communication

  public Block(String previousHash) {
    this.previousHash = previousHash;
    this.timeStamp = new Date().getTime();
    this.hash = calculateHash();
  }

  // calculates hash from all parts of the block
  public String calculateHash() {
    String calculatedHash = SHA256HashingUtil.applyHash(
      previousHash +
      Long.toString(timeStamp) +
      Integer.toString(nonce) +
      merkleRoot
    );
    return calculatedHash;
  }

  public void mineBlock(int difficulty) {
    merkleRoot = SHA256HashingUtil.getMerkleRoot(transactions);

    //Create a string with difficulty * "0" 
    String target = new String(new char[difficulty]).replace('\0', '0'); 

    while (!hash.substring(0, difficulty).equals(target)) {
      nonce++;
      hash = calculateHash();
    }
    System.out.println("Block mined! : "  + hash);
  }
  public boolean addTransaction(Transaction transaction) {
    // returns false if null transaction
    if (transaction == null) {
      return false;
    }
    // checks if it isnt the gensis block (first block)
    if (!previousHash.equals("0")) {
      // runs processTransaction()
      // if not true e.g. valid signature - returns false
      if (transaction.processTransaction() != true) {
        System.out.println("Transaction Failed. Discarded");
        return false;
      }
    }
    // or else adds it to list of transactions
    transactions.add(transaction);
    System.out.println("Transaction Successfully added to Block");
    return true;
  }
}




