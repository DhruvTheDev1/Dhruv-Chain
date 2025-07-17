package com.example;

public class TransactionInput {
  // every new transaction input must point to a previous transaction output (UTXO)
  public String transactionOutputId; // references to transactionOutput
  public TransactionOutput UTXO; // unspent transaction output

  public TransactionInput(String transactionOutputId) {
    this.transactionOutputId = transactionOutputId;
  }
}
