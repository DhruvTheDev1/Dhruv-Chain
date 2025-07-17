package com.example;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

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

}
