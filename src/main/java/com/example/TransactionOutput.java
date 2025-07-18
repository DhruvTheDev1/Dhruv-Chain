package com.example;

import java.security.PublicKey;

public class TransactionOutput {
  public String id;
  public PublicKey recipient; // owner of the crypto
  public float value; // amount they own
  public String parentTransactionId; // transaction where this output was created

  public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
    this.recipient = recipient;
    this.value = value;
    this.parentTransactionId = parentTransactionId;
    this.id = SHA256HashingUtil.applyHash(SHA256HashingUtil.getStringFromKey(recipient) + Float.toString(value) + parentTransactionId);
  }

  public boolean isMine(PublicKey publicKey) {
    return (publicKey == recipient);
  }

}
