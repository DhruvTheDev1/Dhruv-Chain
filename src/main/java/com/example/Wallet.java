package com.example;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.util.test.Test;

public class Wallet {
  public PublicKey publicKey; // will act as address
  public PrivateKey privateKey;
  public Map<String, TransactionOutput> UTXOs = new HashMap<>();

  public Wallet() {
    generateKeyPair();
  }

  // generates pairs of public and private keys
  // uses Elliptic-curve cryptography algorithm
  private void generateKeyPair() {
    try {
      // creates KeyPairGenerator object that will make public/private keys
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");

      SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      // The curve is defined over a prime field, with a prime number as the modulus
      ECGenParameterSpec eSpec = new ECGenParameterSpec("prime192v1");

      // initialise key generator and generate a keyPair
      keyPairGenerator.initialize(eSpec, random);
      KeyPair keyPair = keyPairGenerator.generateKeyPair();

      privateKey = keyPair.getPrivate();
      publicKey = keyPair.getPublic();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // checks how much crypto is available
  public float getBalance() {
    float total = 0;
    // goes through all unspoint coins in the blockchain
    for (Map.Entry<String, TransactionOutput> item : TestChain.UTXOs.entrySet()) {
      TransactionOutput UTXO = item.getValue();
      // if it belongs to the wallet - adds it
      if (UTXO.isMine(publicKey)) {
        UTXOs.put(UTXO.id, UTXO);
        total += UTXO.value;
      }
    }
    return total;
  }

  // send crypto to recipient
  public Transaction sendFunds(PublicKey recipient, float value) {
    // check if enough balance
    if (getBalance() < value) {
      System.out.println("Not enough funds available");
      return null;
    }
    List<TransactionInput> inputs = new ArrayList<>();
    // loops through the wallets UTXO
    // adds them to a list of inputs and breaks once enough coins are available to send
    float total = 0;
    for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
      TransactionOutput UTXO = item.getValue();
      total += UTXO.value;
      inputs.add(new TransactionInput(UTXO.id));
      if (total > value) {
        break;
      }
    }
    // creates and signs the transaction
    Transaction newTransaction = new Transaction(publicKey, recipient, value, inputs);
    newTransaction.generateDigitalSignature(privateKey);

    // the coins are spent hence removed from available UTXO
    for (TransactionInput input : inputs) {
      UTXOs.remove(input.transactionOutputId);
    }

    return newTransaction;
  }
}
