package com.example;

import java.util.Date;

public class Blocks {
  public String hash; // will hold digital signature
  public String previousHash; // holds the previous blocks hash
  private String data; // block data
  private long timeStamp; // returns num of milliseconds

  public Blocks(String data, String previousHash) {
    this.data = data;
    this.previousHash = previousHash;
    this.timeStamp = new Date().getTime();
  }
}
