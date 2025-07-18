package com.example;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;

public class TestChain {

  public static List<Block> blockchain = new ArrayList<>();
  public static Map<String, TransactionOutput> UTXOs = new HashMap<>(); // unspent transactions

  public static int difficulty = 3;
  public static float minimumTransaction = 0.1f;
  public static Wallet walletA;
  public static Wallet walletB;
  public static Transaction genesisTransaction; // first block

  public static void main(String[] args) {

    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

    // create wallets
    walletA = new Wallet();
    walletB = new Wallet();
    Wallet ledger = new Wallet();

    // genesis transaction
    // 10 coins to wallet A
    genesisTransaction = new Transaction(ledger.publicKey, walletA.publicKey, 10f, null);
    genesisTransaction.generateDigitalSignature(ledger.privateKey); // signs the transaction
    genesisTransaction.transactionId = "0"; // transactionId = 0
    // creates an output (wallet A owns 10 coins)
    genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value,
        genesisTransaction.transactionId));
    // adds to UTXO pool - wallet A can use the coins - id, value
    UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

    System.out.println("Creating and Mining Genesis Block...");
    Block genesis = new Block("0");
    genesis.addTransaction(genesisTransaction);
    addBlock(genesis);

    // testing
    Block block1 = new Block(genesis.hash);
    System.out.println("Wallet A Balance: " + walletA.getBalance());
    System.out.println("Wallet A is attemping to send 5 coins to wallet B...");
    block1.addTransaction(walletA.sendFunds(walletB.publicKey, 5f));
    addBlock(block1);
    System.out.println("Wallet A Balance: " + walletA.getBalance());
    System.out.println("Wallet B Balance: " + walletB.getBalance());

    Block block2 = new Block(block1.hash);
    System.out.println("Wallet A is attemping to send 100 coins to Wallet B");
    block2.addTransaction(walletA.sendFunds(walletB.publicKey, 100f));
    addBlock(block2);
    System.out.println("Wallet A Balance: " + walletA.getBalance());
    System.out.println("Wallet B Balance: " + walletB.getBalance());

    Block block3 = new Block(block2.hash);
    System.out.println("Wallet B is attemping to send 5 coins to Wallet A");
    block3.addTransaction(walletB.sendFunds(walletA.publicKey, 5f));
    addBlock(block3)
    System.out.println("Wallet A Balance: " + walletA.getBalance());
    System.out.println("Wallet B Balance: " + walletB.getBalance());

    validate();

    // System.out.println("Public key: " +
    // SHA256HashingUtil.getStringFromKey(walletA.publicKey));
    // System.out.println("Private key: " +
    // SHA256HashingUtil.getStringFromKey(walletA.privateKey));

    // test -> wallet A to wallet B
    // from, to, value, inputs
    // Transaction transaction = new Transaction(walletA.publicKey,
    // walletB.publicKey, 5, null);
    // transaction.generateDigitalSignature(walletA.privateKey);

    // System.out.println("Signature Verified " +
    // transaction.verifiyDigitalSignature());

    // //data, previous hash
    // blockchain.add(new Block("First Block", "0"));
    // System.out.println("Trying to mine block 1...");
    // blockchain.get(0).mineBlock(difficulty);

    // blockchain.add(new Block("Second Block", blockchain.get(blockchain.size() -
    // 1).hash));
    // System.out.println("Trying to mine block 2...");
    // blockchain.get(1).mineBlock(difficulty);

    // blockchain.add(new Block("Third Block", blockchain.get(blockchain.size() -
    // 1).hash));
    // System.out.println("Trying to mine block 3...");
    // blockchain.get(2).mineBlock(difficulty);

    // System.out.println("\nBlockchain is valid: " + validate());

    // String blockchainJson = new
    // GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
    // System.out.println("\nThe blockchain: ");
    // System.out.println(blockchainJson);
  }

  public static boolean validate() {
    Block currentBlock;
    Block prevBlock;
    String hashTarget = new String(new char[difficulty]).replace('\0', '0');
    Map<String, TransactionOutput> tempUTXOs = new HashMap<>();
    tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

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

      // loops through blockchain transactions
      TransactionOutput temOutput;
      for (int j = 0; j < currentBlock.transactions.size(); j++) {
        Transaction currTransaction = currentBlock.transactions.get(j);

        if (!currTransaction.verifiyDigitalSignature()) {
          System.out.println("Signature is invalid");
          return false;
        }

        if (currTransaction.getInputsValue() != currTransaction.getOutputValue()) {
          System.out.println("Inputs not equal to output");
          return false;
        }

        for (TransactionInput input : currTransaction.inputs) {
          temOutput = tempUTXOs.get(input.transactionOutputId);

          if (temOutput == null) {
            System.out.println("Referenced input in transaction missing");
            return false;
          }

          if (input.UTXO.value != temOutput.value) {
            System.out.println("Referenced input transaction value is invalid");
            return false;
          }

          tempUTXOs.remove(input.transactionOutputId);
        }

        for (TransactionOutput output : currTransaction.outputs) {
          tempUTXOs.put(output.id, output);
        }
        // crypto being sent is not going to the correct recipient
        if (currTransaction.outputs.get(0).recipient != currTransaction.recipient) {
          System.out.println("Recipient is not who it should be");
          return false;
        }
        // checks if change (leftover from transaction) is being sent to the owner
        if (currTransaction.outputs.get(1).recipient != currTransaction.sender) {
          System.out.println("Leftover change is not the owner");
          return false;
        }
      }
    } // end of for

    System.out.println("Blockchain is valid");
    return true;
  }

  private static void addBlock(Block newBlock) {
    newBlock.mineBlock(difficulty);
    blockchain.add(newBlock);
  }
}
