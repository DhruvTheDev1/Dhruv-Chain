package com.example;

import java.util.Date;

public class Block {
  public String hash; // will hold digital signature
  public String previousHash; // holds the previous blocks hash
  private String data; // block data
  private long timeStamp; // returns num of milliseconds
  private int nonce; // a number used only once in cryptographic communication

  public Block(String data, String previousHash) {
    this.data = data;
    this.previousHash = previousHash;
    this.timeStamp = new Date().getTime();
    this.hash = calculateHash();
  }

  // calculates hash from all parts of the block
  public String calculateHash() {
    String calculatedHash = SHA256HashingUtil.applyHash(
      previousHash +
      Long.toString(timeStamp) +
      data
    );
    return calculatedHash;
  }

  public void mineBlock(int difficulty) {
    //Create a string with difficulty * "0" 
    String target = new String(new char[difficulty]).replace('\0', '0'); 

    while (!hash.substring(0, difficulty).equals(target)) {
      nonce++;
      hash = calculateHash();
    }
    System.out.println("Block mined! : "  + hash);
  }
}
