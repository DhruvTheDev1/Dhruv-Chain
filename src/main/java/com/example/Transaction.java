package com.example;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.util.test.Test;

public class Transaction {
  public String transactionId; // hash of the transaction
  public PublicKey sender; // senders address - public key
  public PublicKey recipient; // recipients address - public key (who we are sending to)
  public float value; // value we are sending
  public byte[] signature; // cryptographic signature - proves owner of the address is the one sending

  public List<TransactionInput> inputs = new ArrayList<>();
  public List<TransactionOutput> outputs = new ArrayList<>();

  public static int sequence = 0; // count of how many transactions

  public Transaction(PublicKey from, PublicKey to, float value, List<TransactionInput> inputs) {
    this.sender = from;
    this.recipient = to;
    this.value = value;
    this.inputs = inputs;
  }

  // calculates transaction hash
  private String calculateHash() {
    sequence++;
    return SHA256HashingUtil.applyHash(
        SHA256HashingUtil.getStringFromKey(sender) +
            SHA256HashingUtil.getStringFromKey(recipient) +
            Float.toString(value) + sequence);
  }

  // creates digital signature for transaction details
  // sender, recipient, value
  public void generateDigitalSignature(PrivateKey privateKey) {
    String data = SHA256HashingUtil.getStringFromKey(sender) + SHA256HashingUtil.getStringFromKey(recipient) +
        Float.valueOf(value);

    this.signature = SHA256HashingUtil.applyDigitalSig(privateKey, data);
  }

  // verifies the authenticity of the transaction
  public boolean verifiyDigitalSignature() {
    String data = SHA256HashingUtil.getStringFromKey(sender) + SHA256HashingUtil.getStringFromKey(recipient) +
        Float.valueOf(value);

    return SHA256HashingUtil.verifyDigitalSig(sender, data, this.signature);
  }

  // currency transaction system - similar to how bitcoin handles transaction with UTXOs
  public boolean processTransaction() {
    
    // verifies digital signature
    if (verifiyDigitalSignature() == false) {
      System.out.println("Tranasction Signature Failed To Verify");
      return false;
    }
    // gathers transaction inputs
    // for each input - fetches the UTXO from list
    // UTXOs - coins that can be spent
    for(TransactionInput transactionInput : inputs) {
      transactionInput.UTXO = TestChain.UTXOs.get(transactionInput.transactionOutputId);
    }

    //checks total input is enough
    if (getInputsValue() < TestChain.minimumTransaction) {
      System.out.println("Transaction Inputs Too Small " + getInputsValue());
      return false;
    }

    // generates transaction output
    float change = getInputsValue() - value; // calculates leftover value
    transactionId = calculateHash();
    outputs.add(new TransactionOutput(recipient, value, transactionId)); // send crypto to recipient
    outputs.add(new TransactionOutput(sender, change, transactionId)); // send leftover crypto back to sender

    // adds output to UTXO list - marks as unspent and available for future transactions
    for(TransactionOutput transactionOutput : outputs) {
      TestChain.UTXOs.put(transactionOutput.id, transactionOutput);
    }

    // removes spent input from UTXO list
    // marks as spent hence no longer valid
    for(TransactionInput transactionInput : inputs) {
      if (transactionInput.UTXO == null) {
        continue;
      } else {
        TestChain.UTXOs.remove(transactionInput.UTXO.id);
      }
    }

    return true;

  }
  // sums value of all UTXOs - total crypto available to spend
  private float getInputsValue() {
    float total = 0;
    for(TransactionInput num : inputs) {
      if (num.UTXO == null) {
        continue;
      } else {
        total += num.UTXO.value;
      }
    }
    return total;
  }

  // returns total value of all outputs in a transaction
  public float getOutputValue() {
    float total = 0;
    for(TransactionOutput output : outputs) {
      total += output.value;
    }
    return total;
  }

}
