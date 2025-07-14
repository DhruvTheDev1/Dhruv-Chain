package com.example;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;

public class TestChain {

  public static List<Block> blockchain = new ArrayList<>();

  public static void main(String[] args) {
    // data, previous hash
    Block genesisBlock = new Block("First Block", "0");
    Block secondBlock = new Block("Second Block", genesisBlock.hash);
    Block thirdBlock = new Block("Third block", secondBlock.hash);

    blockchain.add(genesisBlock);
    blockchain.add(secondBlock);
    blockchain.add(thirdBlock);

    String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
    System.out.println(blockchainJson);
  }

  public static boolean validate() {
    Block currentBlock;
    Block prevBlock;

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
    }

    return true;
  }
}
